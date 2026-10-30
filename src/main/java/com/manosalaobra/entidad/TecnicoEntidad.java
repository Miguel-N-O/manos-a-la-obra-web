package com.manosalaobra.entidad;

import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.math.BigDecimal;
import java.util.UUID;

public final class TecnicoEntidad {

    private UUID id;
    private UsuarioEntidad usuario;
    private ServicioEntidad servicio;
    private BarrioEntidad barrio;
    private BigDecimal tarifa;
    private String tipoTarifa;
    private int experiencia;
    private String descripcion;
    private String tipoDocumento;
    private String documento;
    private BigDecimal promedio;

    public TecnicoEntidad() {
        setId(UtilUUID.obtenerUUIDDefecto());
        setUsuario(new UsuarioEntidad());
        setServicio(new ServicioEntidad());
        setBarrio(new BarrioEntidad());
        setTarifa(BigDecimal.ZERO);
        setTipoTarifa(UtilTexto.VACIO);
        setExperiencia(0);
        setDescripcion(UtilTexto.VACIO);
        setTipoDocumento(UtilTexto.VACIO);
        setDocumento(UtilTexto.VACIO);
        setPromedio(BigDecimal.ZERO);
    }

    public TecnicoEntidad(UUID id, UsuarioEntidad usuario, ServicioEntidad servicio, BarrioEntidad barrio, BigDecimal tarifa, int experiencia, String documento, BigDecimal promedio) {
        setId(id);
        setUsuario(usuario);
        setServicio(servicio);
        setBarrio(barrio);
        setTarifa(tarifa);
        setTipoTarifa(UtilTexto.VACIO);
        setExperiencia(experiencia);
        setDescripcion(UtilTexto.VACIO);
        setTipoDocumento(UtilTexto.VACIO);
        setDocumento(documento);
        setPromedio(promedio);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilUUID.obtenerValorDefecto(id);
    }

    public UsuarioEntidad getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntidad usuario) {
        this.usuario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(usuario, new UsuarioEntidad());
    }

    public ServicioEntidad getServicio() {
        return servicio;
    }

    public void setServicio(ServicioEntidad servicio) {
        this.servicio = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(servicio, new ServicioEntidad());
    }

    public BarrioEntidad getBarrio() {
        return barrio;
    }

    public void setBarrio(BarrioEntidad barrio) {
        this.barrio = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(barrio, new BarrioEntidad());
    }

    public BigDecimal getTarifa() {
        return tarifa;
    }

    public void setTarifa(BigDecimal tarifa) {
        this.tarifa = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(tarifa, BigDecimal.ZERO);
    }

    public String getTipoTarifa() {
        return tipoTarifa;
    }

    public void setTipoTarifa(String tipoTarifa) {
        this.tipoTarifa = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(tipoTarifa);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(descripcion);
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
