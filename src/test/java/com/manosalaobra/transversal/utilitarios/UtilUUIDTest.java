package com.manosalaobra.transversal.utilitarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.UUID;

class UtilUUIDTest {

    @Test
    void elValorNuloSeConvierteEnElUuidPorDefecto() {
        assertEquals(UtilUUID.obtenerUUIDDefecto(), UtilUUID.obtenerValorDefecto(null));
        assertTrue(UtilUUID.esValorDefecto(null));
    }

    @Test
    void unIdGeneradoNoEsElValorPorDefecto() {
        UUID id = UtilUUID.generar();
        assertFalse(UtilUUID.esValorDefecto(id));
        assertEquals(id, UtilUUID.convertirAUUID(" " + id + " "));
    }
}
