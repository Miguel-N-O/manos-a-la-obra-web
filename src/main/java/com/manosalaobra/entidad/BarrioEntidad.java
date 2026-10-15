package com.manosalaobra.entidad;

import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.util.UUID;

public final class BarrioEntidad {

    private UUID id;
    private CiudadEntidad ciudad;
    private String nombre;
    private boolean activo;

    public BarrioEntidad() {
        setId(UtilUUID.obtenerUUIDDefecto());
        setCiudad(new CiudadEntidad());
        setNombre(UtilTexto.VACIO);
        setActivo(true);
    }

    public BarrioEntidad(UUID id, CiudadEntidad ciudad, String nombre, boolean activo) {
        setId(id);
        setCiudad(ciudad);
        setNombre(nombre);
        setActivo(activo);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilUUID.obtenerValorDefecto(id);
    }

    public CiudadEntidad getCiudad() {
        return ciudad;
    }

    public void setCiudad(CiudadEntidad ciudad) {
        this.ciudad = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(ciudad, new CiudadEntidad());
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
