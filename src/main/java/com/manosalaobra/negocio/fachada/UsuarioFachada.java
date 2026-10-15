package com.manosalaobra.negocio.fachada;

import com.manosalaobra.dto.UsuarioDTO;

public interface UsuarioFachada {

    UsuarioDTO registrarCiudadano(UsuarioDTO usuario);

    UsuarioDTO iniciarSesion(UsuarioDTO credenciales);
}
