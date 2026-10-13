package com.manosalaobra;

import static org.junit.jupiter.api.Assertions.*;

import com.manosalaobra.util.Claves;
import org.junit.jupiter.api.Test;

class ClavesTest {
    @Test
    void laClaveCifradaCoincideSoloConLaOriginal() {
        String hash = Claves.cifrar("secreta123");
        assertNotEquals("secreta123", hash);
        assertTrue(Claves.coincide("secreta123", hash));
        assertFalse(Claves.coincide("otra", hash));
    }
}
