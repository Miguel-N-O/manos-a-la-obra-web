package com.manosalaobra.negocio.fachada.ensamblador.impl;

import com.manosalaobra.dominio.CiudadDominio;
import com.manosalaobra.dto.CiudadDTO;
import com.manosalaobra.negocio.fachada.ensamblador.EnsambladorDTO;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import java.util.ArrayList;
import java.util.List;

public final class CiudadEnsambladorDTO implements EnsambladorDTO<CiudadDominio, CiudadDTO> {

    private static final EnsambladorDTO<CiudadDominio, CiudadDTO> INSTANCIA = new CiudadEnsambladorDTO();

    private CiudadEnsambladorDTO() {
    }

    public static EnsambladorDTO<CiudadDominio, CiudadDTO> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public CiudadDTO convertirADTO(CiudadDominio dominio) {
        CiudadDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new CiudadDominio.Builder().build());
        CiudadDTO dto = new CiudadDTO();
        dto.setId(dominioSeguro.getId());
        dto.setNombre(dominioSeguro.getNombre());
        dto.setActivo(dominioSeguro.isActivo());
        return dto;
    }

    @Override
    public CiudadDominio convertirADominio(CiudadDTO dto) {
        CiudadDTO dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new CiudadDTO());
        return new CiudadDominio.Builder()
                .id(dtoSeguro.getId())
                .nombre(dtoSeguro.getNombre())
                .activo(dtoSeguro.isActivo())
                .build();
    }

    @Override
    public List<CiudadDTO> convertirADTO(List<CiudadDominio> dominios) {
        List<CiudadDominio> dominiosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominios, new ArrayList<>());
        List<CiudadDTO> dtos = new ArrayList<>();
        for (CiudadDominio dominio : dominiosSeguros) {
            dtos.add(convertirADTO(dominio));
        }
        return dtos;
    }
}
