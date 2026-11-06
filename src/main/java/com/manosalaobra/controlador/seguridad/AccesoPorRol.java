package com.manosalaobra.controlador.seguridad;

import com.manosalaobra.controlador.respuesta.Respuesta;
import com.manosalaobra.controlador.respuesta.RespuestaError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class AccesoPorRol {

    private AccesoPorRol() {
    }

    // Devuelve null si hay sesión y el rol es el esperado; si no, la respuesta de error que debe devolverse
    public static <D> ResponseEntity<Respuesta<D>> exigir(HttpServletRequest peticion, String rolEsperado,
            String mensajeSinSesion, String mensajeRolIncorrecto) {
        String rol = SesionUsuario.rolActual(peticion);
        if (rol == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RespuestaError.crear(mensajeSinSesion));
        }
        if (!rolEsperado.equals(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RespuestaError.crear(mensajeRolIncorrecto));
        }
        return null;
    }
}
