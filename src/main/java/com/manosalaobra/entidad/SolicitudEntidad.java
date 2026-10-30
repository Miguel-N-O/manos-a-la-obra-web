package com.manosalaobra.entidad;

import com.manosalaobra.transversal.utilitarios.UtilFecha;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.time.LocalDateTime;
import java.util.UUID;

public final class SolicitudEntidad {

    private UUID id;
    private UsuarioEntidad ciudadano;
    private TecnicoEntidad tecnico;
    private BarrioEntidad barrio;
    private String descripcion;
    private LocalDateTime fechaHora;
    private EstadoSolicitudEntidad estadoSolicitud;
    private boolean notificada;
    private String motivoRechazo;
    private LocalDateTime respondidaEn;
    private LocalDateTime creadaEn;

    public SolicitudEntidad() {
        setId(UtilUUID.obtenerUUIDDefecto());
        setCiudadano(new UsuarioEntidad());
        setTecnico(new TecnicoEntidad());
        setBarrio(new BarrioEntidad());
        setDescripcion(UtilTexto.VACIO);
        setFechaHora(UtilFecha.obtenerFechaHoraDefecto());
        setEstadoSolicitud(new EstadoSolicitudEntidad());
        setNotificada(false);
        setMotivoRechazo(UtilTexto.VACIO);
        setRespondidaEn(UtilFecha.obtenerFechaHoraDefecto());
        setCreadaEn(UtilFecha.obtenerFechaHoraDefecto());
    }

    public SolicitudEntidad(UUID id, UsuarioEntidad ciudadano, TecnicoEntidad tecnico, String descripcion, LocalDateTime fechaHora, EstadoSolicitudEntidad estadoSolicitud, boolean notificada) {
        setId(id);
        setCiudadano(ciudadano);
        setTecnico(tecnico);
        setDescripcion(descripcion);
        setFechaHora(fechaHora);
        setEstadoSolicitud(estadoSolicitud);
        setNotificada(notificada);
        setBarrio(new BarrioEntidad());
        setMotivoRechazo(UtilTexto.VACIO);
        setRespondidaEn(UtilFecha.obtenerFechaHoraDefecto());
        setCreadaEn(UtilFecha.obtenerFechaHoraDefecto());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilUUID.obtenerValorDefecto(id);
    }

    public UsuarioEntidad getCiudadano() {
        return ciudadano;
    }

    public void setCiudadano(UsuarioEntidad ciudadano) {
        this.ciudadano = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(ciudadano, new UsuarioEntidad());
    }

    public TecnicoEntidad getTecnico() {
        return tecnico;
    }

    public void setTecnico(TecnicoEntidad tecnico) {
        this.tecnico = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(tecnico, new TecnicoEntidad());
    }

    public BarrioEntidad getBarrio() {
        return barrio;
    }

    public void setBarrio(BarrioEntidad barrio) {
        this.barrio = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(barrio, new BarrioEntidad());
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(motivoRechazo);
    }

    public LocalDateTime getRespondidaEn() {
        return respondidaEn;
    }

    public void setRespondidaEn(LocalDateTime respondidaEn) {
        this.respondidaEn = UtilFecha.obtenerValorDefecto(respondidaEn);
    }

    public LocalDateTime getCreadaEn() {
        return creadaEn;
    }

    public void setCreadaEn(LocalDateTime creadaEn) {
        this.creadaEn = UtilFecha.obtenerValorDefecto(creadaEn);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(descripcion);
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = UtilFecha.obtenerValorDefecto(fechaHora);
    }

    public EstadoSolicitudEntidad getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(EstadoSolicitudEntidad estadoSolicitud) {
        this.estadoSolicitud = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(estadoSolicitud, new EstadoSolicitudEntidad());
    }

    public boolean isNotificada() {
        return notificada;
    }

    public void setNotificada(boolean notificada) {
        this.notificada = notificada;
    }
}
