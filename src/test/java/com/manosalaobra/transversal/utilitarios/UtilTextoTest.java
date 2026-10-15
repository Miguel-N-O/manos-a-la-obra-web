package com.manosalaobra.transversal.utilitarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UtilTextoTest {

    private final UtilTexto utilTexto = UtilTexto.getUtilTexto();

    @Test
    void normalizaElCorreoSinEspaciosYEnMinusculas() {
        assertEquals("ana@example.com", utilTexto.normalizarCorreo("  Ana@Example.COM "));
    }

    @Test
    void trataElTextoNuloComoVacio() {
        assertEquals(UtilTexto.VACIO, utilTexto.quitarEspacioEnBlanco(null));
        assertTrue(utilTexto.esVacia("   "));
        assertFalse(utilTexto.esVacia("a"));
    }

    @Test
    void validaElFormatoDelCorreo() {
        assertTrue(utilTexto.formatoEsValido("ana@example.com", UtilTexto.CORREO));
        assertFalse(utilTexto.formatoEsValido("ana@", UtilTexto.CORREO));
    }

    @Test
    void validaLaLongitudDeLaCadena() {
        assertTrue(utilTexto.obtenerLongitudCadenaEsValida("  abc ", 1, 3, true));
        assertFalse(utilTexto.obtenerLongitudCadenaEsValida("  abc ", 1, 3, false));
    }
}
