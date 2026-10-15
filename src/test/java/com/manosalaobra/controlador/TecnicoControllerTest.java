package com.manosalaobra.controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.manosalaobra.controlador.respuesta.EstadoRespuesta;
import com.manosalaobra.controlador.respuesta.Respuesta;
import com.manosalaobra.controlador.seguridad.SesionUsuario;
import com.manosalaobra.dto.TecnicoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

class TecnicoControllerTest {

    private final TecnicoController controlador = new TecnicoController();

    private ResponseEntity<Respuesta<TecnicoDTO>> buscarConRol(String rol) {
        MockHttpServletRequest peticion = new MockHttpServletRequest("GET", "/api/tecnicos");
        if (rol != null) {
            peticion.getSession(true).setAttribute(SesionUsuario.ATRIBUTO_ROL, rol);
        }
        return controlador.consultarTecnicos(null, null, peticion);
    }

    @Test
    void sinSesionResponde401() {
        ResponseEntity<Respuesta<TecnicoDTO>> respuesta = buscarConRol(null);

        assertEquals(401, respuesta.getStatusCode().value());
        assertEquals(EstadoRespuesta.ERROR, respuesta.getBody().getEstado());
        assertEquals("Inicia sesión para buscar técnicos.", respuesta.getBody().getMensajes().get(0));
    }

    @Test
    void conRolDeTecnicoResponde403() {
        ResponseEntity<Respuesta<TecnicoDTO>> respuesta = buscarConRol("TECNICO");

        assertEquals(403, respuesta.getStatusCode().value());
        assertEquals("Solo los ciudadanos pueden buscar técnicos.", respuesta.getBody().getMensajes().get(0));
    }

    @Test
    void conRolDeAdministradorResponde403() {
        assertEquals(403, buscarConRol("ADMIN").getStatusCode().value());
    }
}
