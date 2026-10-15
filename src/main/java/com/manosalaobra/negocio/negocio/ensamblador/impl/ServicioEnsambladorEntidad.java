package com.manosalaobra.negocio.negocio.ensamblador.impl;

import com.manosalaobra.dominio.ServicioDominio;
import com.manosalaobra.entidad.ServicioEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.EnsambladorEntidad;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

public final class ServicioEnsambladorEntidad implements EnsambladorEntidad<ServicioDominio, ServicioEntidad> {

    private static final EnsambladorEntidad<ServicioDominio, ServicioEntidad> INSTANCIA = new ServicioEnsambladorEntidad();

    private ServicioEnsambladorEntidad() {
    }

    public static EnsambladorEntidad<ServicioDominio, ServicioEntidad> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public ServicioEntidad convertirAEntidad(ServicioDominio dominio) {
        ServicioDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new ServicioDominio.Builder().build());
        return new ServicioEntidad(dominioSeguro.getId(), dominioSeguro.getNombre(), dominioSeguro.isActivo());
    }

    @Override
    public ServicioDominio convertirADominio(ServicioEntidad entidad) {
        ServicioEntidad entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                entidad, new ServicioEntidad());
        return new ServicioDominio.Builder()
                .id(entidadSegura.getId())
                .nombre(entidadSegura.getNombre())
                .activo(entidadSegura.isActivo())
                .build();
    }
}
