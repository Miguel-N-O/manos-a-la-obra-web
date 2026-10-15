package com.manosalaobra.negocio.negocio.reglas.impl.usuario;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;

import org.junit.jupiter.api.Test;

class ValidarDatosIniciarSesionReglaTest {

    private final Regla<UsuarioDominio> regla = ValidarDatosIniciarSesionRegla.obtenerInstancia();

    private static UsuarioDominio credenciales(String correo, String clave) {
        return new UsuarioDominio.Builder().correo(correo).clave(clave).build();
    }

    @Test
    void sinCorreoLanzaExcepcion() {
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> regla.ejecutar(credenciales("", "Clave1234")));
        assertEquals("Escribe tu correo y tu contraseña.", excepcion.getMensajeUsuario());
    }

    @Test
    void sinClaveLanzaExcepcion() {
        assertThrows(ManosALaObraExcepcion.class, () -> regla.ejecutar(credenciales("ana@example.com", "")));
    }

    @Test
    void conCorreoYClaveNoLanzaExcepcion() {
        assertDoesNotThrow(() -> regla.ejecutar(credenciales("ana@example.com", "Clave1234")));
    }
}
