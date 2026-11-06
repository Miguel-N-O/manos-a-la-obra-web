package com.manosalaobra.controlador;

import com.manosalaobra.controlador.respuesta.Respuesta;
import com.manosalaobra.controlador.respuesta.RespuestaError;
import com.manosalaobra.controlador.respuesta.RespuestaOk;
import com.manosalaobra.controlador.seguridad.AccesoPorRol;
import com.manosalaobra.controlador.seguridad.SesionUsuario;
import com.manosalaobra.dominio.RolDominio;
import com.manosalaobra.dto.CalificacionDTO;
import com.manosalaobra.negocio.fachada.CalificacionFachada;
import com.manosalaobra.negocio.fachada.impl.CalificacionFachadaImpl;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController extends AbstractRestController {

    // HU-14 y HU-15: el ciudadano califica un servicio finalizado. Cuerpo:
    // { "solicitud": { "id": "..." }, "estrellas": 5, "comentario": "opcional" }
    @PostMapping
    public ResponseEntity<Respuesta<CalificacionDTO>> calificar(@RequestBody CalificacionDTO calificacion,
            HttpServletRequest peticion) {
        ResponseEntity<Respuesta<CalificacionDTO>> sinPermiso = AccesoPorRol.exigir(peticion, RolDominio.CIUDADANO,
                CatalogoMensajes.CalificacionController.USUARIO_ERROR_NO_HAY_SESION,
                CatalogoMensajes.CalificacionController.USUARIO_ERROR_SOLO_CIUDADANOS);
        if (sinPermiso != null) {
            return sinPermiso;
        }
        CalificacionFachada calificacionFachada = new CalificacionFachadaImpl();
        CalificacionDTO registrada = calificacionFachada.calificar(SesionUsuario.idActual(peticion), calificacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(RespuestaOk.crear(
                registrada, CatalogoMensajes.CalificacionController.USUARIO_CALIFICACION_REGISTRADA));
    }

    // Calificaciones y comentarios visibles en el perfil de un técnico (cualquier usuario con sesión)
    @GetMapping("/tecnico/{tecnicoId}")
    public ResponseEntity<Respuesta<CalificacionDTO>> consultarDelTecnico(@PathVariable("tecnicoId") UUID tecnicoId,
            HttpServletRequest peticion) {
        if (SesionUsuario.rolActual(peticion) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RespuestaError.crear(
                    CatalogoMensajes.CalificacionController.USUARIO_ERROR_NO_HAY_SESION));
        }
        CalificacionFachada calificacionFachada = new CalificacionFachadaImpl();
        List<CalificacionDTO> calificaciones = calificacionFachada.consultarDelTecnico(tecnicoId);
        if (calificaciones.isEmpty()) {
            return ResponseEntity.ok(RespuestaOk.crear(calificaciones,
                    List.of(CatalogoMensajes.CalificacionController.USUARIO_SIN_CALIFICACIONES)));
        }
        return ResponseEntity.ok(RespuestaOk.crear(calificaciones));
    }
}
