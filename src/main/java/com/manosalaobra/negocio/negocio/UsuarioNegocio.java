package com.manosalaobra.negocio.negocio;

import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.dominio.UsuarioDominio;
import java.util.List;
import java.util.UUID;

public interface UsuarioNegocio {

    UsuarioDominio registrarCiudadano(UsuarioDominio usuario);

    TecnicoDominio registrarTecnico(TecnicoDominio tecnico);

    UsuarioDominio iniciarSesion(UsuarioDominio credenciales);

    List<UsuarioDominio> consultarTodos();

    List<UsuarioDominio> consultarTodos(String texto, String rol, String estado);

    UsuarioDominio cambiarEstado(UUID administradorId, UUID usuarioId, String nombreEstado, String motivo);
}
