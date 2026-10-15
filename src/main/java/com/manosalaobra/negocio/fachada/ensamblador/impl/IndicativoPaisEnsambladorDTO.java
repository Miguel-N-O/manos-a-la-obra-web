package com.manosalaobra.negocio.fachada.ensamblador.impl;

import com.manosalaobra.dominio.IndicativoPaisDominio;
import com.manosalaobra.dto.IndicativoPaisDTO;
import com.manosalaobra.negocio.fachada.ensamblador.EnsambladorDTO;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import java.util.ArrayList;
import java.util.List;

public final class IndicativoPaisEnsambladorDTO implements EnsambladorDTO<IndicativoPaisDominio, IndicativoPaisDTO> {

    private static final EnsambladorDTO<IndicativoPaisDominio, IndicativoPaisDTO> INSTANCIA =
            new IndicativoPaisEnsambladorDTO();

    private IndicativoPaisEnsambladorDTO() {
    }

    public static EnsambladorDTO<IndicativoPaisDominio, IndicativoPaisDTO> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public IndicativoPaisDTO convertirADTO(IndicativoPaisDominio dominio) {
        IndicativoPaisDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new IndicativoPaisDominio.Builder().build());
        IndicativoPaisDTO dto = new IndicativoPaisDTO();
        dto.setId(dominioSeguro.getId());
        dto.setPais(dominioSeguro.getPais());
        dto.setIndicativo(dominioSeguro.getIndicativo());
        dto.setActivo(dominioSeguro.isActivo());
        return dto;
    }

    @Override
    public IndicativoPaisDominio convertirADominio(IndicativoPaisDTO dto) {
        IndicativoPaisDTO dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new IndicativoPaisDTO());
        return new IndicativoPaisDominio.Builder()
                .id(dtoSeguro.getId())
                .pais(dtoSeguro.getPais())
                .indicativo(dtoSeguro.getIndicativo())
                .activo(dtoSeguro.isActivo())
                .build();
    }

    @Override
    public List<IndicativoPaisDTO> convertirADTO(List<IndicativoPaisDominio> dominios) {
        List<IndicativoPaisDominio> dominiosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominios, new ArrayList<>());
        List<IndicativoPaisDTO> dtos = new ArrayList<>();
        for (IndicativoPaisDominio dominio : dominiosSeguros) {
            dtos.add(convertirADTO(dominio));
        }
        return dtos;
    }
}
