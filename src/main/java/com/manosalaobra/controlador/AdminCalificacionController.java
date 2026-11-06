package com.manosalaobra.controlador;

import com.manosalaobra.controlador.respuesta.Respuesta;
import com.manosalaobra.controlador.respuesta.RespuestaOk;
import com.manosalaobra.controlador.seguridad.AccesoPorRol;
import com.manosalaobra.dominio.RolDominio;
import com.manosalaobra.dto.CalificacionDTO;
import com.manosalaobra.negocio.fachada.CalificacionFachada;
import com.manosalaobra.negocio.fachada.impl.CalificacionFachadaImpl;
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
import org.springframework.web.bind.annotation.RestController;

// HU-17: moderación de comentarios por el administrador
@RestController
@RequestMapping("/api/admin/calificaciones")
public class AdminCalificacionController extends AbstractRestController {

    @GetMapping
    public ResponseEntity<Respuesta<CalificacionDTO>> consultarTodas(HttpServletRequest peticion) {
        ResponseEntity<Respuesta<CalificacionDTO>> sinPermiso = exigirAdministrador(peticion);
        if (sinPermiso != null) {
            return sinPermiso;
        }
        CalificacionFachada calificacionFachada = new CalificacionFachadaImpl();
        List<CalificacionDTO> calificaciones = calificacionFachada.consultarTodasParaModeracion();
        if (calificaciones.isEmpty()) {
            return ResponseEntity.ok(RespuestaOk.crear(calificaciones,
                    List.of(CatalogoMensajes.AdminCalificacionController.USUARIO_SIN_CALIFICACIONES)));
        }
        return ResponseEntity.ok(RespuestaOk.crear(calificaciones));
    }

    // Cuerpo: { "motivoModeracion": "LENGUAJE_OFENSIVO" }; también INFORMACION_PERSONAL, CONTENIDO_NO_RELACIONADO u OTRO
    @PutMapping("/{id}/ocultar")
    public ResponseEntity<Respuesta<CalificacionDTO>> ocultar(@PathVariable("id") UUID id,
            @RequestBody CalificacionDTO cuerpo, HttpServletRequest peticion) {
        ResponseEntity<Respuesta<CalificacionDTO>> sinPermiso = exigirAdministrador(peticion);
        if (sinPermiso != null) {
            return sinPermiso;
        }
        CalificacionFachada calificacionFachada = new CalificacionFachadaImpl();
        return ResponseEntity.ok(RespuestaOk.crear(calificacionFachada.ocultarComentario(id, cuerpo.getMotivoModeracion()),
                CatalogoMensajes.AdminCalificacionController.USUARIO_COMENTARIO_OCULTO));
    }

    @PutMapping("/{id}/mostrar")
    public ResponseEntity<Respuesta<CalificacionDTO>> mostrar(@PathVariable("id") UUID id,
            HttpServletRequest peticion) {
        ResponseEntity<Respuesta<CalificacionDTO>> sinPermiso = exigirAdministrador(peticion);
        if (sinPermiso != null) {
            return sinPermiso;
        }
        CalificacionFachada calificacionFachada = new CalificacionFachadaImpl();
        return ResponseEntity.ok(RespuestaOk.crear(calificacionFachada.mostrarComentario(id),
                CatalogoMensajes.AdminCalificacionController.USUARIO_COMENTARIO_VISIBLE));
    }

    private ResponseEntity<Respuesta<CalificacionDTO>> exigirAdministrador(HttpServletRequest peticion) {
        return AccesoPorRol.exigir(peticion, RolDominio.ADMIN,
                CatalogoMensajes.AdminCalificacionController.USUARIO_ERROR_NO_HAY_SESION,
                CatalogoMensajes.AdminCalificacionController.USUARIO_ERROR_SOLO_ADMINISTRADORES);
    }
}
