package com.manosalaobra.negocio.fachada;

import com.manosalaobra.dto.TecnicoDTO;
import com.manosalaobra.dto.UsuarioDTO;

public interface UsuarioFachada {

    UsuarioDTO registrarCiudadano(UsuarioDTO usuario);

    UsuarioDTO registrarTecnico(TecnicoDTO tecnico);

    UsuarioDTO iniciarSesion(UsuarioDTO credenciales);
}
