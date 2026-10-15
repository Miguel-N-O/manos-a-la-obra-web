package com.manosalaobra.negocio.fachada.ensamblador.impl;

import com.manosalaobra.dominio.RolDominio;
import com.manosalaobra.dto.RolDTO;
import com.manosalaobra.negocio.fachada.ensamblador.EnsambladorDTO;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class RolEnsambladorDTO implements EnsambladorDTO<RolDominio, RolDTO> {

    private static final EnsambladorDTO<RolDominio, RolDTO> INSTANCIA = new RolEnsambladorDTO();

    private RolEnsambladorDTO() {
    }

    public static EnsambladorDTO<RolDominio, RolDTO> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public RolDTO convertirADTO(RolDominio dominio) {
        RolDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new RolDominio.Builder().build());
        RolDTO dto = new RolDTO();
        dto.setId(dominioSeguro.getId());
        dto.setNombre(dominioSeguro.getNombre());
        dto.setActivo(dominioSeguro.isActivo());
        return dto;
    }

    @Override
    public RolDominio convertirADominio(RolDTO dto) {
        RolDTO dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new RolDTO());
        return new RolDominio.Builder()
                .id(dtoSeguro.getId())
                .nombre(dtoSeguro.getNombre())
                .activo(dtoSeguro.isActivo())
                .build();
    }

    @Override
    public List<RolDTO> convertirADTO(List<RolDominio> dominios) {
        List<RolDominio> dominiosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominios, new ArrayList<>());
        List<RolDTO> dtos = new ArrayList<>();
        for (RolDominio dominio : dominiosSeguros) {
            dtos.add(convertirADTO(dominio));
        }
        return dtos;
    }
}
