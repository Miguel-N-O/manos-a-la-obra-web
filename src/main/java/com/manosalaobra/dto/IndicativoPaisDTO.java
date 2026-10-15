package com.manosalaobra.dto;

import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.util.UUID;

public final class IndicativoPaisDTO {

    private UUID id;
    private String pais;
    private String indicativo;
    private boolean activo;

    public IndicativoPaisDTO() {
        setId(UtilUUID.obtenerUUIDDefecto());
        setPais(UtilTexto.VACIO);
        setIndicativo(UtilTexto.VACIO);
        setActivo(true);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilUUID.obtenerValorDefecto(id);
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(pais);
    }

    public String getIndicativo() {
        return indicativo;
    }

    public void setIndicativo(String indicativo) {
        this.indicativo = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(indicativo);
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
