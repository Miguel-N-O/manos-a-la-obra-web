package com.manosalaobra.dominio;

import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.util.UUID;

public final class CalificacionDominio {
    private final UUID id;
    private final SolicitudDominio solicitud;
    private final int estrellas;
    private final String comentario;
    private final boolean visible;

    private CalificacionDominio(Builder builder) {
        this.id = builder.id;
        this.solicitud = builder.solicitud;
        this.estrellas = builder.estrellas;
        this.comentario = builder.comentario;
        this.visible = builder.visible;
    }

    public UUID getId() {
        return id;
    }

    public SolicitudDominio getSolicitud() {
        return solicitud;
    }

    public int getEstrellas() {
        return estrellas;
    }

    public String getComentario() {
        return comentario;
    }

    public boolean isVisible() {
        return visible;
    }

    public static class Builder {
        private UUID id;
        private SolicitudDominio solicitud;
        private int estrellas;
        private String comentario;
        private boolean visible;

        public Builder() {
            id = UtilUUID.obtenerUUIDDefecto();
            solicitud = new SolicitudDominio.Builder().build();
            estrellas = 0;
            comentario = UtilTexto.VACIO;
            visible = true;
        }

        public Builder id(UUID id) {
            this.id = UtilUUID.obtenerValorDefecto(id);
            return this;
        }

        public Builder solicitud(SolicitudDominio solicitud) {
            this.solicitud = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(solicitud, new SolicitudDominio.Builder().build());
            return this;
        }

        public Builder estrellas(int estrellas) {
            this.estrellas = estrellas;
            return this;
        }

        public Builder comentario(String comentario) {
            this.comentario = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(comentario);
            return this;
        }

        public Builder visible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public CalificacionDominio build() {
            return new CalificacionDominio(this);
        }
    }
}
