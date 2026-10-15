package com.manosalaobra.negocio.negocio.reglas.impl.usuario;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.manosalaobra.entidad.EstadoUsuarioEntidad;
import com.manosalaobra.entidad.UsuarioEntidad;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.utilitarios.Claves;

import org.junit.jupiter.api.Test;

class AsegurarCredencialesValidasReglaTest {

    private static final String MENSAJE = "Correo o contraseña incorrectos.";
    private final Regla<Object> regla = AsegurarCredencialesValidasRegla.obtenerInstancia();

    private static UsuarioEntidad usuario(String clave, String estado) {
        UsuarioEntidad usuario = new UsuarioEntidad();
        usuario.setClaveHash(Claves.cifrar(clave));
        EstadoUsuarioEntidad estadoUsuario = new EstadoUsuarioEntidad();
        estadoUsuario.setNombre(estado);
        usuario.setEstadoUsuario(estadoUsuario);
        return usuario;
    }

    @Test
    void conClaveCorrectaYUsuarioActivoNoLanzaExcepcion() {
        assertDoesNotThrow(() -> regla.ejecutar(usuario("Clave1234", "ACTIVO"), "Clave1234"));
    }

    @Test
    void conClaveIncorrectaLanzaMensajeGenerico() {
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> regla.ejecutar(usuario("Clave1234", "ACTIVO"), "Otra12345"));
        assertEquals(MENSAJE, excepcion.getMensajeUsuario());
    }

    @Test
    void conUsuarioInexistenteLanzaElMismoMensaje() {
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> regla.ejecutar(new UsuarioEntidad(), "Clave1234"));
        assertEquals(MENSAJE, excepcion.getMensajeUsuario());
    }

    @Test
    void conUsuarioNoActivoLanzaElMismoMensaje() {
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> regla.ejecutar(usuario("Clave1234", "SUSPENDIDO"), "Clave1234"));
        assertEquals(MENSAJE, excepcion.getMensajeUsuario());
    }
}
