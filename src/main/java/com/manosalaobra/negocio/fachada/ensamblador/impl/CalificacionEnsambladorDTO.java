package com.manosalaobra.negocio.fachada.ensamblador.impl;

import com.manosalaobra.dominio.CalificacionDominio;
import com.manosalaobra.dto.CalificacionDTO;
import com.manosalaobra.dto.SolicitudDTO;
import com.manosalaobra.dto.TecnicoDTO;
import com.manosalaobra.dto.UsuarioDTO;
import com.manosalaobra.negocio.fachada.ensamblador.EnsambladorDTO;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import java.util.ArrayList;
import java.util.List;

public final class CalificacionEnsambladorDTO implements EnsambladorDTO<CalificacionDominio, CalificacionDTO> {

    private static final EnsambladorDTO<CalificacionDominio, CalificacionDTO> INSTANCIA =
            new CalificacionEnsambladorDTO();

    private CalificacionEnsambladorDTO() {
    }

    public static EnsambladorDTO<CalificacionDominio, CalificacionDTO> getInstancia() {
        return INSTANCIA;
    }

    // De la solicitud solo salen su id, quién calificó (nombre) y a qué técnico: nunca datos de contacto
    @Override
    public CalificacionDTO convertirADTO(CalificacionDominio dominio) {
        CalificacionDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new CalificacionDominio.Builder().build());

        UsuarioDTO ciudadano = new UsuarioDTO();
        ciudadano.setId(dominioSeguro.getSolicitud().getCiudadano().getId());
        ciudadano.setNombre(dominioSeguro.getSolicitud().getCiudadano().getNombre());

        UsuarioDTO usuarioTecnico = new UsuarioDTO();
        usuarioTecnico.setNombre(dominioSeguro.getSolicitud().getTecnico().getUsuario().getNombre());
        TecnicoDTO tecnico = new TecnicoDTO();
        tecnico.setId(dominioSeguro.getSolicitud().getTecnico().getId());
        tecnico.setUsuario(usuarioTecnico);

        SolicitudDTO solicitud = new SolicitudDTO();
        solicitud.setId(dominioSeguro.getSolicitud().getId());
        solicitud.setDescripcion(dominioSeguro.getSolicitud().getDescripcion());
        solicitud.setCiudadano(ciudadano);
        solicitud.setTecnico(tecnico);

        CalificacionDTO dto = new CalificacionDTO();
        dto.setId(dominioSeguro.getId());
        dto.setSolicitud(solicitud);
        dto.setEstrellas(dominioSeguro.getEstrellas());
        dto.setComentario(dominioSeguro.getComentario());
        dto.setVisible(dominioSeguro.isVisible());
        dto.setMotivoModeracion(dominioSeguro.getMotivoModeracion());
        return dto;
    }

    // Del cuerpo de la petición solo se toman la solicitud, las estrellas y el comentario; la visibilidad y el motivo no
    @Override
    public CalificacionDominio convertirADominio(CalificacionDTO dto) {
        CalificacionDTO dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new CalificacionDTO());
        return new CalificacionDominio.Builder()
                .solicitud(SolicitudEnsambladorDTO.getInstancia().convertirADominio(dtoSeguro.getSolicitud()))
                .estrellas(dtoSeguro.getEstrellas())
                .comentario(dtoSeguro.getComentario())
                .build();
    }

    @Override
    public List<CalificacionDTO> convertirADTO(List<CalificacionDominio> dominios) {
        List<CalificacionDominio> dominiosSeguros = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominios, new ArrayList<>());
        List<CalificacionDTO> dtos = new ArrayList<>();
        for (CalificacionDominio dominio : dominiosSeguros) {
            dtos.add(convertirADTO(dominio));
        }
        return dtos;
    }
}
