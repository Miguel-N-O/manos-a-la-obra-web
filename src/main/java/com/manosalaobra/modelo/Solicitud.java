package com.manosalaobra.modelo;

import java.time.LocalDateTime;

public class Solicitud {
    private int id;
    private int ciudadanoId;
    private int tecnicoId;
    private String descripcion;
    private LocalDateTime fechaHora;
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCiudadanoId() { return ciudadanoId; }
    public void setCiudadanoId(int ciudadanoId) { this.ciudadanoId = ciudadanoId; }
    public int getTecnicoId() { return tecnicoId; }
    public void setTecnicoId(int tecnicoId) { this.tecnicoId = tecnicoId; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public EstadoSolicitud getEstado() { return estado; }
    public void setEstado(EstadoSolicitud estado) { this.estado = estado; }
}
