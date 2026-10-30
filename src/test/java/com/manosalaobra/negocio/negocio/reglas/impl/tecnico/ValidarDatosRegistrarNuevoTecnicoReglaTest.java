package com.manosalaobra.negocio.negocio.reglas.impl.tecnico;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.manosalaobra.dominio.IndicativoPaisDominio;
import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.excepciones.enums.Capa;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import org.junit.jupiter.api.Test;

class ValidarDatosRegistrarNuevoTecnicoReglaTest {

    private final Regla<TecnicoDominio> regla = ValidarDatosRegistrarNuevoTecnicoRegla.obtenerInstancia();

    private UsuarioDominio usuarioCompleto() {
        return new UsuarioDominio.Builder()
                .nombre("Pedro Gómez").correo("pedro@example.com").clave("claveSegura1").numeroTelefono("3001234567")
                .indicativoPais(new IndicativoPaisDominio.Builder().id(UtilUUID.generar()).build())
                .build();
    }

    private TecnicoDominio tecnico(String tipoDocumento, String documento) {
        return new TecnicoDominio.Builder()
                .usuario(usuarioCompleto()).tipoDocumento(tipoDocumento).documento(documento).build();
    }

    private String mensajeDeUsuarioAlValidar(TecnicoDominio tecnico) {
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class, () -> regla.ejecutar(tecnico));
        assertEquals(Capa.NEGOCIO, excepcion.getCapa());
        return excepcion.getMensajeUsuario();
    }

    @Test
    void conDatosValidosNoLanzaExcepcion() {
        assertDoesNotThrow(() -> regla.ejecutar(tecnico("CC", "1036123456")));
        assertDoesNotThrow(() -> regla.ejecutar(tecnico("CE", "AB12345")));
    }

    @Test
    void conTodoVacioIndicaCadaDatoQueFalta() {
        String mensaje = mensajeDeUsuarioAlValidar(new TecnicoDominio.Builder().build());
        assertTrue(mensaje.contains("tu nombre"));
        assertTrue(mensaje.contains("el tipo de documento"));
        assertTrue(mensaje.contains("el número de documento"));
        assertTrue(mensaje.contains("tu correo"));
        assertTrue(mensaje.contains("tu contraseña"));
        assertTrue(mensaje.contains("el indicativo de país"));
        assertTrue(mensaje.contains("tu teléfono"));
    }

    @Test
    void soloMencionaLosDatosQueFaltan() {
        String mensaje = mensajeDeUsuarioAlValidar(tecnico("CC", "   "));
        assertTrue(mensaje.contains("el número de documento"));
        assertFalse(mensaje.contains("el tipo de documento"));
        assertFalse(mensaje.contains("tu nombre"));
    }

    @Test
    void conTipoDeDocumentoDistintoDeCedulaLanzaExcepcion() {
        String esperado = "El tipo de documento debe ser cédula de ciudadanía o cédula de extranjería.";
        assertEquals(esperado, mensajeDeUsuarioAlValidar(tecnico("PA", "1036123456")));
        assertEquals(esperado, mensajeDeUsuarioAlValidar(tecnico("cc", "1036123456")));
    }

    @Test
    void conDocumentoConPuntosOEspaciosLanzaExcepcion() {
        String esperado = "El número de documento solo puede tener letras y números, sin espacios ni puntos.";
        assertEquals(esperado, mensajeDeUsuarioAlValidar(tecnico("CC", "1.036.123.456")));
        assertEquals(esperado, mensajeDeUsuarioAlValidar(tecnico("CC", "1036 123456")));
        assertEquals(esperado, mensajeDeUsuarioAlValidar(tecnico("CC", "1036-123456")));
    }

    @Test
    void conDocumentoDeMasDe30CaracteresLanzaExcepcion() {
        assertDoesNotThrow(() -> regla.ejecutar(tecnico("CC", "1".repeat(30))));
        assertEquals("El número de documento es muy largo (máximo 30 caracteres).",
                mensajeDeUsuarioAlValidar(tecnico("CC", "1".repeat(31))));
    }
}
