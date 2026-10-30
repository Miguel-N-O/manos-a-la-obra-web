package com.manosalaobra.controlador;

import com.manosalaobra.controlador.respuesta.Respuesta;
import com.manosalaobra.controlador.respuesta.RespuestaError;
import com.manosalaobra.controlador.respuesta.RespuestaOk;
import com.manosalaobra.controlador.seguridad.SesionUsuario;
import com.manosalaobra.dominio.RolDominio;
import com.manosalaobra.dto.SolicitudDTO;
import com.manosalaobra.negocio.fachada.SolicitudFachada;
import com.manosalaobra.negocio.fachada.impl.SolicitudFachadaImpl;
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
@RequestMapping("/api/solicitudes")
public class SolicitudController extends AbstractRestController {

    // HU-09: el ciudadano envía la solicitud a un técnico y queda PENDIENTE
    @PostMapping
    public ResponseEntity<Respuesta<SolicitudDTO>> crear(@RequestBody SolicitudDTO solicitud,
            HttpServletRequest peticion) {
        ResponseEntity<Respuesta<SolicitudDTO>> sinPermiso = validarSesionDeCiudadano(peticion);
        if (sinPermiso != null) {
            return sinPermiso;
        }
        SolicitudFachada solicitudFachada = new SolicitudFachadaImpl();
        SolicitudDTO creada = solicitudFachada.crear(SesionUsuario.idActual(peticion), solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(RespuestaOk.crear(
                creada, CatalogoMensajes.SolicitudController.USUARIO_SOLICITUD_ENVIADA));
    }

    // CU19: Mis solicitudes, de la más reciente a la más antigua
    @GetMapping("/mias")
    public ResponseEntity<Respuesta<SolicitudDTO>> consultarMias(HttpServletRequest peticion) {
        ResponseEntity<Respuesta<SolicitudDTO>> sinPermiso = validarSesionDeCiudadano(peticion);
        if (sinPermiso != null) {
            return sinPermiso;
        }
        SolicitudFachada solicitudFachada = new SolicitudFachadaImpl();
        List<SolicitudDTO> solicitudes = solicitudFachada.consultarDelCiudadano(SesionUsuario.idActual(peticion));
        if (solicitudes.isEmpty()) {
            return ResponseEntity.ok(RespuestaOk.crear(solicitudes,
                    List.of(CatalogoMensajes.SolicitudController.USUARIO_SIN_SOLICITUDES)));
        }
        return ResponseEntity.ok(RespuestaOk.crear(solicitudes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Respuesta<SolicitudDTO>> consultarUna(@PathVariable("id") UUID id,
            HttpServletRequest peticion) {
        ResponseEntity<Respuesta<SolicitudDTO>> sinPermiso = validarSesionDeCiudadano(peticion);
        if (sinPermiso != null) {
            return sinPermiso;
        }
        SolicitudFachada solicitudFachada = new SolicitudFachadaImpl();
        return ResponseEntity.ok(RespuestaOk.crear(
                List.of(solicitudFachada.consultarUnaDelCiudadano(SesionUsuario.idActual(peticion), id))));
    }

    private ResponseEntity<Respuesta<SolicitudDTO>> validarSesionDeCiudadano(HttpServletRequest peticion) {
        String rol = SesionUsuario.rolActual(peticion);
        if (rol == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RespuestaError.crear(
                    CatalogoMensajes.SolicitudController.USUARIO_ERROR_NO_HAY_SESION));
        }
        if (!RolDominio.CIUDADANO.equals(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RespuestaError.crear(
                    CatalogoMensajes.SolicitudController.USUARIO_ERROR_SOLO_CIUDADANOS));
        }
        return null;
    }
}
