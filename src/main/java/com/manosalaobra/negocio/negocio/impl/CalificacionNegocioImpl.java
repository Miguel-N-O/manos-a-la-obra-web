package com.manosalaobra.negocio.negocio.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.CalificacionDominio;
import com.manosalaobra.dominio.SolicitudDominio;
import com.manosalaobra.entidad.CalificacionEntidad;
import com.manosalaobra.entidad.SolicitudEntidad;
import com.manosalaobra.negocio.negocio.CalificacionNegocio;
import com.manosalaobra.negocio.negocio.ensamblador.impl.CalificacionEnsambladorEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.impl.SolicitudEnsambladorEntidad;
import com.manosalaobra.negocio.negocio.reglas.impl.calificacion.AsegurarSolicitudFinalizadaRegla;
import com.manosalaobra.negocio.negocio.reglas.impl.calificacion.AsegurarSolicitudSinCalificacionRegla;
import com.manosalaobra.negocio.negocio.reglas.impl.calificacion.ValidarDatosCalificarRegla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class CalificacionNegocioImpl implements CalificacionNegocio {

    // HU-17: motivos con los que el administrador puede ocultar un comentario
    private static final Set<String> MOTIVOS_MODERACION = Set.of(
            "LENGUAJE_OFENSIVO", "INFORMACION_PERSONAL", "CONTENIDO_NO_RELACIONADO", "OTRO");

    private final DAOFactory daoFactory;

    public CalificacionNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // HU-14 y HU-15: el ciudadano califica una solicitud suya ya finalizada, con comentario opcional,
    // y el promedio del técnico se actualiza en la misma operación
    @Override
    public CalificacionDominio calificar(UUID ciudadanoId, CalificacionDominio calificacion) {
        ValidarDatosCalificarRegla.obtenerInstancia().ejecutar(calificacion);
        UUID solicitudId = calificacion.getSolicitud().getId();
        SolicitudDominio solicitud = consultarSolicitudDelCiudadano(ciudadanoId, solicitudId);
        AsegurarSolicitudFinalizadaRegla.obtenerInstancia().ejecutar(solicitud);
        AsegurarSolicitudSinCalificacionRegla.obtenerInstancia().ejecutar(solicitudId, daoFactory);

        CalificacionDominio calificacionNueva = new CalificacionDominio.Builder()
                .id(UtilUUID.generar())
                .solicitud(solicitud)
                .estrellas(calificacion.getEstrellas())
                .comentario(calificacion.getComentario())
                .visible(true)
                .build();
        daoFactory.obtenerCalificacionDAO().crear(
                CalificacionEnsambladorEntidad.getInstancia().convertirAEntidad(calificacionNueva));
        daoFactory.obtenerCalificacionDAO().actualizarPromedioDelTecnico(solicitud.getTecnico().getId());
        return consultarPorId(calificacionNueva.getId());
    }

    // Calificaciones de un técnico para su perfil: un comentario oculto por moderación no se muestra,
    // pero sus estrellas siguen contando
    @Override
    public List<CalificacionDominio> consultarDelTecnico(UUID tecnicoId) {
        if (UtilUUID.esValorDefecto(tecnicoId)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.CalificacionNegocioImpl.USUARIO_ERROR_TECNICO_NO_INDICADO);
        }
        CalificacionEntidad filtro = new CalificacionEntidad();
        filtro.getSolicitud().getTecnico().setId(tecnicoId);
        List<CalificacionDominio> calificaciones = new ArrayList<>();
        for (CalificacionEntidad entidad : daoFactory.obtenerCalificacionDAO().consultarPorFiltro(filtro)) {
            calificaciones.add(sinComentarioSiEstaOculto(
                    CalificacionEnsambladorEntidad.getInstancia().convertirADominio(entidad)));
        }
        return calificaciones;
    }

    // HU-17: el administrador ve todas, también las que tienen el comentario oculto
    @Override
    public List<CalificacionDominio> consultarTodasParaModeracion() {
        List<CalificacionDominio> calificaciones = new ArrayList<>();
        for (CalificacionEntidad entidad : daoFactory.obtenerCalificacionDAO().consultarTodos()) {
            calificaciones.add(CalificacionEnsambladorEntidad.getInstancia().convertirADominio(entidad));
        }
        return calificaciones;
    }

    @Override
    public CalificacionDominio ocultarComentario(UUID calificacionId, String motivo) {
        String motivoLimpio = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(motivo);
        if (!MOTIVOS_MODERACION.contains(motivoLimpio)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.CalificacionNegocioImpl.USUARIO_ERROR_MOTIVO_MODERACION_NO_VALIDO);
        }
        return cambiarVisibilidad(calificacionId, false, motivoLimpio);
    }

    @Override
    public CalificacionDominio mostrarComentario(UUID calificacionId) {
        return cambiarVisibilidad(calificacionId, true, UtilTexto.VACIO);
    }

    // Al volver a mostrar un comentario el motivo de moderación se borra
    private CalificacionDominio cambiarVisibilidad(UUID calificacionId, boolean visible, String motivo) {
        CalificacionDominio existente = consultarPorId(calificacionId);
        CalificacionEntidad cambio = CalificacionEnsambladorEntidad.getInstancia().convertirAEntidad(existente);
        cambio.setVisible(visible);
        cambio.setMotivoModeracion(motivo);
        daoFactory.obtenerCalificacionDAO().actualizar(calificacionId, cambio);
        return consultarPorId(calificacionId);
    }

    // Sin esta guarda el DAO traería la primera calificación de cualquiera
    private CalificacionDominio consultarPorId(UUID calificacionId) {
        if (UtilUUID.esValorDefecto(calificacionId)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.CalificacionNegocioImpl.USUARIO_ERROR_CALIFICACION_NO_ENCONTRADA);
        }
        CalificacionEntidad encontrada = daoFactory.obtenerCalificacionDAO().consultarPorId(calificacionId);
        if (UtilUUID.esValorDefecto(encontrada.getId())) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.CalificacionNegocioImpl.USUARIO_ERROR_CALIFICACION_NO_ENCONTRADA);
        }
        return CalificacionEnsambladorEntidad.getInstancia().convertirADominio(encontrada);
    }

    // El ciudadano solo puede calificar solicitudes que son suyas
    private SolicitudDominio consultarSolicitudDelCiudadano(UUID ciudadanoId, UUID solicitudId) {
        if (UtilUUID.esValorDefecto(ciudadanoId)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.CalificacionNegocioImpl.USUARIO_ERROR_SOLICITUD_NO_ENCONTRADA);
        }
        SolicitudEntidad filtro = new SolicitudEntidad();
        filtro.setId(solicitudId);
        filtro.getCiudadano().setId(ciudadanoId);
        List<SolicitudEntidad> encontradas = daoFactory.obtenerSolicitudDAO().consultarPorFiltro(filtro);
        if (encontradas.isEmpty()) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.CalificacionNegocioImpl.USUARIO_ERROR_SOLICITUD_NO_ENCONTRADA);
        }
        return SolicitudEnsambladorEntidad.getInstancia().convertirADominio(encontradas.get(0));
    }

    private CalificacionDominio sinComentarioSiEstaOculto(CalificacionDominio calificacion) {
        if (calificacion.isVisible()) {
            return calificacion;
        }
        return new CalificacionDominio.Builder()
                .id(calificacion.getId())
                .solicitud(calificacion.getSolicitud())
                .estrellas(calificacion.getEstrellas())
                .comentario(UtilTexto.VACIO)
                .visible(false)
                .build();
    }
}
