package com.manosalaobra.negocio.negocio.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.EstadoUsuarioDominio;
import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.entidad.TecnicoEntidad;
import com.manosalaobra.negocio.negocio.TecnicoNegocio;
import com.manosalaobra.negocio.negocio.ensamblador.impl.TecnicoEnsambladorEntidad;
import com.manosalaobra.negocio.negocio.reglas.impl.barrio.AsegurarBarrioExisteRegla;
import com.manosalaobra.negocio.negocio.reglas.impl.servicio.AsegurarServicioExisteRegla;
import com.manosalaobra.negocio.negocio.reglas.impl.tecnico.ValidarDatosPerfilTecnicoRegla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public final class TecnicoNegocioImpl implements TecnicoNegocio {

    private static final Comparator<TecnicoDominio> POR_PROMEDIO_Y_NOMBRE = Comparator
            .comparing(TecnicoDominio::getPromedio, Comparator.reverseOrder())
            .thenComparing(tecnico -> tecnico.getUsuario().getNombre(), String.CASE_INSENSITIVE_ORDER);

    private final DAOFactory daoFactory;

    public TecnicoNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<TecnicoDominio> consultarPorFiltro(TecnicoDominio filtro) {
        TecnicoEntidad filtroEntidad = TecnicoEnsambladorEntidad.getInstancia().convertirAEntidad(filtro);
        filtroEntidad.getUsuario().getEstadoUsuario().setNombre(EstadoUsuarioDominio.ACTIVO);

        List<TecnicoDominio> tecnicos = new ArrayList<>();
        for (TecnicoEntidad entidad : daoFactory.obtenerTecnicoDAO().consultarPorFiltro(filtroEntidad)) {
            tecnicos.add(TecnicoEnsambladorEntidad.getInstancia().convertirADominio(entidad));
        }
        tecnicos.sort(POR_PROMEDIO_Y_NOMBRE);
        return tecnicos;
    }

    @Override
    public TecnicoDominio consultarPorId(UUID id) {
        if (UtilUUID.esValorDefecto(id)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_TECNICO_NO_ENCONTRADO);
        }
        TecnicoEntidad filtroEntidad = new TecnicoEntidad();
        filtroEntidad.setId(id);
        filtroEntidad.getUsuario().getEstadoUsuario().setNombre(EstadoUsuarioDominio.ACTIVO);

        List<TecnicoEntidad> encontrados = daoFactory.obtenerTecnicoDAO().consultarPorFiltro(filtroEntidad);
        if (encontrados.isEmpty()) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_TECNICO_NO_ENCONTRADO);
        }
        return TecnicoEnsambladorEntidad.getInstancia().convertirADominio(encontrados.get(0));
    }

    @Override
    public TecnicoDominio consultarPorUsuario(UUID usuarioId) {
        if (UtilUUID.esValorDefecto(usuarioId)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_REGISTRO_TECNICO_NO_ENCONTRADO);
        }
        TecnicoEntidad encontrado = daoFactory.obtenerTecnicoDAO().consultarPorUsuario(usuarioId);
        if (UtilUUID.esValorDefecto(encontrado.getId())) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_REGISTRO_TECNICO_NO_ENCONTRADO);
        }
        return TecnicoEnsambladorEntidad.getInstancia().convertirADominio(encontrado);
    }

    @Override
    public TecnicoDominio guardarPerfil(UUID usuarioId, TecnicoDominio perfil) {
        ValidarDatosPerfilTecnicoRegla.obtenerInstancia().ejecutar(perfil);
        AsegurarServicioExisteRegla.obtenerInstancia().ejecutar(perfil.getServicio().getId(), daoFactory);
        AsegurarBarrioExisteRegla.obtenerInstancia().ejecutar(perfil.getBarrio().getId(), daoFactory);

        // El técnico solo puede cambiar su propio registro: el id sale de la sesión, no de lo que envía
        TecnicoDominio actual = consultarPorUsuario(usuarioId);
        TecnicoDominio perfilNuevo = new TecnicoDominio.Builder()
                .id(actual.getId())
                .usuario(actual.getUsuario())
                .servicio(perfil.getServicio())
                .barrio(perfil.getBarrio())
                .tarifa(perfil.getTarifa())
                .tipoTarifa(perfil.getTipoTarifa())
                .experiencia(perfil.getExperiencia())
                .descripcion(perfil.getDescripcion())
                .build();
        daoFactory.obtenerTecnicoDAO().actualizar(actual.getId(),
                TecnicoEnsambladorEntidad.getInstancia().convertirAEntidad(perfilNuevo));
        return consultarPorUsuario(usuarioId);
    }
}
