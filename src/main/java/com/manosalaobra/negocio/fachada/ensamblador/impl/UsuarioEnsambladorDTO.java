package com.manosalaobra.negocio.fachada.ensamblador.impl;

import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.dto.UsuarioDTO;
import com.manosalaobra.negocio.fachada.ensamblador.EnsambladorDTO;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class UsuarioEnsambladorDTO implements EnsambladorDTO<UsuarioDominio, UsuarioDTO> {

    private static final EnsambladorDTO<UsuarioDominio, UsuarioDTO> INSTANCIA = new UsuarioEnsambladorDTO();

    private UsuarioEnsambladorDTO() {
    }

    public static EnsambladorDTO<UsuarioDominio, UsuarioDTO> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public UsuarioDTO convertirADTO(UsuarioDominio dominio) {
        UsuarioDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new UsuarioDominio.Builder().build());
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(dominioSeguro.getId());
        dto.setRol(RolEnsambladorDTO.getInstancia().convertirADTO(dominioSeguro.getRol()));
        dto.setEstadoUsuario(EstadoUsuarioEnsambladorDTO.getInstancia().convertirADTO(dominioSeguro.getEstadoUsuario()));
        dto.setNombre(dominioSeguro.getNombre());
        dto.setCorreo(dominioSeguro.getCorreo());
        dto.setTelefono(dominioSeguro.getTelefono());
        return dto;
    }

    @Override
    public UsuarioDominio convertirADominio(UsuarioDTO dto) {
        UsuarioDTO dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new UsuarioDTO());
        return new UsuarioDominio.Builder()
                .id(dtoSeguro.getId())
                .rol(RolEnsambladorDTO.getInstancia().convertirADominio(dtoSeguro.getRol()))
                .estadoUsuario(EstadoUsuarioEnsambladorDTO.getInstancia().convertirADominio(dtoSeguro.getEstadoUsuario()))
                .nombre(dtoSeguro.getNombre())
                .correo(dtoSeguro.getCorreo())
                .clave(dtoSeguro.getClave())
                .telefono(dtoSeguro.getTelefono())
                .build();
    }

    @Override
    public List<UsuarioDTO> convertirADTO(List<UsuarioDominio> dominios) {
        List<UsuarioDominio> dominiosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominios, new ArrayList<>());
        List<UsuarioDTO> dtos = new ArrayList<>();
        for (UsuarioDominio dominio : dominiosSeguros) {
            dtos.add(convertirADTO(dominio));
        }
        return dtos;
    }
}
