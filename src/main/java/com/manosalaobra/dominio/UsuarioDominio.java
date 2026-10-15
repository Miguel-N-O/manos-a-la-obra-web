package com.manosalaobra.dominio;

import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.util.UUID;

public final class UsuarioDominio {
    private final UUID id;
    private final RolDominio rol;
    private final EstadoUsuarioDominio estadoUsuario;
    private final String nombre;
    private final String correo;
    private final String clave;
    private final String claveHash;
    private final IndicativoPaisDominio indicativoPais;
    private final String numeroTelefono;

    private UsuarioDominio(Builder builder) {
        this.id = builder.id;
        this.rol = builder.rol;
        this.estadoUsuario = builder.estadoUsuario;
        this.nombre = builder.nombre;
        this.correo = builder.correo;
        this.clave = builder.clave;
        this.claveHash = builder.claveHash;
        this.indicativoPais = builder.indicativoPais;
        this.numeroTelefono = builder.numeroTelefono;
    }

    public UUID getId() {
        return id;
    }

    public RolDominio getRol() {
        return rol;
    }

    public EstadoUsuarioDominio getEstadoUsuario() {
        return estadoUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getClave() {
        return clave;
    }

    public String getClaveHash() {
        return claveHash;
    }

    public IndicativoPaisDominio getIndicativoPais() {
        return indicativoPais;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public static class Builder {
        private UUID id;
        private RolDominio rol;
        private EstadoUsuarioDominio estadoUsuario;
        private String nombre;
        private String correo;
        private String clave;
        private String claveHash;
        private IndicativoPaisDominio indicativoPais;
        private String numeroTelefono;

        public Builder() {
            id = UtilUUID.obtenerUUIDDefecto();
            rol = new RolDominio.Builder().build();
            estadoUsuario = new EstadoUsuarioDominio.Builder().build();
            nombre = UtilTexto.VACIO;
            correo = UtilTexto.VACIO;
            clave = UtilTexto.VACIO;
            claveHash = UtilTexto.VACIO;
            indicativoPais = new IndicativoPaisDominio.Builder().build();
            numeroTelefono = UtilTexto.VACIO;
        }

        public Builder id(UUID id) {
            this.id = UtilUUID.obtenerValorDefecto(id);
            return this;
        }

        public Builder rol(RolDominio rol) {
            this.rol = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(rol, new RolDominio.Builder().build());
            return this;
        }

        public Builder estadoUsuario(EstadoUsuarioDominio estadoUsuario) {
            this.estadoUsuario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(estadoUsuario, new EstadoUsuarioDominio.Builder().build());
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(nombre);
            return this;
        }

        public Builder correo(String correo) {
            this.correo = UtilTexto.getUtilTexto().normalizarCorreo(correo);
            return this;
        }

        public Builder clave(String clave) {
            this.clave = UtilTexto.getUtilTexto().obtenerValorDefecto(clave);
            return this;
        }

        public Builder claveHash(String claveHash) {
            this.claveHash = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(claveHash);
            return this;
        }

        public Builder indicativoPais(IndicativoPaisDominio indicativoPais) {
            this.indicativoPais = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                    indicativoPais, new IndicativoPaisDominio.Builder().build());
            return this;
        }

        public Builder numeroTelefono(String numeroTelefono) {
            this.numeroTelefono = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(numeroTelefono);
            return this;
        }

        public UsuarioDominio build() {
            return new UsuarioDominio(this);
        }
    }
}
