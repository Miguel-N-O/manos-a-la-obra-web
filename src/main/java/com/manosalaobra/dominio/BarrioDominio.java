package com.manosalaobra.dominio;

import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.util.UUID;

public final class BarrioDominio {
    private final UUID id;
    private final CiudadDominio ciudad;
    private final String nombre;
    private final boolean activo;

    private BarrioDominio(Builder builder) {
        this.id = builder.id;
        this.ciudad = builder.ciudad;
        this.nombre = builder.nombre;
        this.activo = builder.activo;
    }

    public UUID getId() {
        return id;
    }

    public CiudadDominio getCiudad() {
        return ciudad;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public static class Builder {
        private UUID id;
        private CiudadDominio ciudad;
        private String nombre;
        private boolean activo;

        public Builder() {
            id = UtilUUID.obtenerUUIDDefecto();
            ciudad = new CiudadDominio.Builder().build();
            nombre = UtilTexto.VACIO;
            activo = true;
        }

        public Builder id(UUID id) {
            this.id = UtilUUID.obtenerValorDefecto(id);
            return this;
        }

        public Builder ciudad(CiudadDominio ciudad) {
            this.ciudad = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(ciudad, new CiudadDominio.Builder().build());
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

        public BarrioDominio build() {
            return new BarrioDominio(this);
        }
    }
}
