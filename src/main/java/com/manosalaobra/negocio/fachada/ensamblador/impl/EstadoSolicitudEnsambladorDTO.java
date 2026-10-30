package com.manosalaobra.negocio.fachada.ensamblador.impl;

import com.manosalaobra.dominio.EstadoSolicitudDominio;
import com.manosalaobra.dto.EstadoSolicitudDTO;
import com.manosalaobra.negocio.fachada.ensamblador.EnsambladorDTO;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class EstadoSolicitudEnsambladorDTO implements EnsambladorDTO<EstadoSolicitudDominio, EstadoSolicitudDTO> {

    private static final EnsambladorDTO<EstadoSolicitudDominio, EstadoSolicitudDTO> INSTANCIA = new EstadoSolicitudEnsambladorDTO();

    private EstadoSolicitudEnsambladorDTO() {
    }

    public static EnsambladorDTO<EstadoSolicitudDominio, EstadoSolicitudDTO> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public EstadoSolicitudDTO convertirADTO(EstadoSolicitudDominio dominio) {
        EstadoSolicitudDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new EstadoSolicitudDominio.Builder().build());
        EstadoSolicitudDTO dto = new EstadoSolicitudDTO();
        dto.setId(dominioSeguro.getId());
        dto.setNombre(dominioSeguro.getNombre());
        dto.setActivo(dominioSeguro.isActivo());
        return dto;
    }

    @Override
    public EstadoSolicitudDominio convertirADominio(EstadoSolicitudDTO dto) {
        EstadoSolicitudDTO dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new EstadoSolicitudDTO());
        return new EstadoSolicitudDominio.Builder()
                .id(dtoSeguro.getId())
                .nombre(dtoSeguro.getNombre())
                .activo(dtoSeguro.isActivo())
                .build();
    }

    @Override
    public List<EstadoSolicitudDTO> convertirADTO(List<EstadoSolicitudDominio> dominios) {
        List<EstadoSolicitudDominio> dominiosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominios, new ArrayList<>());
        List<EstadoSolicitudDTO> dtos = new ArrayList<>();
        for (EstadoSolicitudDominio dominio : dominiosSeguros) {
            dtos.add(convertirADTO(dominio));
        }
        return dtos;
    }
}
