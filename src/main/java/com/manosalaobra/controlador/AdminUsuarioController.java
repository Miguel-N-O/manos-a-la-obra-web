package com.manosalaobra.controlador;

import com.manosalaobra.controlador.respuesta.Respuesta;
import com.manosalaobra.controlador.respuesta.RespuestaOk;
import com.manosalaobra.controlador.seguridad.AccesoPorRol;
import com.manosalaobra.controlador.seguridad.SesionUsuario;
import com.manosalaobra.dominio.EstadoUsuarioDominio;
import com.manosalaobra.dominio.RolDominio;
import com.manosalaobra.dto.UsuarioDTO;
import com.manosalaobra.negocio.fachada.UsuarioFachada;
import com.manosalaobra.negocio.fachada.impl.UsuarioFachadaImpl;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// HU-16: administración de cuentas por el administrador
@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminUsuarioController extends AbstractRestController {

    @GetMapping
    public ResponseEntity<Respuesta<UsuarioDTO>> consultarTodos(
            @RequestParam(name = "texto", required = false) String texto,
            @RequestParam(name = "rol", required = false) String rol,
            @RequestParam(name = "estado", required = false) String estado,
            HttpServletRequest peticion) {
        ResponseEntity<Respuesta<UsuarioDTO>> sinPermiso = exigirAdministrador(peticion);
        if (sinPermiso != null) {
            return sinPermiso;
        }
        UsuarioFachada usuarioFachada = new UsuarioFachadaImpl();
        List<UsuarioDTO> usuarios = usuarioFachada.consultarTodos(texto, rol, estado);
        if (usuarios.isEmpty()) {
            return ResponseEntity.ok(RespuestaOk.crear(usuarios,
                    List.of(CatalogoMensajes.AdminUsuarioController.USUARIO_SIN_USUARIOS)));
        }
        return ResponseEntity.ok(RespuestaOk.crear(usuarios));
    }

    // Cuerpo: { "estadoUsuario": { "nombre": "SUSPENDIDO" }, "motivoEstado": "..." }
    // Estados: ACTIVO, SUSPENDIDO o BLOQUEADO; el motivo es obligatorio salvo al reactivar
    // Buscar: GET /api/admin/usuarios?texto=ana&rol=TECNICO&estado=ACTIVO (nombre o correo, rol y estado opcionales)
    @PutMapping("/{id}/estado")
    public ResponseEntity<Respuesta<UsuarioDTO>> cambiarEstado(@PathVariable("id") UUID id,
            @RequestBody UsuarioDTO cambio, HttpServletRequest peticion) {
        ResponseEntity<Respuesta<UsuarioDTO>> sinPermiso = exigirAdministrador(peticion);
        if (sinPermiso != null) {
            return sinPermiso;
        }
        UsuarioFachada usuarioFachada = new UsuarioFachadaImpl();
        UsuarioDTO actualizado = usuarioFachada.cambiarEstado(SesionUsuario.idActual(peticion), id, cambio);
        // Quien queda suspendido o bloqueado pierde su sesión abierta; al reactivarlo puede volver a entrar
        if (EstadoUsuarioDominio.ACTIVO.equals(actualizado.getEstadoUsuario().getNombre())) {
            SesionUsuario.liberar(id);
        } else {
            SesionUsuario.restringir(id);
        }
        return ResponseEntity.ok(RespuestaOk.crear(
                actualizado, CatalogoMensajes.AdminUsuarioController.USUARIO_ESTADO_ACTUALIZADO));
    }

    private ResponseEntity<Respuesta<UsuarioDTO>> exigirAdministrador(HttpServletRequest peticion) {
        return AccesoPorRol.exigir(peticion, RolDominio.ADMIN,
                CatalogoMensajes.AdminUsuarioController.USUARIO_ERROR_NO_HAY_SESION,
                CatalogoMensajes.AdminUsuarioController.USUARIO_ERROR_SOLO_ADMINISTRADORES);
    }
}
