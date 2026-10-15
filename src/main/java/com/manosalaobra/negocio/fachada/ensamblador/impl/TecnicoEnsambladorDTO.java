package com.manosalaobra.negocio.fachada.ensamblador.impl;

import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.dto.TecnicoDTO;
import com.manosalaobra.dto.UsuarioDTO;
import com.manosalaobra.negocio.fachada.ensamblador.EnsambladorDTO;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import java.util.ArrayList;
import java.util.List;

public final class TecnicoEnsambladorDTO implements EnsambladorDTO<TecnicoDominio, TecnicoDTO> {

    private static final EnsambladorDTO<TecnicoDominio, TecnicoDTO> INSTANCIA = new TecnicoEnsambladorDTO();

    private TecnicoEnsambladorDTO() {
    }

    public static EnsambladorDTO<TecnicoDominio, TecnicoDTO> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public TecnicoDTO convertirADTO(TecnicoDominio dominio) {
        TecnicoDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new TecnicoDominio.Builder().build());
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNombre(dominioSeguro.getUsuario().getNombre());

        TecnicoDTO dto = new TecnicoDTO();
        dto.setId(dominioSeguro.getId());
        dto.setUsuario(usuario);
        dto.setServicio(ServicioEnsambladorDTO.getInstancia().convertirADTO(dominioSeguro.getServicio()));
        dto.setBarrio(BarrioEnsambladorDTO.getInstancia().convertirADTO(dominioSeguro.getBarrio()));
        dto.setTarifa(dominioSeguro.getTarifa());
        dto.setExperiencia(dominioSeguro.getExperiencia());
        dto.setPromedio(dominioSeguro.getPromedio());
        return dto;
    }

    @Override
    public TecnicoDominio convertirADominio(TecnicoDTO dto) {
        TecnicoDTO dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new TecnicoDTO());
        return new TecnicoDominio.Builder()
                .id(dtoSeguro.getId())
                .servicio(ServicioEnsambladorDTO.getInstancia().convertirADominio(dtoSeguro.getServicio()))
                .barrio(BarrioEnsambladorDTO.getInstancia().convertirADominio(dtoSeguro.getBarrio()))
                .tarifa(dtoSeguro.getTarifa())
                .experiencia(dtoSeguro.getExperiencia())
                .promedio(dtoSeguro.getPromedio())
                .build();
    }

    @Override
    public List<TecnicoDTO> convertirADTO(List<TecnicoDominio> dominios) {
        List<TecnicoDominio> dominiosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominios, new ArrayList<>());
        List<TecnicoDTO> dtos = new ArrayList<>();
        for (TecnicoDominio dominio : dominiosSeguros) {
            dtos.add(convertirADTO(dominio));
        }
        return dtos;
    }
}
