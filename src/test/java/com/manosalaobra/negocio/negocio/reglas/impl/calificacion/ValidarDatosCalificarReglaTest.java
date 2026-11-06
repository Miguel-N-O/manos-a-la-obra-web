package com.manosalaobra.negocio.negocio.reglas.impl.calificacion;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.manosalaobra.dominio.CalificacionDominio;
import com.manosalaobra.dominio.SolicitudDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import org.junit.jupiter.api.Test;

class ValidarDatosCalificarReglaTest {

    private final Regla<CalificacionDominio> regla = ValidarDatosCalificarRegla.obtenerInstancia();

    private static CalificacionDominio.Builder calificacion() {
        return new CalificacionDominio.Builder()
                .solicitud(new SolicitudDominio.Builder().id(UtilUUID.generar()).build())
                .estrellas(4)
                .comentario("Llegó puntual y dejó todo limpio.");
    }

    @Test
    void aceptaUnaCalificacionConEstrellasYComentario() {
        assertDoesNotThrow(() -> regla.ejecutar(calificacion().build()));
    }

    @Test
    void aceptaUnaCalificacionSinComentarioPorqueEsOpcional() {
        assertDoesNotThrow(() -> regla.ejecutar(calificacion().comentario("").build()));
    }

    @Test
    void aceptaLosExtremosDeUnoACincoEstrellas() {
        assertDoesNotThrow(() -> regla.ejecutar(calificacion().estrellas(1).build()));
        assertDoesNotThrow(() -> regla.ejecutar(calificacion().estrellas(5).build()));
    }

    @Test
    void rechazaEstrellasFueraDeRango() {
        for (int estrellas : new int[] {-1, 0, 6, 10}) {
            ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                    () -> regla.ejecutar(calificacion().estrellas(estrellas).build()));
            assertEquals("Elige de 1 a 5 estrellas para calificar al técnico.", excepcion.getMensajeUsuario());
        }
    }

    @Test
    void elComentarioEscritoTieneDeDiezATrescientosCaracteres() {
        assertDoesNotThrow(() -> regla.ejecutar(calificacion().comentario("a".repeat(10)).build()));
        assertDoesNotThrow(() -> regla.ejecutar(calificacion().comentario("a".repeat(300)).build()));
        for (int longitud : new int[] {1, 9, 301}) {
            ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                    () -> regla.ejecutar(calificacion().comentario("a".repeat(longitud)).build()));
            assertEquals("Tu comentario debe tener entre 10 y 300 caracteres.", excepcion.getMensajeUsuario());
        }
    }

    @Test
    void rechazaUnaCalificacionSinSolicitud() {
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> regla.ejecutar(new CalificacionDominio.Builder().estrellas(5).build()));
        assertEquals("Falta indicar el servicio que quieres calificar.", excepcion.getMensajeUsuario());
    }
}
