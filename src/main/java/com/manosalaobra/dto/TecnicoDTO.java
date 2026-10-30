package com.manosalaobra.dto;

import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.math.BigDecimal;
import java.util.UUID;

public final class TecnicoDTO {

    private UUID id;
    private UsuarioDTO usuario;
    private ServicioDTO servicio;
    private BarrioDTO barrio;
    private BigDecimal tarifa;
    private int experiencia;
    private String tipoDocumento;
    private String documento;
    private BigDecimal promedio;

    public TecnicoDTO() {
        setId(UtilUUID.obtenerUUIDDefecto());
        setUsuario(new UsuarioDTO());
        setServicio(new ServicioDTO());
        setBarrio(new BarrioDTO());
        setTarifa(BigDecimal.ZERO);
        setExperiencia(0);
        setTipoDocumento(UtilTexto.VACIO);
        setDocumento(UtilTexto.VACIO);
        setPromedio(BigDecimal.ZERO);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilUUID.obtenerValorDefecto(id);
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(usuario, new UsuarioDTO());
    }

    public ServicioDTO getServicio() {
        return servicio;
    }

    public void setServicio(ServicioDTO servicio) {
        this.servicio = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(servicio, new ServicioDTO());
    }

    public BarrioDTO getBarrio() {
        return barrio;
    }

    public void setBarrio(BarrioDTO barrio) {
        this.barrio = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(barrio, new BarrioDTO());
    }

    public BigDecimal getTarifa() {
        return tarifa;
    }

    public void setTarifa(BigDecimal tarifa) {
        this.tarifa = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(tarifa, BigDecimal.ZERO);
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(tipoDocumento);
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(documento);
    }

    public BigDecimal getPromedio() {
        return promedio;
    }

    public void setPromedio(BigDecimal promedio) {
        this.promedio = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(promedio, BigDecimal.ZERO);
    }
}
