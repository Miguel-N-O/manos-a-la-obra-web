package com.manosalaobra.negocio.negocio;

import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.dominio.UsuarioDominio;

public interface UsuarioNegocio {

    UsuarioDominio registrarCiudadano(UsuarioDominio usuario);

    TecnicoDominio registrarTecnico(TecnicoDominio tecnico);

    UsuarioDominio iniciarSesion(UsuarioDominio credenciales);
}
