package com.manosalaobra.negocio.fachada.ensamblador.impl;

import com.manosalaobra.dominio.EstadoUsuarioDominio;
import com.manosalaobra.dto.EstadoUsuarioDTO;
import com.manosalaobra.negocio.fachada.ensamblador.EnsambladorDTO;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class EstadoUsuarioEnsambladorDTO implements EnsambladorDTO<EstadoUsuarioDominio, EstadoUsuarioDTO> {

    private static final EnsambladorDTO<EstadoUsuarioDominio, EstadoUsuarioDTO> INSTANCIA = new EstadoUsuarioEnsambladorDTO();

    private EstadoUsuarioEnsambladorDTO() {
    }

    public static EnsambladorDTO<EstadoUsuarioDominio, EstadoUsuarioDTO> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public EstadoUsuarioDTO convertirADTO(EstadoUsuarioDominio dominio) {
        EstadoUsuarioDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new EstadoUsuarioDominio.Builder().build());
        EstadoUsuarioDTO dto = new EstadoUsuarioDTO();
        dto.setId(dominioSeguro.getId());
        dto.setNombre(dominioSeguro.getNombre());
        dto.setActivo(dominioSeguro.isActivo());
        return dto;
    }

    @Override
    public EstadoUsuarioDominio convertirADominio(EstadoUsuarioDTO dto) {
        EstadoUsuarioDTO dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new EstadoUsuarioDTO());
        return new EstadoUsuarioDominio.Builder()
                .id(dtoSeguro.getId())
                .nombre(dtoSeguro.getNombre())
                .activo(dtoSeguro.isActivo())
                .build();
    }

    @Override
    public List<EstadoUsuarioDTO> convertirADTO(List<EstadoUsuarioDominio> dominios) {
        List<EstadoUsuarioDominio> dominiosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominios, new ArrayList<>());
        List<EstadoUsuarioDTO> dtos = new ArrayList<>();
        for (EstadoUsuarioDominio dominio : dominiosSeguros) {
            dtos.add(convertirADTO(dominio));
        }
        return dtos;
    }
}
