package com.manosalaobra.negocio.negocio.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.EstadoSolicitudDominio;
import com.manosalaobra.dominio.SolicitudDominio;
import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.entidad.EstadoSolicitudEntidad;
import com.manosalaobra.entidad.SolicitudEntidad;
import com.manosalaobra.entidad.TecnicoEntidad;
import com.manosalaobra.negocio.negocio.SolicitudNegocio;
import com.manosalaobra.negocio.negocio.ensamblador.impl.EstadoSolicitudEnsambladorEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.impl.SolicitudEnsambladorEntidad;
import com.manosalaobra.negocio.negocio.reglas.impl.barrio.AsegurarBarrioExisteRegla;
import com.manosalaobra.negocio.negocio.reglas.impl.solicitud.ValidarDatosCrearSolicitudRegla;
import com.manosalaobra.negocio.negocio.reglas.impl.tecnico.AsegurarTecnicoDisponibleRegla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public final class SolicitudNegocioImpl implements SolicitudNegocio {

    private final DAOFactory daoFactory;

    public SolicitudNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public SolicitudDominio crear(UUID ciudadanoId, SolicitudDominio solicitud) {
        ValidarDatosCrearSolicitudRegla.obtenerInstancia().ejecutar(solicitud);
        AsegurarTecnicoDisponibleRegla.obtenerInstancia().ejecutar(solicitud.getTecnico().getId(), daoFactory);
        AsegurarBarrioExisteRegla.obtenerInstancia().ejecutar(solicitud.getBarrio().getId(), daoFactory);

        // El ciudadano sale de la sesión, nunca de lo que envía el navegador. HU-09 CA-02: queda PENDIENTE
        SolicitudDominio solicitudNueva = new SolicitudDominio.Builder()
                .id(UtilUUID.generar())
                .ciudadano(new UsuarioDominio.Builder().id(ciudadanoId).build())
                .tecnico(solicitud.getTecnico())
                .barrio(solicitud.getBarrio())
                .descripcion(solicitud.getDescripcion())
                .fechaHora(solicitud.getFechaHora())
                .estadoSolicitud(consultarEstadoPorNombre(EstadoSolicitudDominio.PENDIENTE))
                .build();
        daoFactory.obtenerSolicitudDAO().crear(
                SolicitudEnsambladorEntidad.getInstancia().convertirAEntidad(solicitudNueva));
        return consultarUnaDelCiudadano(ciudadanoId, solicitudNueva.getId());
    }

    @Override
    public List<SolicitudDominio> consultarDelCiudadano(UUID ciudadanoId) {
        SolicitudEntidad filtro = new SolicitudEntidad();
        filtro.getCiudadano().setId(ciudadanoId);
        return convertirADominio(consultarConFiltroSeguro(ciudadanoId, filtro));
    }

    // CU19: el ciudadano solo puede ver sus propias solicitudes
    @Override
    public SolicitudDominio consultarUnaDelCiudadano(UUID ciudadanoId, UUID solicitudId) {
        if (UtilUUID.esValorDefecto(solicitudId)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_SOLICITUD_NO_ENCONTRADA);
        }
        SolicitudEntidad filtro = new SolicitudEntidad();
        filtro.setId(solicitudId);
        filtro.getCiudadano().setId(ciudadanoId);
        return obtenerUnica(consultarConFiltroSeguro(ciudadanoId, filtro));
    }

    // HU-10 CA-02 y CA-03: solo las dirigidas al técnico, con las pendientes primero (CU18)
    @Override
    public List<SolicitudDominio> consultarRecibidas(UUID usuarioTecnicoId) {
        SolicitudEntidad filtro = new SolicitudEntidad();
        filtro.getTecnico().setId(consultarIdTecnico(usuarioTecnicoId));
        List<SolicitudDominio> solicitudes = convertirADominio(
                daoFactory.obtenerSolicitudDAO().consultarPorFiltro(filtro));
        // El DAO las trae de la más reciente a la más antigua y el ordenamiento estable conserva ese orden
        solicitudes.sort(Comparator.comparing(solicitud -> !esPendiente(solicitud)));
        return solicitudes;
    }

    @Override
    public SolicitudDominio consultarUnaRecibida(UUID usuarioTecnicoId, UUID solicitudId) {
        if (UtilUUID.esValorDefecto(solicitudId)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_SOLICITUD_NO_ENCONTRADA);
        }
        SolicitudEntidad filtro = new SolicitudEntidad();
        filtro.setId(solicitudId);
        filtro.getTecnico().setId(consultarIdTecnico(usuarioTecnicoId));
        return obtenerUnica(daoFactory.obtenerSolicitudDAO().consultarPorFiltro(filtro));
    }

    // CU10: al abrir "Solicitudes recibidas" el aviso de solicitudes nuevas deja de mostrarse
    @Override
    public void marcarRecibidasComoNotificadas(UUID usuarioTecnicoId) {
        daoFactory.obtenerSolicitudDAO().marcarComoNotificadas(consultarIdTecnico(usuarioTecnicoId));
    }

    // La sesión guarda el usuario; las solicitudes apuntan al registro del técnico
    private UUID consultarIdTecnico(UUID usuarioTecnicoId) {
        if (UtilUUID.esValorDefecto(usuarioTecnicoId)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_REGISTRO_TECNICO_NO_ENCONTRADO);
        }
        TecnicoEntidad tecnico = daoFactory.obtenerTecnicoDAO().consultarPorUsuario(usuarioTecnicoId);
        if (UtilUUID.esValorDefecto(tecnico.getId())) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_REGISTRO_TECNICO_NO_ENCONTRADO);
        }
        return tecnico.getId();
    }

    private boolean esPendiente(SolicitudDominio solicitud) {
        return EstadoSolicitudDominio.PENDIENTE.equals(solicitud.getEstadoSolicitud().getNombre());
    }

    private SolicitudDominio obtenerUnica(List<SolicitudEntidad> encontradas) {
        if (encontradas.isEmpty()) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_SOLICITUD_NO_ENCONTRADA);
        }
        return SolicitudEnsambladorEntidad.getInstancia().convertirADominio(encontradas.get(0));
    }

    // Sin ciudadano en el filtro el DAO devolvería las solicitudes de todos
    private List<SolicitudEntidad> consultarConFiltroSeguro(UUID ciudadanoId, SolicitudEntidad filtro) {
        if (UtilUUID.esValorDefecto(ciudadanoId)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_SOLICITUD_NO_ENCONTRADA);
        }
        return daoFactory.obtenerSolicitudDAO().consultarPorFiltro(filtro);
    }

    private List<SolicitudDominio> convertirADominio(List<SolicitudEntidad> entidades) {
        List<SolicitudDominio> solicitudes = new ArrayList<>();
        for (SolicitudEntidad entidad : entidades) {
            solicitudes.add(SolicitudEnsambladorEntidad.getInstancia().convertirADominio(entidad));
        }
        return solicitudes;
    }

    private EstadoSolicitudDominio consultarEstadoPorNombre(String nombre) {
        EstadoSolicitudEntidad filtro = new EstadoSolicitudEntidad();
        filtro.setNombre(nombre);
        List<EstadoSolicitudEntidad> estados = daoFactory.obtenerEstadoSolicitudDAO().consultarPorFiltro(filtro);
        if (estados.isEmpty()) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_ESTADO_NO_CONFIGURADO);
        }
        return EstadoSolicitudEnsambladorEntidad.getInstancia().convertirADominio(estados.get(0));
    }
}
