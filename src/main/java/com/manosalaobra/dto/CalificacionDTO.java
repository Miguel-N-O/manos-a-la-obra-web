package com.manosalaobra.dto;

import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.util.UUID;

public final class CalificacionDTO {

    private UUID id;
    private SolicitudDTO solicitud;
    private int estrellas;
    private String comentario;
    private boolean visible;

    public CalificacionDTO() {
        setId(UtilUUID.obtenerUUIDDefecto());
        setSolicitud(new SolicitudDTO());
        setEstrellas(0);
        setComentario(UtilTexto.VACIO);
        setVisible(true);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilUUID.obtenerValorDefecto(id);
    }

    public SolicitudDTO getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudDTO solicitud) {
        this.solicitud = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(solicitud, new SolicitudDTO());
    }

    public int getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(int estrellas) {
        this.estrellas = estrellas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(comentario);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
