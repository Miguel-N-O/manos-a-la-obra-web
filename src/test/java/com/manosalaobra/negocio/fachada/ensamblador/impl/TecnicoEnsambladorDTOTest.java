package com.manosalaobra.negocio.fachada.ensamblador.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.dto.TecnicoDTO;
import com.manosalaobra.dto.UsuarioDTO;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TecnicoEnsambladorDTOTest {

    private TecnicoDominio tecnicoConDatosDeContacto(UUID id) {
        UsuarioDominio usuario = new UsuarioDominio.Builder()
                .id(UtilUUID.generar())
                .nombre("Andrés Ruiz")
                .correo("andres@example.com")
                .clave("claveSecreta1")
                .claveHash("$2a$10$hashSecreto")
                .numeroTelefono("3200000001")
                .build();
        return new TecnicoDominio.Builder()
                .id(id)
                .usuario(usuario)
                .tarifa(new BigDecimal("90000"))
                .experiencia(12)
                .documento("1037500001")
                .promedio(new BigDecimal("4.8"))
                .build();
    }

    @Test
    void elDTODeBusquedaNoLlevaCorreoTelefonoClaveNiDocumento() {
        TecnicoDTO dto = TecnicoEnsambladorDTO.getInstancia().convertirADTO(tecnicoConDatosDeContacto(UtilUUID.generar()));
        UsuarioDTO usuario = dto.getUsuario();

        assertEquals("Andrés Ruiz", usuario.getNombre());
        assertEquals("", usuario.getCorreo());
        assertEquals("", usuario.getNumeroTelefono());
        assertEquals("", usuario.getClave());
        assertEquals("", dto.getDocumento());
    }

    @Test
    void elDTOConservaTarifaExperienciaYPromedio() {
        UUID id = UtilUUID.generar();
        TecnicoDTO dto = TecnicoEnsambladorDTO.getInstancia().convertirADTO(tecnicoConDatosDeContacto(id));

        assertEquals(id, dto.getId());
        assertEquals(new BigDecimal("90000"), dto.getTarifa());
        assertEquals(12, dto.getExperiencia());
        assertEquals(new BigDecimal("4.8"), dto.getPromedio());
    }

    @Test
    void convertirUnObjetoNuloDevuelveUnDTOConValoresPorDefecto() {
        TecnicoDTO dto = TecnicoEnsambladorDTO.getInstancia().convertirADTO((TecnicoDominio) null);

        assertEquals("", dto.getUsuario().getNombre());
        assertEquals(BigDecimal.ZERO, dto.getPromedio());
    }

    @Test
    void convertirUnaListaNulaDevuelveUnaListaVacia() {
        List<TecnicoDTO> dtos = TecnicoEnsambladorDTO.getInstancia().convertirADTO((List<TecnicoDominio>) null);

        assertEquals(0, dtos.size());
    }

    @Test
    void convertirADominioLlevaLosFiltrosDeEspecialidadYBarrio() {
        UUID idServicio = UtilUUID.generar();
        UUID idBarrio = UtilUUID.generar();
        TecnicoDTO filtro = new TecnicoDTO();
        filtro.getServicio().setId(idServicio);
        filtro.getBarrio().setId(idBarrio);

        TecnicoDominio dominio = TecnicoEnsambladorDTO.getInstancia().convertirADominio(filtro);

        assertEquals(idServicio, dominio.getServicio().getId());
        assertEquals(idBarrio, dominio.getBarrio().getId());
    }
}
