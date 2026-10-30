package com.manosalaobra.negocio.negocio.ensamblador.impl;

import com.manosalaobra.dominio.SolicitudDominio;
import com.manosalaobra.entidad.SolicitudEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.EnsambladorEntidad;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

public final class SolicitudEnsambladorEntidad implements EnsambladorEntidad<SolicitudDominio, SolicitudEntidad> {

    private static final EnsambladorEntidad<SolicitudDominio, SolicitudEntidad> INSTANCIA =
            new SolicitudEnsambladorEntidad();

    private SolicitudEnsambladorEntidad() {
    }

    public static EnsambladorEntidad<SolicitudDominio, SolicitudEntidad> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public SolicitudEntidad convertirAEntidad(SolicitudDominio dominio) {
        SolicitudDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new SolicitudDominio.Builder().build());
        SolicitudEntidad entidad = new SolicitudEntidad(
                dominioSeguro.getId(),
                UsuarioEnsambladorEntidad.getInstancia().convertirAEntidad(dominioSeguro.getCiudadano()),
                TecnicoEnsambladorEntidad.getInstancia().convertirAEntidad(dominioSeguro.getTecnico()),
                dominioSeguro.getDescripcion(),
                dominioSeguro.getFechaHora(),
                EstadoSolicitudEnsambladorEntidad.getInstancia().convertirAEntidad(dominioSeguro.getEstadoSolicitud()),
                dominioSeguro.isNotificada());
        entidad.setBarrio(BarrioEnsambladorEntidad.getInstancia().convertirAEntidad(dominioSeguro.getBarrio()));
        entidad.setMotivoRechazo(dominioSeguro.getMotivoRechazo());
        entidad.setRespondidaEn(dominioSeguro.getRespondidaEn());
        entidad.setCreadaEn(dominioSeguro.getCreadaEn());
        return entidad;
    }

    @Override
    public SolicitudDominio convertirADominio(SolicitudEntidad entidad) {
        SolicitudEntidad entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                entidad, new SolicitudEntidad());
        return new SolicitudDominio.Builder()
                .id(entidadSegura.getId())
                .ciudadano(UsuarioEnsambladorEntidad.getInstancia().convertirADominio(entidadSegura.getCiudadano()))
                .tecnico(TecnicoEnsambladorEntidad.getInstancia().convertirADominio(entidadSegura.getTecnico()))
                .barrio(BarrioEnsambladorEntidad.getInstancia().convertirADominio(entidadSegura.getBarrio()))
                .descripcion(entidadSegura.getDescripcion())
                .fechaHora(entidadSegura.getFechaHora())
                .estadoSolicitud(EstadoSolicitudEnsambladorEntidad.getInstancia()
                        .convertirADominio(entidadSegura.getEstadoSolicitud()))
                .notificada(entidadSegura.isNotificada())
                .motivoRechazo(entidadSegura.getMotivoRechazo())
                .respondidaEn(entidadSegura.getRespondidaEn())
                .creadaEn(entidadSegura.getCreadaEn())
                .build();
    }
}
