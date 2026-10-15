package com.manosalaobra.negocio.negocio.ensamblador.impl;

import com.manosalaobra.dominio.BarrioDominio;
import com.manosalaobra.entidad.BarrioEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.EnsambladorEntidad;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

public final class BarrioEnsambladorEntidad implements EnsambladorEntidad<BarrioDominio, BarrioEntidad> {

    private static final EnsambladorEntidad<BarrioDominio, BarrioEntidad> INSTANCIA = new BarrioEnsambladorEntidad();

    private BarrioEnsambladorEntidad() {
    }

    public static EnsambladorEntidad<BarrioDominio, BarrioEntidad> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public BarrioEntidad convertirAEntidad(BarrioDominio dominio) {
        BarrioDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new BarrioDominio.Builder().build());
        return new BarrioEntidad(
                dominioSeguro.getId(),
                CiudadEnsambladorEntidad.getInstancia().convertirAEntidad(dominioSeguro.getCiudad()),
                dominioSeguro.getNombre(),
                dominioSeguro.isActivo());
    }

    @Override
    public BarrioDominio convertirADominio(BarrioEntidad entidad) {
        BarrioEntidad entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                entidad, new BarrioEntidad());
        return new BarrioDominio.Builder()
                .id(entidadSegura.getId())
                .ciudad(CiudadEnsambladorEntidad.getInstancia().convertirADominio(entidadSegura.getCiudad()))
                .nombre(entidadSegura.getNombre())
                .activo(entidadSegura.isActivo())
                .build();
    }
}
