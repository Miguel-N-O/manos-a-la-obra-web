package com.manosalaobra.negocio.fachada.ensamblador.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.dto.UsuarioDTO;

import org.junit.jupiter.api.Test;

import java.util.List;

class UsuarioEnsambladorDTOTest {

    @Test
    void convertirADominioLlevaLaClaveEscritaPeroNoLaDevuelveAlDTO() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("  Ana Pérez ");
        dto.setCorreo(" ANA@Example.com ");
        dto.setClave("claveSegura1");

        UsuarioDominio dominio = UsuarioEnsambladorDTO.getInstancia().convertirADominio(dto);
        UsuarioDTO deVuelta = UsuarioEnsambladorDTO.getInstancia().convertirADTO(dominio);

        assertEquals("Ana Pérez", dominio.getNombre());
        assertEquals("ana@example.com", dominio.getCorreo());
        assertEquals("claveSegura1", dominio.getClave());
        assertEquals("", deVuelta.getClave());
    }

    @Test
    void convertirUnObjetoNuloDevuelveUnDTOConValoresPorDefecto() {
        UsuarioDTO dto = UsuarioEnsambladorDTO.getInstancia().convertirADTO((UsuarioDominio) null);
        assertNotNull(dto.getRol());
        assertEquals("", dto.getNombre());
    }

    @Test
    void convertirUnaListaNulaDevuelveUnaListaVacia() {
        List<UsuarioDTO> dtos = UsuarioEnsambladorDTO.getInstancia().convertirADTO((List<UsuarioDominio>) null);
        assertEquals(0, dtos.size());
    }
}
