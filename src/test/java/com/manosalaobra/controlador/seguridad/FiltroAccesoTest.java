package com.manosalaobra.controlador.seguridad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class FiltroAccesoTest {

    private final FiltroAcceso filtro = new FiltroAcceso();

    private MockHttpServletResponse pedir(String ruta, String rol, MockFilterChain cadena) throws Exception {
        MockHttpServletRequest peticion = new MockHttpServletRequest("GET", ruta);
        if (rol != null) {
            peticion.getSession(true).setAttribute(SesionUsuario.ATRIBUTO_ROL, rol);
        }
        MockHttpServletResponse respuesta = new MockHttpServletResponse();
        filtro.doFilter(peticion, respuesta, cadena);
        return respuesta;
    }

    @Test
    void sinSesionRedirigeAlLogin() throws Exception {
        MockFilterChain cadena = new MockFilterChain();
        MockHttpServletResponse respuesta = pedir("/ciudadano/inicio.html", null, cadena);
        assertEquals("/login.html", respuesta.getRedirectedUrl());
        assertNull(cadena.getRequest());
    }

    @Test
    void conOtroRolRedirigeASuInicio() throws Exception {
        MockFilterChain cadena = new MockFilterChain();
        MockHttpServletResponse respuesta = pedir("/admin/inicio.html", "CIUDADANO", cadena);
        assertEquals("/ciudadano/inicio.html", respuesta.getRedirectedUrl());
        assertNull(cadena.getRequest());
    }

    @Test
    void conElRolCorrectoDejaPasar() throws Exception {
        MockFilterChain cadena = new MockFilterChain();
        pedir("/tecnico/inicio.html", "TECNICO", cadena);
        assertNotNull(cadena.getRequest());
    }

    @Test
    void rutasPublicasPasanSinSesion() throws Exception {
        MockFilterChain cadena = new MockFilterChain();
        pedir("/login.html", null, cadena);
        assertNotNull(cadena.getRequest());
    }

    @Test
    void mayusculasYRutasRarasNoEvitanElFiltro() throws Exception {
        for (String ruta : new String[] {"/Admin/inicio.html", "//admin/inicio.html", "/x/../admin/inicio.html"}) {
            MockFilterChain cadena = new MockFilterChain();
            MockHttpServletResponse respuesta = pedir(ruta, null, cadena);
            assertEquals("/login.html", respuesta.getRedirectedUrl(), ruta);
        }
    }
}
