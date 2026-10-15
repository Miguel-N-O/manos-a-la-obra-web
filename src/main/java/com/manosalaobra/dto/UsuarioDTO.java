package com.manosalaobra.dto;

import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.util.UUID;

public final class UsuarioDTO {

    private UUID id;
    private RolDTO rol;
    private EstadoUsuarioDTO estadoUsuario;
    private String nombre;
    private String correo;
    private String clave;
    private IndicativoPaisDTO indicativoPais;
    private String numeroTelefono;

    public UsuarioDTO() {
        setId(UtilUUID.obtenerUUIDDefecto());
        setRol(new RolDTO());
        setEstadoUsuario(new EstadoUsuarioDTO());
        setNombre(UtilTexto.VACIO);
        setCorreo(UtilTexto.VACIO);
        setClave(UtilTexto.VACIO);
        setIndicativoPais(new IndicativoPaisDTO());
        setNumeroTelefono(UtilTexto.VACIO);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilUUID.obtenerValorDefecto(id);
    }

    public RolDTO getRol() {
        return rol;
    }

    public void setRol(RolDTO rol) {
        this.rol = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(rol, new RolDTO());
    }

    public EstadoUsuarioDTO getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(EstadoUsuarioDTO estadoUsuario) {
        this.estadoUsuario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(estadoUsuario, new EstadoUsuarioDTO());
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = UtilTexto.getUtilTexto().obtenerValorDefecto(clave);
    }

    public IndicativoPaisDTO getIndicativoPais() {
        return indicativoPais;
    }

    public void setIndicativoPais(IndicativoPaisDTO indicativoPais) {
        this.indicativoPais = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(indicativoPais, new IndicativoPaisDTO());
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(numeroTelefono);
    }
}
