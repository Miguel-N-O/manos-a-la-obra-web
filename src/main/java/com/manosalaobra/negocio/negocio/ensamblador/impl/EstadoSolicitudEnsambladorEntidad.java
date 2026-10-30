package com.manosalaobra.negocio.negocio.ensamblador.impl;

import com.manosalaobra.dominio.EstadoSolicitudDominio;
import com.manosalaobra.entidad.EstadoSolicitudEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.EnsambladorEntidad;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

public final class EstadoSolicitudEnsambladorEntidad implements EnsambladorEntidad<EstadoSolicitudDominio, EstadoSolicitudEntidad> {

    private static final EnsambladorEntidad<EstadoSolicitudDominio, EstadoSolicitudEntidad> INSTANCIA = new EstadoSolicitudEnsambladorEntidad();

    private EstadoSolicitudEnsambladorEntidad() {
    }

    public static EnsambladorEntidad<EstadoSolicitudDominio, EstadoSolicitudEntidad> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public EstadoSolicitudEntidad convertirAEntidad(EstadoSolicitudDominio dominio) {
        EstadoSolicitudDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new EstadoSolicitudDominio.Builder().build());
        return new EstadoSolicitudEntidad(dominioSeguro.getId(), dominioSeguro.getNombre(), dominioSeguro.isActivo());
    }

    @Override
    public EstadoSolicitudDominio convertirADominio(EstadoSolicitudEntidad entidad) {
        EstadoSolicitudEntidad entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                entidad, new EstadoSolicitudEntidad());
        return new EstadoSolicitudDominio.Builder()
                .id(entidadSegura.getId())
                .nombre(entidadSegura.getNombre())
                .activo(entidadSegura.isActivo())
                .build();
    }
}
