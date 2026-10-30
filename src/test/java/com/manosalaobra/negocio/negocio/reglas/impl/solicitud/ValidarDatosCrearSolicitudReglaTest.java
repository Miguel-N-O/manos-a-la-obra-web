package com.manosalaobra.negocio.negocio.reglas.impl.solicitud;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.manosalaobra.dominio.BarrioDominio;
import com.manosalaobra.dominio.SolicitudDominio;
import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.excepciones.enums.Capa;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ValidarDatosCrearSolicitudReglaTest {

    private static final String DESCRIPCION_VALIDA = "Se rompió el tubo debajo del lavaplatos.";

    private final Regla<SolicitudDominio> regla = ValidarDatosCrearSolicitudRegla.obtenerInstancia();

    private SolicitudDominio.Builder solicitudValida() {
        return new SolicitudDominio.Builder()
                .tecnico(new TecnicoDominio.Builder().id(UtilUUID.generar()).build())
                .barrio(new BarrioDominio.Builder().id(UtilUUID.generar()).build())
                .descripcion(DESCRIPCION_VALIDA)
                .fechaHora(LocalDateTime.now().plusDays(2));
    }

    private String mensajeDeUsuarioAlValidar(SolicitudDominio solicitud) {
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class, () -> regla.ejecutar(solicitud));
        assertEquals(Capa.NEGOCIO, excepcion.getCapa());
        return excepcion.getMensajeUsuario();
    }

    @Test
    void conDatosValidosNoLanzaExcepcion() {
        assertDoesNotThrow(() -> regla.ejecutar(solicitudValida().build()));
    }

    @Test
    void conTodoVacioIndicaLaInformacionFaltante() {
        String mensaje = mensajeDeUsuarioAlValidar(new SolicitudDominio.Builder().build());
        assertTrue(mensaje.startsWith("Para enviar la solicitud debes completar: "));
        assertTrue(mensaje.contains("el técnico"));
        assertTrue(mensaje.contains("la descripción del trabajo"));
        assertTrue(mensaje.contains("el barrio donde se realizará el trabajo"));
        assertTrue(mensaje.contains("la fecha y hora propuestas"));
    }

    @Test
    void soloMencionaLoQueFalta() {
        String mensaje = mensajeDeUsuarioAlValidar(solicitudValida().descripcion("   ").build());
        assertTrue(mensaje.contains("la descripción del trabajo"));
        assertFalse(mensaje.contains("el barrio"));
        assertFalse(mensaje.contains("la fecha"));
    }

    @Test
    void laDescripcionDebeTenerEntre10Y500Caracteres() {
        String esperado = "La descripción del trabajo debe tener entre 10 y 500 caracteres.";
        assertEquals(esperado, mensajeDeUsuarioAlValidar(solicitudValida().descripcion("Fuga").build()));
        assertEquals(esperado, mensajeDeUsuarioAlValidar(solicitudValida().descripcion("a".repeat(501)).build()));
        assertDoesNotThrow(() -> regla.ejecutar(solicitudValida().descripcion("a".repeat(10)).build()));
        assertDoesNotThrow(() -> regla.ejecutar(solicitudValida().descripcion("a".repeat(500)).build()));
    }

    @Test
    void laFechaYHoraDebenSerPosterioresAlMomentoActual() {
        String esperado = "La fecha y hora propuestas deben ser posteriores al momento actual.";
        assertEquals(esperado, mensajeDeUsuarioAlValidar(
                solicitudValida().fechaHora(LocalDateTime.now().minusMinutes(1)).build()));
        assertEquals(esperado, mensajeDeUsuarioAlValidar(
                solicitudValida().fechaHora(LocalDateTime.now().minusDays(3)).build()));
    }
}
