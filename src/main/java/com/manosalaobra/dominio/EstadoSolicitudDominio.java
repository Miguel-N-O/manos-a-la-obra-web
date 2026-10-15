package com.manosalaobra.dominio;

import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.util.UUID;

public final class EstadoSolicitudDominio {
    public static final String PENDIENTE = "PENDIENTE";
    public static final String ACEPTADA = "ACEPTADA";
    public static final String RECHAZADA = "RECHAZADA";
    public static final String FINALIZADA = "FINALIZADA";

    private final UUID id;
    private final String nombre;
    private final boolean activo;

    private EstadoSolicitudDominio(Builder builder) {
        this.id = builder.id;
        this.nombre = builder.nombre;
        this.activo = builder.activo;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public static class Builder {
        private UUID id;
        private String nombre;
        private boolean activo;

        public Builder() {
            id = UtilUUID.obtenerUUIDDefecto();
            nombre = UtilTexto.VACIO;
            activo = true;
        }

        public Builder id(UUID id) {
            this.id = UtilUUID.obtenerValorDefecto(id);
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(nombre);
            return this;
        }

        public Builder activo(boolean activo) {
            this.activo = activo;
            return this;
        }

        public EstadoSolicitudDominio build() {
            return new EstadoSolicitudDominio(this);
        }
    }
}
