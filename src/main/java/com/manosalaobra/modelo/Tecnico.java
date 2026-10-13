package com.manosalaobra.modelo;

import java.math.BigDecimal;

public class Tecnico extends Usuario {
    private int servicioId;
    private String ciudad;
    private String barrio;
    private BigDecimal tarifa;
    private int experiencia;
    private double promedio;

    public int getServicioId() { return servicioId; }
    public void setServicioId(int servicioId) { this.servicioId = servicioId; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getBarrio() { return barrio; }
    public void setBarrio(String barrio) { this.barrio = barrio; }
    public BigDecimal getTarifa() { return tarifa; }
    public void setTarifa(BigDecimal tarifa) { this.tarifa = tarifa; }
    public int getExperiencia() { return experiencia; }
    public void setExperiencia(int experiencia) { this.experiencia = experiencia; }
    public double getPromedio() { return promedio; }
    public void setPromedio(double promedio) { this.promedio = promedio; }
}
