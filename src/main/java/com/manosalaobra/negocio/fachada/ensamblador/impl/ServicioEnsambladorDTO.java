package com.manosalaobra.negocio.fachada.ensamblador.impl;

import com.manosalaobra.dominio.ServicioDominio;
import com.manosalaobra.dto.ServicioDTO;
import com.manosalaobra.negocio.fachada.ensamblador.EnsambladorDTO;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import java.util.ArrayList;
import java.util.List;

public final class ServicioEnsambladorDTO implements EnsambladorDTO<ServicioDominio, ServicioDTO> {

    private static final EnsambladorDTO<ServicioDominio, ServicioDTO> INSTANCIA = new ServicioEnsambladorDTO();

    private ServicioEnsambladorDTO() {
    }

    public static EnsambladorDTO<ServicioDominio, ServicioDTO> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public ServicioDTO convertirADTO(ServicioDominio dominio) {
        ServicioDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new ServicioDominio.Builder().build());
        ServicioDTO dto = new ServicioDTO();
        dto.setId(dominioSeguro.getId());
        dto.setNombre(dominioSeguro.getNombre());
        dto.setActivo(dominioSeguro.isActivo());
        return dto;
    }

    @Override
    public ServicioDominio convertirADominio(ServicioDTO dto) {
        ServicioDTO dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new ServicioDTO());
        return new ServicioDominio.Builder()
                .id(dtoSeguro.getId())
                .nombre(dtoSeguro.getNombre())
                .activo(dtoSeguro.isActivo())
                .build();
    }

    @Override
    public List<ServicioDTO> convertirADTO(List<ServicioDominio> dominios) {
        List<ServicioDominio> dominiosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominios, new ArrayList<>());
        List<ServicioDTO> dtos = new ArrayList<>();
        for (ServicioDominio dominio : dominiosSeguros) {
            dtos.add(convertirADTO(dominio));
        }
        return dtos;
    }
}
