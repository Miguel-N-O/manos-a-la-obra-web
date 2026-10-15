package com.manosalaobra.negocio.negocio.ensamblador.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.manosalaobra.dominio.RolDominio;
import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.entidad.UsuarioEntidad;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import org.junit.jupiter.api.Test;

import java.util.UUID;

class UsuarioEnsambladorEntidadTest {

    @Test
    void laConversionDeIdaYVueltaConservaLosDatos() {
        UUID id = UtilUUID.generar();
        UsuarioDominio dominio = new UsuarioDominio.Builder()
                .id(id)
                .rol(new RolDominio.Builder().nombre(RolDominio.CIUDADANO).build())
                .nombre("Ana Pérez")
                .correo("ana@example.com")
                .claveHash("hash")
                .telefono("3001234567")
                .build();

        UsuarioEntidad entidad = UsuarioEnsambladorEntidad.getInstancia().convertirAEntidad(dominio);
        UsuarioDominio deVuelta = UsuarioEnsambladorEntidad.getInstancia().convertirADominio(entidad);

        assertEquals(id, entidad.getId());
        assertEquals(RolDominio.CIUDADANO, entidad.getRol().getNombre());
        assertEquals("hash", entidad.getClaveHash());
        assertEquals(dominio.getCorreo(), deVuelta.getCorreo());
        assertEquals(dominio.getTelefono(), deVuelta.getTelefono());
    }
}
