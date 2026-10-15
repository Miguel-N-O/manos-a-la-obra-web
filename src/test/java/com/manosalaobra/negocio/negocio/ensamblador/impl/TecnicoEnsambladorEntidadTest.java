package com.manosalaobra.negocio.negocio.ensamblador.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.manosalaobra.dominio.BarrioDominio;
import com.manosalaobra.dominio.CiudadDominio;
import com.manosalaobra.dominio.ServicioDominio;
import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.entidad.TecnicoEntidad;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TecnicoEnsambladorEntidadTest {

    @Test
    void laConversionDeIdaYVueltaConservaLosDatos() {
        UUID id = UtilUUID.generar();
        TecnicoDominio dominio = new TecnicoDominio.Builder()
                .id(id)
                .usuario(new UsuarioDominio.Builder().nombre("Andrés Ruiz").build())
                .servicio(new ServicioDominio.Builder().nombre("Electricista").build())
                .barrio(new BarrioDominio.Builder()
                        .nombre("San Antonio")
                        .ciudad(new CiudadDominio.Builder().nombre("Rionegro").build())
                        .build())
                .tarifa(new BigDecimal("90000"))
                .experiencia(12)
                .promedio(new BigDecimal("4.8"))
                .build();

        TecnicoEntidad entidad = TecnicoEnsambladorEntidad.getInstancia().convertirAEntidad(dominio);
        TecnicoDominio deVuelta = TecnicoEnsambladorEntidad.getInstancia().convertirADominio(entidad);

        assertEquals(id, entidad.getId());
        assertEquals("Electricista", entidad.getServicio().getNombre());
        assertEquals("Rionegro", entidad.getBarrio().getCiudad().getNombre());
        assertEquals("Andrés Ruiz", deVuelta.getUsuario().getNombre());
        assertEquals("San Antonio", deVuelta.getBarrio().getNombre());
        assertEquals(new BigDecimal("90000"), deVuelta.getTarifa());
        assertEquals(12, deVuelta.getExperiencia());
    }
}
