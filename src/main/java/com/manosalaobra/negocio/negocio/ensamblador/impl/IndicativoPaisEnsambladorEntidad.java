package com.manosalaobra.negocio.negocio.ensamblador.impl;

import com.manosalaobra.dominio.IndicativoPaisDominio;
import com.manosalaobra.entidad.IndicativoPaisEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.EnsambladorEntidad;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

public final class IndicativoPaisEnsambladorEntidad
        implements EnsambladorEntidad<IndicativoPaisDominio, IndicativoPaisEntidad> {

    private static final EnsambladorEntidad<IndicativoPaisDominio, IndicativoPaisEntidad> INSTANCIA =
            new IndicativoPaisEnsambladorEntidad();

    private IndicativoPaisEnsambladorEntidad() {
    }

    public static EnsambladorEntidad<IndicativoPaisDominio, IndicativoPaisEntidad> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public IndicativoPaisEntidad convertirAEntidad(IndicativoPaisDominio dominio) {
        IndicativoPaisDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new IndicativoPaisDominio.Builder().build());
        return new IndicativoPaisEntidad(dominioSeguro.getId(), dominioSeguro.getPais(),
                dominioSeguro.getIndicativo(), dominioSeguro.isActivo());
    }

    @Override
    public IndicativoPaisDominio convertirADominio(IndicativoPaisEntidad entidad) {
        IndicativoPaisEntidad entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                entidad, new IndicativoPaisEntidad());
        return new IndicativoPaisDominio.Builder()
                .id(entidadSegura.getId())
                .pais(entidadSegura.getPais())
                .indicativo(entidadSegura.getIndicativo())
                .activo(entidadSegura.isActivo())
                .build();
    }
}
