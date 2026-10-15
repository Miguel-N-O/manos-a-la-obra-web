package com.manosalaobra.negocio.negocio.ensamblador.impl;

import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.entidad.TecnicoEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.EnsambladorEntidad;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

public final class TecnicoEnsambladorEntidad implements EnsambladorEntidad<TecnicoDominio, TecnicoEntidad> {

    private static final EnsambladorEntidad<TecnicoDominio, TecnicoEntidad> INSTANCIA =
            new TecnicoEnsambladorEntidad();

    private TecnicoEnsambladorEntidad() {
    }

    public static EnsambladorEntidad<TecnicoDominio, TecnicoEntidad> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public TecnicoEntidad convertirAEntidad(TecnicoDominio dominio) {
        TecnicoDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new TecnicoDominio.Builder().build());
        return new TecnicoEntidad(
                dominioSeguro.getId(),
                UsuarioEnsambladorEntidad.getInstancia().convertirAEntidad(dominioSeguro.getUsuario()),
                ServicioEnsambladorEntidad.getInstancia().convertirAEntidad(dominioSeguro.getServicio()),
                BarrioEnsambladorEntidad.getInstancia().convertirAEntidad(dominioSeguro.getBarrio()),
                dominioSeguro.getTarifa(),
                dominioSeguro.getExperiencia(),
                dominioSeguro.getDocumento(),
                dominioSeguro.getPromedio());
    }

    @Override
    public TecnicoDominio convertirADominio(TecnicoEntidad entidad) {
        TecnicoEntidad entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                entidad, new TecnicoEntidad());
        return new TecnicoDominio.Builder()
                .id(entidadSegura.getId())
                .usuario(UsuarioEnsambladorEntidad.getInstancia().convertirADominio(entidadSegura.getUsuario()))
                .servicio(ServicioEnsambladorEntidad.getInstancia().convertirADominio(entidadSegura.getServicio()))
                .barrio(BarrioEnsambladorEntidad.getInstancia().convertirADominio(entidadSegura.getBarrio()))
                .tarifa(entidadSegura.getTarifa())
                .experiencia(entidadSegura.getExperiencia())
                .documento(entidadSegura.getDocumento())
                .promedio(entidadSegura.getPromedio())
                .build();
    }
}
