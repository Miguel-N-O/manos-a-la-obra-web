package com.manosalaobra.modelo;

public class Calificacion {
    private int id;
    private int solicitudId;
    private int estrellas;
    private String comentario;
    private boolean visible = true;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getSolicitudId() { return solicitudId; }
    public void setSolicitudId(int solicitudId) { this.solicitudId = solicitudId; }
    public int getEstrellas() { return estrellas; }
    public void setEstrellas(int estrellas) { this.estrellas = estrellas; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
}
