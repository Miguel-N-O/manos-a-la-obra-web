package com.manosalaobra.entidad;

import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.util.UUID;

public final class EstadoSolicitudEntidad {

    private UUID id;
    private String nombre;
    private boolean activo;

    public EstadoSolicitudEntidad() {
        setId(UtilUUID.obtenerUUIDDefecto());
        setNombre(UtilTexto.VACIO);
        setActivo(true);
    }

    public EstadoSolicitudEntidad(UUID id, String nombre, boolean activo) {
        setId(id);
        setNombre(nombre);
        setActivo(activo);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilUUID.obtenerValorDefecto(id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(nombre);
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
