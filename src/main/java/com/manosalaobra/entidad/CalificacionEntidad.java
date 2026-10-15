package com.manosalaobra.entidad;

import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.util.UUID;

public final class CalificacionEntidad {

    private UUID id;
    private SolicitudEntidad solicitud;
    private int estrellas;
    private String comentario;
    private boolean visible;

    public CalificacionEntidad() {
        setId(UtilUUID.obtenerUUIDDefecto());
        setSolicitud(new SolicitudEntidad());
        setEstrellas(0);
        setComentario(UtilTexto.VACIO);
        setVisible(true);
    }

    public CalificacionEntidad(UUID id, SolicitudEntidad solicitud, int estrellas, String comentario, boolean visible) {
        setId(id);
        setSolicitud(solicitud);
        setEstrellas(estrellas);
        setComentario(comentario);
        setVisible(visible);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = UtilUUID.obtenerValorDefecto(id);
    }

    public SolicitudEntidad getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudEntidad solicitud) {
        this.solicitud = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(solicitud, new SolicitudEntidad());
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
