package com.manosalaobra.dominio;

import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.util.UUID;

public final class IndicativoPaisDominio {

    private final UUID id;
    private final String pais;
    private final String indicativo;
    private final boolean activo;

    private IndicativoPaisDominio(Builder builder) {
        this.id = builder.id;
        this.pais = builder.pais;
        this.indicativo = builder.indicativo;
        this.activo = builder.activo;
    }

    public UUID getId() {
        return id;
    }

    public String getPais() {
        return pais;
    }

    public String getIndicativo() {
        return indicativo;
    }

    public boolean isActivo() {
        return activo;
    }

    public static class Builder {
        private UUID id;
        private String pais;
        private String indicativo;
        private boolean activo;

        public Builder() {
            id = UtilUUID.obtenerUUIDDefecto();
            pais = UtilTexto.VACIO;
            indicativo = UtilTexto.VACIO;
            activo = true;
        }

        public Builder id(UUID id) {
            this.id = UtilUUID.obtenerValorDefecto(id);
            return this;
        }

        public Builder pais(String pais) {
            this.pais = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(pais);
            return this;
        }

        public Builder indicativo(String indicativo) {
            this.indicativo = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(indicativo);
            return this;
        }

        public Builder activo(boolean activo) {
            this.activo = activo;
            return this;
        }

        public IndicativoPaisDominio build() {
            return new IndicativoPaisDominio(this);
        }
    }
}
