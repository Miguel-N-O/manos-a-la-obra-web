package com.manosalaobra.negocio.negocio.reglas.impl.usuario;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.manosalaobra.dominio.IndicativoPaisDominio;
import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.excepciones.enums.Capa;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import org.junit.jupiter.api.Test;

class ValidarDatosRegistrarNuevoUsuarioReglaTest {

    private static final String TELEFONO_VALIDO = "3001234567";

    private final Regla<UsuarioDominio> regla = ValidarDatosRegistrarNuevoUsuarioRegla.obtenerInstancia();

    private UsuarioDominio ciudadano(String nombre, String correo, String clave, String numeroTelefono) {
        return ciudadanoConIndicativo(nombre, correo, clave, numeroTelefono, true);
    }

    private UsuarioDominio ciudadanoConIndicativo(String nombre, String correo, String clave,
            String numeroTelefono, boolean conIndicativo) {
        UsuarioDominio.Builder builder = new UsuarioDominio.Builder()
                .nombre(nombre).correo(correo).clave(clave).numeroTelefono(numeroTelefono);
        if (conIndicativo) {
            builder.indicativoPais(new IndicativoPaisDominio.Builder().id(UtilUUID.generar()).build());
        }
        return builder.build();
    }

    private String mensajeDeUsuarioAlValidar(UsuarioDominio usuario) {
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class, () -> regla.ejecutar(usuario));
        assertEquals(Capa.NEGOCIO, excepcion.getCapa());
        return excepcion.getMensajeUsuario();
    }

    @Test
    void conTodosLosCamposVaciosIndicaCualesFaltan() {
        String mensaje = mensajeDeUsuarioAlValidar(ciudadanoConIndicativo("", "", "", "", false));
        assertTrue(mensaje.contains("tu nombre"));
        assertTrue(mensaje.contains("tu correo"));
        assertTrue(mensaje.contains("tu contraseña"));
        assertTrue(mensaje.contains("el indicativo de país"));
        assertTrue(mensaje.contains("tu teléfono"));
    }

    @Test
    void soloMencionaLosCamposQueFaltan() {
        String mensaje = mensajeDeUsuarioAlValidar(ciudadano("Ana", "   ", "claveSegura1", TELEFONO_VALIDO));
        assertTrue(mensaje.contains("tu correo"));
        assertFalse(mensaje.contains("tu nombre"));
        assertFalse(mensaje.contains("tu contraseña"));
        assertFalse(mensaje.contains("tu teléfono"));
    }

    @Test
    void sinIndicativoDePaisIndicaQueFalta() {
        String mensaje = mensajeDeUsuarioAlValidar(
                ciudadanoConIndicativo("Ana", "ana@example.com", "claveSegura1", TELEFONO_VALIDO, false));
        assertTrue(mensaje.contains("el indicativo de país"));
        assertFalse(mensaje.contains("tu teléfono"));
    }

    @Test
    void sinNumeroDeTelefonoIndicaQueFalta() {
        String mensaje = mensajeDeUsuarioAlValidar(ciudadano("Ana", "ana@example.com", "claveSegura1", "  "));
        assertTrue(mensaje.contains("tu teléfono"));
        assertFalse(mensaje.contains("el indicativo de país"));
    }

    @Test
    void conUsuarioNuloIndicaQueFaltanTodosLosCampos() {
        assertTrue(mensajeDeUsuarioAlValidar(null).contains("tu nombre"));
    }

    @Test
    void conCorreoInvalidoLoRechaza() {
        assertEquals("El correo no es válido.",
                mensajeDeUsuarioAlValidar(ciudadano("Ana", "ana@", "claveSegura1", TELEFONO_VALIDO)));
    }

    @Test
    void conClaveCortaLaRechaza() {
        assertTrue(mensajeDeUsuarioAlValidar(ciudadano("Ana", "ana@example.com", "1234567", TELEFONO_VALIDO))
                .contains("mínimo 8"));
    }

    @Test
    void conClaveMasLargaQueElLimiteDeBcryptLaRechaza() {
        String claveLarga = "a".repeat(73);
        assertTrue(mensajeDeUsuarioAlValidar(ciudadano("Ana", "ana@example.com", claveLarga, TELEFONO_VALIDO))
                .contains("máximo 72"));
    }

    @Test
    void conTelefonoConLetrasOSimbolosLoRechaza() {
        for (String invalido : new String[] { "300abc4567", "+573001234567", "300 123 4567", "300-1234567" }) {
            assertTrue(mensajeDeUsuarioAlValidar(ciudadano("Ana", "ana@example.com", "claveSegura1", invalido))
                    .contains("solo puede tener números"), invalido);
        }
    }

    @Test
    void conTelefonoFueraDelRangoDe7A14DigitosLoRechaza() {
        for (String invalido : new String[] { "123456", "1".repeat(15) }) {
            assertTrue(mensajeDeUsuarioAlValidar(ciudadano("Ana", "ana@example.com", "claveSegura1", invalido))
                    .contains("entre 7 y 14"), invalido);
        }
    }

    @Test
    void conTelefonoEnLosLimitesDelRangoLoAcepta() {
        assertDoesNotThrow(() -> regla.ejecutar(ciudadano("Ana", "ana@example.com", "claveSegura1", "1234567")));
        assertDoesNotThrow(() -> regla.ejecutar(
                ciudadano("Ana", "ana@example.com", "claveSegura1", "1".repeat(14))));
    }

    @Test
    void conDatosValidosNoLanzaExcepcion() {
        assertDoesNotThrow(() -> regla.ejecutar(
                ciudadano("Ana Pérez", " Ana@Example.com ", "claveSegura1", TELEFONO_VALIDO)));
    }
}
