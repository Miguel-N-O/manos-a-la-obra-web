package com.manosalaobra.negocio.fachada.ensamblador.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.manosalaobra.dominio.CalificacionDominio;
import com.manosalaobra.dominio.SolicitudDominio;
import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.dto.CalificacionDTO;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import org.junit.jupiter.api.Test;

class CalificacionEnsambladorDTOTest {

    @Test
    void laCalificacionNoExponeContactoDeNadie() {
        UsuarioDominio ciudadano = new UsuarioDominio.Builder().id(UtilUUID.generar()).nombre("Ana")
                .correo("ana@example.com").numeroTelefono("3001112233").build();
        UsuarioDominio usuarioTecnico = new UsuarioDominio.Builder().id(UtilUUID.generar()).nombre("Beto")
                .correo("beto@example.com").numeroTelefono("3104445566").build();
        TecnicoDominio tecnico = new TecnicoDominio.Builder().id(UtilUUID.generar()).usuario(usuarioTecnico).build();
        CalificacionDominio calificacion = new CalificacionDominio.Builder()
                .id(UtilUUID.generar())
                .solicitud(new SolicitudDominio.Builder().id(UtilUUID.generar()).ciudadano(ciudadano)
                        .tecnico(tecnico).build())
                .estrellas(5)
                .comentario("Muy bien")
                .build();

        CalificacionDTO dto = CalificacionEnsambladorDTO.getInstancia().convertirADTO(calificacion);

        assertEquals("Ana", dto.getSolicitud().getCiudadano().getNombre());
        assertEquals("Beto", dto.getSolicitud().getTecnico().getUsuario().getNombre());
        assertEquals("", dto.getSolicitud().getCiudadano().getNumeroTelefono());
        assertEquals("", dto.getSolicitud().getCiudadano().getCorreo());
        assertEquals("", dto.getSolicitud().getTecnico().getUsuario().getNumeroTelefono());
        assertEquals("", dto.getSolicitud().getTecnico().getUsuario().getCorreo());
        assertEquals(5, dto.getEstrellas());
        assertTrue(dto.isVisible());
    }

    @Test
    void desdeElCuerpoSoloTomaSolicitudEstrellasYComentario() {
        CalificacionDTO cuerpo = new CalificacionDTO();
        cuerpo.getSolicitud().setId(UtilUUID.generar());
        cuerpo.setEstrellas(4);
        cuerpo.setComentario("Bien");
        cuerpo.setVisible(false);

        CalificacionDominio dominio = CalificacionEnsambladorDTO.getInstancia().convertirADominio(cuerpo);

        assertEquals(cuerpo.getSolicitud().getId(), dominio.getSolicitud().getId());
        assertEquals(4, dominio.getEstrellas());
        assertEquals("Bien", dominio.getComentario());
        // La visibilidad solo la cambia el administrador, no quien califica
        assertTrue(dominio.isVisible());
    }
}
