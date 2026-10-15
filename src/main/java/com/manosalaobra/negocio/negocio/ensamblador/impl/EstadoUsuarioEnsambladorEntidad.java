package com.manosalaobra.negocio.negocio.ensamblador.impl;

import com.manosalaobra.dominio.EstadoUsuarioDominio;
import com.manosalaobra.entidad.EstadoUsuarioEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.EnsambladorEntidad;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

public final class EstadoUsuarioEnsambladorEntidad implements EnsambladorEntidad<EstadoUsuarioDominio, EstadoUsuarioEntidad> {

    private static final EnsambladorEntidad<EstadoUsuarioDominio, EstadoUsuarioEntidad> INSTANCIA = new EstadoUsuarioEnsambladorEntidad();

    private EstadoUsuarioEnsambladorEntidad() {
    }

    public static EnsambladorEntidad<EstadoUsuarioDominio, EstadoUsuarioEntidad> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public EstadoUsuarioEntidad convertirAEntidad(EstadoUsuarioDominio dominio) {
        EstadoUsuarioDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new EstadoUsuarioDominio.Builder().build());
        return new EstadoUsuarioEntidad(dominioSeguro.getId(), dominioSeguro.getNombre(), dominioSeguro.isActivo());
    }

    @Override
    public EstadoUsuarioDominio convertirADominio(EstadoUsuarioEntidad entidad) {
        EstadoUsuarioEntidad entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                entidad, new EstadoUsuarioEntidad());
        return new EstadoUsuarioDominio.Builder()
                .id(entidadSegura.getId())
                .nombre(entidadSegura.getNombre())
                .activo(entidadSegura.isActivo())
                .build();
    }
}
