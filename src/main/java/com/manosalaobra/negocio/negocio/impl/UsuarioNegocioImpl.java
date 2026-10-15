package com.manosalaobra.negocio.negocio.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.EstadoUsuarioDominio;
import com.manosalaobra.dominio.RolDominio;
import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.entidad.EstadoUsuarioEntidad;
import com.manosalaobra.entidad.RolEntidad;
import com.manosalaobra.negocio.negocio.UsuarioNegocio;
import com.manosalaobra.negocio.negocio.ensamblador.impl.EstadoUsuarioEnsambladorEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.impl.RolEnsambladorEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.impl.UsuarioEnsambladorEntidad;
import com.manosalaobra.negocio.negocio.reglas.impl.usuario.AsegurarCorreoNoRegistradoRegla;
import com.manosalaobra.negocio.negocio.reglas.impl.usuario.ValidarDatosRegistrarNuevoUsuarioRegla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.Claves;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.util.List;

public final class UsuarioNegocioImpl implements UsuarioNegocio {

    private final DAOFactory daoFactory;

    public UsuarioNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public UsuarioDominio registrarCiudadano(UsuarioDominio usuario) {
        ValidarDatosRegistrarNuevoUsuarioRegla.obtenerInstancia().ejecutar(usuario);
        AsegurarCorreoNoRegistradoRegla.obtenerInstancia().ejecutar(usuario.getCorreo(), daoFactory);

        UsuarioDominio ciudadanoNuevo = new UsuarioDominio.Builder()
                .id(UtilUUID.generar())
                .rol(consultarRolPorNombre(RolDominio.CIUDADANO))
                .estadoUsuario(consultarEstadoUsuarioPorNombre(EstadoUsuarioDominio.ACTIVO))
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .claveHash(Claves.cifrar(usuario.getClave()))
                .telefono(usuario.getTelefono())
                .build();

        daoFactory.obtenerUsuarioDAO().crear(UsuarioEnsambladorEntidad.getInstancia().convertirAEntidad(ciudadanoNuevo));
        return ciudadanoNuevo;
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
