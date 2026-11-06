package com.manosalaobra.negocio.fachada;

import com.manosalaobra.dto.TecnicoDTO;
import com.manosalaobra.dto.UsuarioDTO;
import java.util.List;
import java.util.UUID;

public interface UsuarioFachada {

    UsuarioDTO registrarCiudadano(UsuarioDTO usuario);

    UsuarioDTO registrarTecnico(TecnicoDTO tecnico);

    UsuarioDTO iniciarSesion(UsuarioDTO credenciales);

    List<UsuarioDTO> consultarTodos();

    List<UsuarioDTO> consultarTodos(String texto, String rol, String estado);

    UsuarioDTO cambiarEstado(UUID administradorId, UUID usuarioId, UsuarioDTO cambio);
}
