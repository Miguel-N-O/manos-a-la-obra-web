package com.manosalaobra.negocio.negocio;

import com.manosalaobra.dominio.UsuarioDominio;

public interface UsuarioNegocio {

    UsuarioDominio registrarCiudadano(UsuarioDominio usuario);

    UsuarioDominio iniciarSesion(UsuarioDominio credenciales);
}
