package com.manosalaobra.negocio.negocio.impl;

import com.manosalaobra.negocio.negocio.reglas.impl.indicativopais.AsegurarIndicativoPaisExisteRegla;

import com.manosalaobra.negocio.negocio.ensamblador.impl.IndicativoPaisEnsambladorEntidad;

import com.manosalaobra.dominio.IndicativoPaisDominio;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.EstadoUsuarioDominio;
import com.manosalaobra.dominio.RolDominio;
import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.entidad.EstadoUsuarioEntidad;
import com.manosalaobra.entidad.RolEntidad;
import com.manosalaobra.entidad.UsuarioEntidad;
import com.manosalaobra.negocio.negocio.UsuarioNegocio;
import com.manosalaobra.negocio.negocio.ensamblador.impl.EstadoUsuarioEnsambladorEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.impl.RolEnsambladorEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.impl.TecnicoEnsambladorEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.impl.UsuarioEnsambladorEntidad;
import com.manosalaobra.negocio.negocio.reglas.impl.tecnico.AsegurarDocumentoNoRegistradoRegla;
import com.manosalaobra.negocio.negocio.reglas.impl.tecnico.ValidarDatosRegistrarNuevoTecnicoRegla;
import com.manosalaobra.negocio.negocio.reglas.impl.usuario.AsegurarCorreoNoRegistradoRegla;
import com.manosalaobra.negocio.negocio.reglas.impl.usuario.AsegurarCredencialesValidasRegla;
import com.manosalaobra.negocio.negocio.reglas.impl.usuario.AsegurarUsuarioAdministrableRegla;
import com.manosalaobra.negocio.negocio.reglas.impl.usuario.ValidarDatosIniciarSesionRegla;
import com.manosalaobra.negocio.negocio.reglas.impl.usuario.ValidarDatosRegistrarNuevoUsuarioRegla;
import com.manosalaobra.negocio.negocio.reglas.impl.usuario.ValidarEstadoUsuarioRegla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.Claves;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class UsuarioNegocioImpl implements UsuarioNegocio {

    private final DAOFactory daoFactory;

    public UsuarioNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public UsuarioDominio registrarCiudadano(UsuarioDominio usuario) {
        ValidarDatosRegistrarNuevoUsuarioRegla.obtenerInstancia().ejecutar(usuario);
        AsegurarCorreoNoRegistradoRegla.obtenerInstancia().ejecutar(usuario.getCorreo(), daoFactory);
        AsegurarIndicativoPaisExisteRegla.obtenerInstancia().ejecutar(usuario.getIndicativoPais().getId(), daoFactory);
        return crearUsuario(usuario, RolDominio.CIUDADANO);
    }

    @Override
    public TecnicoDominio registrarTecnico(TecnicoDominio tecnico) {
        ValidarDatosRegistrarNuevoTecnicoRegla.obtenerInstancia().ejecutar(tecnico);
        ValidarDatosRegistrarNuevoUsuarioRegla.obtenerInstancia().ejecutar(tecnico.getUsuario());
        AsegurarCorreoNoRegistradoRegla.obtenerInstancia().ejecutar(tecnico.getUsuario().getCorreo(), daoFactory);
        AsegurarDocumentoNoRegistradoRegla.obtenerInstancia().ejecutar(tecnico.getDocumento(), daoFactory);
        AsegurarIndicativoPaisExisteRegla.obtenerInstancia().ejecutar(
                tecnico.getUsuario().getIndicativoPais().getId(), daoFactory);

        // El técnico queda sin perfil: especialidad, barrio y tarifa se completan en HU-04
        TecnicoDominio tecnicoNuevo = new TecnicoDominio.Builder()
                .id(UtilUUID.generar())
                .usuario(crearUsuario(tecnico.getUsuario(), RolDominio.TECNICO))
                .tipoDocumento(tecnico.getTipoDocumento())
                .documento(tecnico.getDocumento())
                .build();
        daoFactory.obtenerTecnicoDAO().crear(TecnicoEnsambladorEntidad.getInstancia().convertirAEntidad(tecnicoNuevo));
        return tecnicoNuevo;
    }

    @Override
    public UsuarioDominio iniciarSesion(UsuarioDominio credenciales) {
        ValidarDatosIniciarSesionRegla.obtenerInstancia().ejecutar(credenciales);

        UsuarioEntidad filtro = new UsuarioEntidad();
        filtro.setCorreo(credenciales.getCorreo());
        List<UsuarioEntidad> encontrados = daoFactory.obtenerUsuarioDAO().consultarPorFiltro(filtro);
        UsuarioEntidad usuario = encontrados.isEmpty() ? new UsuarioEntidad() : encontrados.get(0);

        AsegurarCredencialesValidasRegla.obtenerInstancia().ejecutar(usuario, credenciales.getClave());
        return UsuarioEnsambladorEntidad.getInstancia().convertirADominio(usuario);
    }

    // HU-16: el administrador consulta todas las cuentas (el hash de la clave no sale de la fachada)
    @Override
    public List<UsuarioDominio> consultarTodos() {
        List<UsuarioDominio> usuarios = new ArrayList<>();
        for (UsuarioEntidad entidad : daoFactory.obtenerUsuarioDAO().consultarTodos()) {
            usuarios.add(UsuarioEnsambladorEntidad.getInstancia().convertirADominio(entidad));
        }
        return usuarios;
    }

    // HU-16: buscar por nombre o correo (texto) y filtrar por rol y estado; los filtros vacíos no filtran
    @Override
    public List<UsuarioDominio> consultarTodos(String texto, String rol, String estado) {
        String buscado = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(texto).toLowerCase();
        String rolBuscado = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(rol);
        String estadoBuscado = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(estado);
        List<UsuarioDominio> filtrados = new ArrayList<>();
        for (UsuarioDominio usuario : consultarTodos()) {
            boolean coincideTexto = buscado.isEmpty()
                    || usuario.getNombre().toLowerCase().contains(buscado)
                    || usuario.getCorreo().toLowerCase().contains(buscado);
            boolean coincideRol = rolBuscado.isEmpty() || rolBuscado.equalsIgnoreCase(usuario.getRol().getNombre());
            boolean coincideEstado = estadoBuscado.isEmpty()
                    || estadoBuscado.equalsIgnoreCase(usuario.getEstadoUsuario().getNombre());
            if (coincideTexto && coincideRol && coincideEstado) {
                filtrados.add(usuario);
            }
        }
        return filtrados;
    }

    // HU-16: suspender, bloquear (con motivo) o reactivar una cuenta que no sea la propia ni la de otro administrador
    @Override
    public UsuarioDominio cambiarEstado(UUID administradorId, UUID usuarioId, String nombreEstado, String motivo) {
        ValidarEstadoUsuarioRegla.obtenerInstancia().ejecutar(nombreEstado, motivo);
        // Sin esta guarda el DAO traería la primera cuenta cuando no llega el id
        UsuarioEntidad usuario = UtilUUID.esValorDefecto(usuarioId)
                ? new UsuarioEntidad() : daoFactory.obtenerUsuarioDAO().consultarPorId(usuarioId);
        AsegurarUsuarioAdministrableRegla.obtenerInstancia().ejecutar(usuario, administradorId);

        UsuarioEntidad cambio = new UsuarioEntidad();
        cambio.setEstadoUsuario(EstadoUsuarioEnsambladorEntidad.getInstancia()
                .convertirAEntidad(consultarEstadoUsuarioPorNombre(nombreEstado)));
        // Al reactivar la cuenta el motivo anterior se borra
        cambio.setMotivoEstado(EstadoUsuarioDominio.ACTIVO.equals(nombreEstado) ? UtilTexto.VACIO : motivo);
        daoFactory.obtenerUsuarioDAO().actualizar(usuarioId, cambio);
        return UsuarioEnsambladorEntidad.getInstancia().convertirADominio(
                daoFactory.obtenerUsuarioDAO().consultarPorId(usuarioId));
    }

    private UsuarioDominio crearUsuario(UsuarioDominio usuario, String nombreRol) {
        IndicativoPaisDominio indicativoPais = IndicativoPaisEnsambladorEntidad.getInstancia().convertirADominio(
                daoFactory.obtenerIndicativoPaisDAO().consultarPorId(usuario.getIndicativoPais().getId()));

        UsuarioDominio usuarioNuevo = new UsuarioDominio.Builder()
                .id(UtilUUID.generar())
                .rol(consultarRolPorNombre(nombreRol))
                .estadoUsuario(consultarEstadoUsuarioPorNombre(EstadoUsuarioDominio.ACTIVO))
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .claveHash(Claves.cifrar(usuario.getClave()))
                .indicativoPais(indicativoPais)
                .numeroTelefono(usuario.getNumeroTelefono())
                .build();

        daoFactory.obtenerUsuarioDAO().crear(UsuarioEnsambladorEntidad.getInstancia().convertirAEntidad(usuarioNuevo));
        return usuarioNuevo;
    }

    private RolDominio consultarRolPorNombre(String nombre) {
        RolEntidad filtro = new RolEntidad();
        filtro.setNombre(nombre);
        List<RolEntidad> roles = daoFactory.obtenerRolDAO().consultarPorFiltro(filtro);
        if (roles.isEmpty()) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_ROL_NO_CONFIGURADO);
        }
        return RolEnsambladorEntidad.getInstancia().convertirADominio(roles.get(0));
    }

    private EstadoUsuarioDominio consultarEstadoUsuarioPorNombre(String nombre) {
        EstadoUsuarioEntidad filtro = new EstadoUsuarioEntidad();
        filtro.setNombre(nombre);
        List<EstadoUsuarioEntidad> estados = daoFactory.obtenerEstadoUsuarioDAO().consultarPorFiltro(filtro);
        if (estados.isEmpty()) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_ESTADO_NO_CONFIGURADO);
        }
        return EstadoUsuarioEnsambladorEntidad.getInstancia().convertirADominio(estados.get(0));
    }
}
