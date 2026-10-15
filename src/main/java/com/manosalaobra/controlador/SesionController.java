package com.manosalaobra.controlador;

import com.manosalaobra.controlador.respuesta.Respuesta;
import com.manosalaobra.controlador.respuesta.RespuestaError;
import com.manosalaobra.controlador.respuesta.RespuestaOk;
import com.manosalaobra.controlador.seguridad.SesionUsuario;
import com.manosalaobra.dto.UsuarioDTO;
import com.manosalaobra.negocio.fachada.UsuarioFachada;
import com.manosalaobra.negocio.fachada.impl.UsuarioFachadaImpl;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sesion")
public class SesionController extends AbstractRestController {

    @PostMapping
    public ResponseEntity<Respuesta<UsuarioDTO>> iniciarSesion(@RequestBody UsuarioDTO credenciales,
            HttpServletRequest peticion) {
        UsuarioFachada usuarioFachada = new UsuarioFachadaImpl();
        UsuarioDTO usuario = usuarioFachada.iniciarSesion(credenciales);
        SesionUsuario.iniciar(peticion, usuario);
        return ResponseEntity.ok(RespuestaOk.crear(usuario,
                CatalogoMensajes.SesionController.USUARIO_SESION_INICIADA));
    }

    @GetMapping
    public ResponseEntity<Respuesta<UsuarioDTO>> consultarSesion(HttpServletRequest peticion) {
        String rol = SesionUsuario.rolActual(peticion);
        if (rol == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RespuestaError.crear(
                    CatalogoMensajes.SesionController.USUARIO_ERROR_NO_HAY_SESION));
        }
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNombre(SesionUsuario.nombreActual(peticion));
        usuario.getRol().setNombre(rol);
        return ResponseEntity.ok(RespuestaOk.crear(usuario, SesionUsuario.paginaDeInicio(rol)));
    }

    @DeleteMapping
    public ResponseEntity<Respuesta<UsuarioDTO>> cerrarSesion(HttpServletRequest peticion) {
        SesionUsuario.cerrar(peticion);
        return ResponseEntity.ok(RespuestaOk.crear(new UsuarioDTO(),
                CatalogoMensajes.SesionController.USUARIO_SESION_CERRADA));
    }
}
