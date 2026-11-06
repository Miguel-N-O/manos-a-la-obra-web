package com.manosalaobra.negocio.negocio.ensamblador.impl;

import com.manosalaobra.dominio.CalificacionDominio;
import com.manosalaobra.entidad.CalificacionEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.EnsambladorEntidad;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

public final class CalificacionEnsambladorEntidad implements EnsambladorEntidad<CalificacionDominio, CalificacionEntidad> {

    private static final EnsambladorEntidad<CalificacionDominio, CalificacionEntidad> INSTANCIA =
            new CalificacionEnsambladorEntidad();

    private CalificacionEnsambladorEntidad() {
    }

    public static EnsambladorEntidad<CalificacionDominio, CalificacionEntidad> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public CalificacionEntidad convertirAEntidad(CalificacionDominio dominio) {
        CalificacionDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new CalificacionDominio.Builder().build());
        CalificacionEntidad entidad = new CalificacionEntidad(
                dominioSeguro.getId(),
                SolicitudEnsambladorEntidad.getInstancia().convertirAEntidad(dominioSeguro.getSolicitud()),
                dominioSeguro.getEstrellas(),
                dominioSeguro.getComentario(),
                dominioSeguro.isVisible());
        entidad.setMotivoModeracion(dominioSeguro.getMotivoModeracion());
        return entidad;
    }

    @Override
    public CalificacionDominio convertirADominio(CalificacionEntidad entidad) {
        CalificacionEntidad entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                entidad, new CalificacionEntidad());
        return new CalificacionDominio.Builder()
                .id(entidadSegura.getId())
                .solicitud(SolicitudEnsambladorEntidad.getInstancia().convertirADominio(entidadSegura.getSolicitud()))
                .estrellas(entidadSegura.getEstrellas())
                .comentario(entidadSegura.getComentario())
                .visible(entidadSegura.isVisible())
                .motivoModeracion(entidadSegura.getMotivoModeracion())
                .build();
    }
}
