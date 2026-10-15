package com.manosalaobra.dto;

import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.util.UUID;

public final class BarrioDTO {

    private UUID id;
    private CiudadDTO ciudad;
    private String nombre;
    private boolean activo;

    public BarrioDTO() {
        setId(UtilUUID.obtenerUUIDDefecto());
        setCiudad(new CiudadDTO());
        setNombre(UtilTexto.VACIO);
        setActivo(true);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilUUID.obtenerValorDefecto(id);
    }

    public CiudadDTO getCiudad() {
        return ciudad;
    }

    public void setCiudad(CiudadDTO ciudad) {
        this.ciudad = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(ciudad, new CiudadDTO());
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
