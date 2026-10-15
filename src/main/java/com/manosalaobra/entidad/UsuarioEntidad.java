package com.manosalaobra.entidad;

import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.util.UUID;

public final class UsuarioEntidad {

    private UUID id;
    private RolEntidad rol;
    private EstadoUsuarioEntidad estadoUsuario;
    private String nombre;
    private String correo;
    private String claveHash;
    private IndicativoPaisEntidad indicativoPais;
    private String numeroTelefono;

    public UsuarioEntidad() {
        setId(UtilUUID.obtenerUUIDDefecto());
        setRol(new RolEntidad());
        setEstadoUsuario(new EstadoUsuarioEntidad());
        setNombre(UtilTexto.VACIO);
        setCorreo(UtilTexto.VACIO);
        setClaveHash(UtilTexto.VACIO);
        setIndicativoPais(new IndicativoPaisEntidad());
        setNumeroTelefono(UtilTexto.VACIO);
    }

    public UsuarioEntidad(UUID id, RolEntidad rol, EstadoUsuarioEntidad estadoUsuario, String nombre, String correo, String claveHash, IndicativoPaisEntidad indicativoPais, String numeroTelefono) {
        setId(id);
        setRol(rol);
        setEstadoUsuario(estadoUsuario);
        setNombre(nombre);
        setCorreo(correo);
        setClaveHash(claveHash);
        setIndicativoPais(indicativoPais);
        setNumeroTelefono(numeroTelefono);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilUUID.obtenerValorDefecto(id);
    }

    public RolEntidad getRol() {
        return rol;
    }

    public void setRol(RolEntidad rol) {
        this.rol = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(rol, new RolEntidad());
    }

    public EstadoUsuarioEntidad getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(EstadoUsuarioEntidad estadoUsuario) {
        this.estadoUsuario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(estadoUsuario, new EstadoUsuarioEntidad());
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(nombre);
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = UtilTexto.getUtilTexto().normalizarCorreo(correo);
    }

    public String getClaveHash() {
        return claveHash;
    }

    public void setClaveHash(String claveHash) {
        this.claveHash = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(claveHash);
    }

    public IndicativoPaisEntidad getIndicativoPais() {
        return indicativoPais;
    }

    public void setIndicativoPais(IndicativoPaisEntidad indicativoPais) {
        this.indicativoPais = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(indicativoPais, new IndicativoPaisEntidad());
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(numeroTelefono);
    }
}
