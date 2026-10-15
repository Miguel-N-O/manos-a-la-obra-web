package com.manosalaobra.negocio.negocio.reglas.impl.usuario;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.excepciones.enums.Capa;

import org.junit.jupiter.api.Test;

class ValidarDatosRegistrarNuevoUsuarioReglaTest {

    private final Regla<UsuarioDominio> regla = ValidarDatosRegistrarNuevoUsuarioRegla.obtenerInstancia();

    private UsuarioDominio ciudadano(String nombre, String correo, String clave, String telefono) {
        return new UsuarioDominio.Builder()
                .nombre(nombre).correo(correo).clave(clave).telefono(telefono).build();
    }

    private String mensajeDeUsuarioAlValidar(UsuarioDominio usuario) {
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class, () -> regla.ejecutar(usuario));
        assertEquals(Capa.NEGOCIO, excepcion.getCapa());
        return excepcion.getMensajeUsuario();
    }

    @Test
    void conTodosLosCamposVaciosIndicaCualesFaltan() {
        String mensaje = mensajeDeUsuarioAlValidar(ciudadano("", "", "", ""));
        assertTrue(mensaje.contains("tu nombre"));
        assertTrue(mensaje.contains("tu correo"));
        assertTrue(mensaje.contains("tu contraseña"));
    }

    @Test
    void soloMencionaLosCamposQueFaltan() {
        String mensaje = mensajeDeUsuarioAlValidar(ciudadano("Ana", "   ", "claveSegura1", ""));
        assertTrue(mensaje.contains("tu correo"));
        assertFalse(mensaje.contains("tu nombre"));
        assertFalse(mensaje.contains("tu contraseña"));
    }

    @Test
    void conUsuarioNuloIndicaQueFaltanTodosLosCampos() {
        assertTrue(mensajeDeUsuarioAlValidar(null).contains("tu nombre"));
    }

    @Test
    void conCorreoInvalidoLoRechaza() {
        assertEquals("El correo no es válido.",
                mensajeDeUsuarioAlValidar(ciudadano("Ana", "ana@", "claveSegura1", "")));
    }

    @Test
    void conClaveCortaLaRechaza() {
        assertTrue(mensajeDeUsuarioAlValidar(ciudadano("Ana", "ana@example.com", "1234567", ""))
                .contains("mínimo 8"));
    }

    @Test
    void conTelefonoMuyLargoLoRechaza() {
        String telefonoLargo = "3".repeat(21);
        assertTrue(mensajeDeUsuarioAlValidar(ciudadano("Ana", "ana@example.com", "claveSegura1", telefonoLargo))
                .contains("teléfono"));
    }

    @Test
    void conDatosValidosNoLanzaExcepcion() {
        assertDoesNotThrow(() -> regla.ejecutar(
                ciudadano("Ana Pérez", " Ana@Example.com ", "claveSegura1", "3001234567")));
    }
}
