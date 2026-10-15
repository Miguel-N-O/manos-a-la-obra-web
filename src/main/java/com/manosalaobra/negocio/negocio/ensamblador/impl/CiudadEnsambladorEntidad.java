package com.manosalaobra.negocio.negocio.ensamblador.impl;

import com.manosalaobra.dominio.CiudadDominio;
import com.manosalaobra.entidad.CiudadEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.EnsambladorEntidad;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

public final class CiudadEnsambladorEntidad implements EnsambladorEntidad<CiudadDominio, CiudadEntidad> {

    private static final EnsambladorEntidad<CiudadDominio, CiudadEntidad> INSTANCIA = new CiudadEnsambladorEntidad();

    private CiudadEnsambladorEntidad() {
    }

    public static EnsambladorEntidad<CiudadDominio, CiudadEntidad> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public CiudadEntidad convertirAEntidad(CiudadDominio dominio) {
        CiudadDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new CiudadDominio.Builder().build());
        return new CiudadEntidad(dominioSeguro.getId(), dominioSeguro.getNombre(), dominioSeguro.isActivo());
    }

    @Override
    public CiudadDominio convertirADominio(CiudadEntidad entidad) {
        CiudadEntidad entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                entidad, new CiudadEntidad());
        return new CiudadDominio.Builder()
                .id(entidadSegura.getId())
                .nombre(entidadSegura.getNombre())
                .activo(entidadSegura.isActivo())
                .build();
    }
}
