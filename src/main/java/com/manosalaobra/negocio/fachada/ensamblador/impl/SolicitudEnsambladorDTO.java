package com.manosalaobra.negocio.fachada.ensamblador.impl;

import com.manosalaobra.dominio.SolicitudDominio;
import com.manosalaobra.dto.SolicitudDTO;
import com.manosalaobra.dto.UsuarioDTO;
import com.manosalaobra.negocio.fachada.ensamblador.EnsambladorDTO;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import java.util.ArrayList;
import java.util.List;

public final class SolicitudEnsambladorDTO implements EnsambladorDTO<SolicitudDominio, SolicitudDTO> {

    private static final EnsambladorDTO<SolicitudDominio, SolicitudDTO> INSTANCIA = new SolicitudEnsambladorDTO();

    private SolicitudEnsambladorDTO() {
    }

    public static EnsambladorDTO<SolicitudDominio, SolicitudDTO> getInstancia() {
        return INSTANCIA;
    }

    // Del ciudadano solo sale el nombre; los datos de contacto se entregan aparte al aceptar (HU-13)
    @Override
    public SolicitudDTO convertirADTO(SolicitudDominio dominio) {
        SolicitudDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new SolicitudDominio.Builder().build());
        UsuarioDTO ciudadano = new UsuarioDTO();
        ciudadano.setId(dominioSeguro.getCiudadano().getId());
        ciudadano.setNombre(dominioSeguro.getCiudadano().getNombre());

        SolicitudDTO dto = new SolicitudDTO();
        dto.setId(dominioSeguro.getId());
        dto.setCiudadano(ciudadano);
        dto.setTecnico(TecnicoEnsambladorDTO.getInstancia().convertirADTO(dominioSeguro.getTecnico()));
        dto.setBarrio(BarrioEnsambladorDTO.getInstancia().convertirADTO(dominioSeguro.getBarrio()));
        dto.setDescripcion(dominioSeguro.getDescripcion());
        dto.setFechaHora(dominioSeguro.getFechaHora());
        dto.setEstadoSolicitud(EstadoSolicitudEnsambladorDTO.getInstancia()
                .convertirADTO(dominioSeguro.getEstadoSolicitud()));
        dto.setNotificada(dominioSeguro.isNotificada());
        dto.setMotivoRechazo(dominioSeguro.getMotivoRechazo());
        dto.setRespondidaEn(dominioSeguro.getRespondidaEn());
        dto.setCreadaEn(dominioSeguro.getCreadaEn());
        return dto;
    }

    @Override
    public SolicitudDominio convertirADominio(SolicitudDTO dto) {
        SolicitudDTO dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new SolicitudDTO());
        return new SolicitudDominio.Builder()
                .id(dtoSeguro.getId())
                .tecnico(TecnicoEnsambladorDTO.getInstancia().convertirADominio(dtoSeguro.getTecnico()))
                .barrio(BarrioEnsambladorDTO.getInstancia().convertirADominio(dtoSeguro.getBarrio()))
                .descripcion(dtoSeguro.getDescripcion())
                .fechaHora(dtoSeguro.getFechaHora())
                .motivoRechazo(dtoSeguro.getMotivoRechazo())
                .build();
    }

    @Override
    public List<SolicitudDTO> convertirADTO(List<SolicitudDominio> dominios) {
        List<SolicitudDominio> dominiosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominios, new ArrayList<>());
        List<SolicitudDTO> dtos = new ArrayList<>();
        for (SolicitudDominio dominio : dominiosSeguros) {
            dtos.add(convertirADTO(dominio));
        }
        return dtos;
    }
}
