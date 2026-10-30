package com.manosalaobra.dominio;

import com.manosalaobra.transversal.utilitarios.UtilFecha;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.time.LocalDateTime;
import java.util.UUID;

public final class SolicitudDominio {
    private final UUID id;
    private final UsuarioDominio ciudadano;
    private final TecnicoDominio tecnico;
    private final BarrioDominio barrio;
    private final String descripcion;
    private final LocalDateTime fechaHora;
    private final EstadoSolicitudDominio estadoSolicitud;
    private final boolean notificada;
    private final String motivoRechazo;
    private final LocalDateTime respondidaEn;
    private final LocalDateTime creadaEn;

    private SolicitudDominio(Builder builder) {
        this.id = builder.id;
        this.ciudadano = builder.ciudadano;
        this.tecnico = builder.tecnico;
        this.barrio = builder.barrio;
        this.descripcion = builder.descripcion;
        this.fechaHora = builder.fechaHora;
        this.estadoSolicitud = builder.estadoSolicitud;
        this.notificada = builder.notificada;
        this.motivoRechazo = builder.motivoRechazo;
        this.respondidaEn = builder.respondidaEn;
        this.creadaEn = builder.creadaEn;
    }

    public UUID getId() {
        return id;
    }

    public UsuarioDominio getCiudadano() {
        return ciudadano;
    }

    public TecnicoDominio getTecnico() {
        return tecnico;
    }

    public BarrioDominio getBarrio() {
        return barrio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public EstadoSolicitudDominio getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public boolean isNotificada() {
        return notificada;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public LocalDateTime getRespondidaEn() {
        return respondidaEn;
    }

    public LocalDateTime getCreadaEn() {
        return creadaEn;
    }

    public static class Builder {
        private UUID id;
        private UsuarioDominio ciudadano;
        private TecnicoDominio tecnico;
        private BarrioDominio barrio;
        private String descripcion;
        private LocalDateTime fechaHora;
        private EstadoSolicitudDominio estadoSolicitud;
        private boolean notificada;
        private String motivoRechazo;
        private LocalDateTime respondidaEn;
        private LocalDateTime creadaEn;

        public Builder() {
            id = UtilUUID.obtenerUUIDDefecto();
            ciudadano = new UsuarioDominio.Builder().build();
            tecnico = new TecnicoDominio.Builder().build();
            barrio = new BarrioDominio.Builder().build();
            descripcion = UtilTexto.VACIO;
            fechaHora = UtilFecha.obtenerFechaHoraDefecto();
            estadoSolicitud = new EstadoSolicitudDominio.Builder().build();
            notificada = false;
            motivoRechazo = UtilTexto.VACIO;
            respondidaEn = UtilFecha.obtenerFechaHoraDefecto();
            creadaEn = UtilFecha.obtenerFechaHoraDefecto();
        }

        public Builder id(UUID id) {
            this.id = UtilUUID.obtenerValorDefecto(id);
            return this;
        }

        public Builder ciudadano(UsuarioDominio ciudadano) {
            this.ciudadano = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(ciudadano, new UsuarioDominio.Builder().build());
            return this;
        }

        public Builder tecnico(TecnicoDominio tecnico) {
            this.tecnico = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(tecnico, new TecnicoDominio.Builder().build());
            return this;
        }

        public Builder barrio(BarrioDominio barrio) {
            this.barrio = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(barrio, new BarrioDominio.Builder().build());
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(descripcion);
            return this;
        }

        public Builder fechaHora(LocalDateTime fechaHora) {
            this.fechaHora = UtilFecha.obtenerValorDefecto(fechaHora);
            return this;
        }

        public Builder estadoSolicitud(EstadoSolicitudDominio estadoSolicitud) {
            this.estadoSolicitud = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(estadoSolicitud, new EstadoSolicitudDominio.Builder().build());
            return this;
        }

        public Builder notificada(boolean notificada) {
            this.notificada = notificada;
            return this;
        }

        public Builder motivoRechazo(String motivoRechazo) {
            this.motivoRechazo = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(motivoRechazo);
            return this;
        }

        public Builder respondidaEn(LocalDateTime respondidaEn) {
            this.respondidaEn = UtilFecha.obtenerValorDefecto(respondidaEn);
            return this;
        }

        public Builder creadaEn(LocalDateTime creadaEn) {
            this.creadaEn = UtilFecha.obtenerValorDefecto(creadaEn);
            return this;
        }

        public SolicitudDominio build() {
            return new SolicitudDominio(this);
        }
    }
}
