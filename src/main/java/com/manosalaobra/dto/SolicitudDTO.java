package com.manosalaobra.dto;

import com.manosalaobra.transversal.utilitarios.UtilFecha;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.time.LocalDateTime;
import java.util.UUID;

public final class SolicitudDTO {

    private UUID id;
    private UsuarioDTO ciudadano;
    private TecnicoDTO tecnico;
    private BarrioDTO barrio;
    private String descripcion;
    private LocalDateTime fechaHora;
    private EstadoSolicitudDTO estadoSolicitud;
    private boolean notificada;
    private String motivoRechazo;
    private LocalDateTime respondidaEn;
    private LocalDateTime creadaEn;

    public SolicitudDTO() {
        setId(UtilUUID.obtenerUUIDDefecto());
        setCiudadano(new UsuarioDTO());
        setTecnico(new TecnicoDTO());
        setBarrio(new BarrioDTO());
        setDescripcion(UtilTexto.VACIO);
        setFechaHora(UtilFecha.obtenerFechaHoraDefecto());
        setEstadoSolicitud(new EstadoSolicitudDTO());
        setNotificada(false);
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

    public UsuarioDTO getCiudadano() {
        return ciudadano;
    }

    public void setCiudadano(UsuarioDTO ciudadano) {
        this.ciudadano = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(ciudadano, new UsuarioDTO());
    }

    public TecnicoDTO getTecnico() {
        return tecnico;
    }

    public void setTecnico(TecnicoDTO tecnico) {
        this.tecnico = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(tecnico, new TecnicoDTO());
    }

    public BarrioDTO getBarrio() {
        return barrio;
    }

    public void setBarrio(BarrioDTO barrio) {
        this.barrio = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(barrio, new BarrioDTO());
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

    public EstadoSolicitudDTO getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(EstadoSolicitudDTO estadoSolicitud) {
        this.estadoSolicitud = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(estadoSolicitud, new EstadoSolicitudDTO());
    }

    public boolean isNotificada() {
        return notificada;
    }

    public void setNotificada(boolean notificada) {
        this.notificada = notificada;
    }
}
