package com.manosalaobra.negocio.negocio.reglas.impl.tecnico;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.manosalaobra.dominio.BarrioDominio;
import com.manosalaobra.dominio.ServicioDominio;
import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.excepciones.enums.Capa;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ValidarDatosPerfilTecnicoReglaTest {

    private static final String TARIFA_O_EXPERIENCIA =
            "La tarifa debe ser mayor a cero y la experiencia un número entre 0 y 60.";

    private final Regla<TecnicoDominio> regla = ValidarDatosPerfilTecnicoRegla.obtenerInstancia();

    private TecnicoDominio.Builder perfilValido() {
        return new TecnicoDominio.Builder()
                .servicio(new ServicioDominio.Builder().id(UtilUUID.generar()).build())
                .barrio(new BarrioDominio.Builder().id(UtilUUID.generar()).build())
                .tarifa(new BigDecimal("80000"))
                .tipoTarifa(TecnicoDominio.TARIFA_POR_TRABAJO)
                .experiencia(5)
                .descripcion("Plomero con experiencia en instalaciones residenciales.");
    }

    private String mensajeDeUsuarioAlValidar(TecnicoDominio perfil) {
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class, () -> regla.ejecutar(perfil));
        assertEquals(Capa.NEGOCIO, excepcion.getCapa());
        return excepcion.getMensajeUsuario();
    }

    @Test
    void conDatosValidosNoLanzaExcepcion() {
        assertDoesNotThrow(() -> regla.ejecutar(perfilValido().build()));
        assertDoesNotThrow(() -> regla.ejecutar(perfilValido().tipoTarifa("POR_HORA").descripcion("").build()));
    }

    @Test
    void conTodoVacioIndicaLosDatosObligatoriosQueFaltan() {
        String mensaje = mensajeDeUsuarioAlValidar(new TecnicoDominio.Builder().build());
        assertTrue(mensaje.startsWith("Para guardar tu perfil debes completar: "));
        assertTrue(mensaje.contains("la especialidad"));
        assertTrue(mensaje.contains("el barrio o zona de trabajo"));
        assertTrue(mensaje.contains("el tipo de tarifa"));
    }

    @Test
    void soloMencionaLoQueFalta() {
        String mensaje = mensajeDeUsuarioAlValidar(perfilValido()
                .barrio(new BarrioDominio.Builder().build()).build());
        assertTrue(mensaje.contains("el barrio o zona de trabajo"));
        assertFalse(mensaje.contains("la especialidad"));
        assertFalse(mensaje.contains("el tipo de tarifa"));
    }

    @Test
    void conTarifaCeroONegativaLanzaExcepcion() {
        assertEquals(TARIFA_O_EXPERIENCIA, mensajeDeUsuarioAlValidar(perfilValido().tarifa(BigDecimal.ZERO).build()));
        assertEquals(TARIFA_O_EXPERIENCIA,
                mensajeDeUsuarioAlValidar(perfilValido().tarifa(new BigDecimal("-100")).build()));
    }

    @Test
    void conTarifaMayorAlMaximoDeLaBaseLanzaExcepcion() {
        assertDoesNotThrow(() -> regla.ejecutar(perfilValido().tarifa(new BigDecimal("99999999.99")).build()));
        assertEquals("La tarifa no puede superar $99.999.999.",
                mensajeDeUsuarioAlValidar(perfilValido().tarifa(new BigDecimal("100000000")).build()));
    }

    @Test
    void laExperienciaDebeEstarEntre0Y60() {
        assertDoesNotThrow(() -> regla.ejecutar(perfilValido().experiencia(0).build()));
        assertDoesNotThrow(() -> regla.ejecutar(perfilValido().experiencia(60).build()));
        assertEquals(TARIFA_O_EXPERIENCIA, mensajeDeUsuarioAlValidar(perfilValido().experiencia(61).build()));
        assertEquals(TARIFA_O_EXPERIENCIA, mensajeDeUsuarioAlValidar(perfilValido().experiencia(-1).build()));
    }

    @Test
    void conTipoDeTarifaDesconocidoLanzaExcepcion() {
        assertEquals("El tipo de tarifa debe ser por hora o por trabajo.",
                mensajeDeUsuarioAlValidar(perfilValido().tipoTarifa("POR_DIA").build()));
    }

    @Test
    void laDescripcionAdmiteHasta500Caracteres() {
        assertDoesNotThrow(() -> regla.ejecutar(perfilValido().descripcion("a".repeat(500)).build()));
        assertEquals("La descripción no puede superar 500 caracteres.",
                mensajeDeUsuarioAlValidar(perfilValido().descripcion("a".repeat(501)).build()));
    }
}
