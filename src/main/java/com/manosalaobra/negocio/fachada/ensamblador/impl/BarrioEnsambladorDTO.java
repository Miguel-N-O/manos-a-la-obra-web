package com.manosalaobra.negocio.fachada.ensamblador.impl;

import com.manosalaobra.dominio.BarrioDominio;
import com.manosalaobra.dto.BarrioDTO;
import com.manosalaobra.negocio.fachada.ensamblador.EnsambladorDTO;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import java.util.ArrayList;
import java.util.List;

public final class BarrioEnsambladorDTO implements EnsambladorDTO<BarrioDominio, BarrioDTO> {

    private static final EnsambladorDTO<BarrioDominio, BarrioDTO> INSTANCIA = new BarrioEnsambladorDTO();

    private BarrioEnsambladorDTO() {
    }

    public static EnsambladorDTO<BarrioDominio, BarrioDTO> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public BarrioDTO convertirADTO(BarrioDominio dominio) {
        BarrioDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new BarrioDominio.Builder().build());
        BarrioDTO dto = new BarrioDTO();
        dto.setId(dominioSeguro.getId());
        dto.setCiudad(CiudadEnsambladorDTO.getInstancia().convertirADTO(dominioSeguro.getCiudad()));
        dto.setNombre(dominioSeguro.getNombre());
        dto.setActivo(dominioSeguro.isActivo());
        return dto;
    }

    @Override
    public BarrioDominio convertirADominio(BarrioDTO dto) {
        BarrioDTO dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new BarrioDTO());
        return new BarrioDominio.Builder()
                .id(dtoSeguro.getId())
                .ciudad(CiudadEnsambladorDTO.getInstancia().convertirADominio(dtoSeguro.getCiudad()))
                .nombre(dtoSeguro.getNombre())
                .activo(dtoSeguro.isActivo())
                .build();
    }

    @Override
    public List<BarrioDTO> convertirADTO(List<BarrioDominio> dominios) {
        List<BarrioDominio> dominiosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominios, new ArrayList<>());
        List<BarrioDTO> dtos = new ArrayList<>();
        for (BarrioDominio dominio : dominiosSeguros) {
            dtos.add(convertirADTO(dominio));
        }
        return dtos;
    }
}
