package com.manosalaobra.negocio.negocio.ensamblador.impl;

import com.manosalaobra.dominio.RolDominio;
import com.manosalaobra.entidad.RolEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.EnsambladorEntidad;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

public final class RolEnsambladorEntidad implements EnsambladorEntidad<RolDominio, RolEntidad> {

    private static final EnsambladorEntidad<RolDominio, RolEntidad> INSTANCIA = new RolEnsambladorEntidad();

    private RolEnsambladorEntidad() {
    }

    public static EnsambladorEntidad<RolDominio, RolEntidad> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public RolEntidad convertirAEntidad(RolDominio dominio) {
        RolDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new RolDominio.Builder().build());
        return new RolEntidad(dominioSeguro.getId(), dominioSeguro.getNombre(), dominioSeguro.isActivo());
    }

    @Override
    public RolDominio convertirADominio(RolEntidad entidad) {
        RolEntidad entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                entidad, new RolEntidad());
        return new RolDominio.Builder()
                .id(entidadSegura.getId())
                .nombre(entidadSegura.getNombre())
                .activo(entidadSegura.isActivo())
                .build();
    }
}
