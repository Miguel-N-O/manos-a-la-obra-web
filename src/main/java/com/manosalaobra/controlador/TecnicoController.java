package com.manosalaobra.controlador;

import com.manosalaobra.controlador.respuesta.Respuesta;
import com.manosalaobra.controlador.respuesta.RespuestaError;
import com.manosalaobra.controlador.respuesta.RespuestaOk;
import com.manosalaobra.controlador.seguridad.SesionUsuario;
import com.manosalaobra.dominio.RolDominio;
import com.manosalaobra.dto.TecnicoDTO;
import com.manosalaobra.negocio.fachada.TecnicoFachada;
import com.manosalaobra.negocio.fachada.impl.TecnicoFachadaImpl;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tecnicos")
public class TecnicoController extends AbstractRestController {

    @GetMapping
    public ResponseEntity<Respuesta<TecnicoDTO>> consultarTecnicos(
            @RequestParam(name = "servicioId", required = false) UUID servicioId,
            @RequestParam(name = "barrioId", required = false) UUID barrioId,
            HttpServletRequest peticion) {
        String rol = SesionUsuario.rolActual(peticion);
        if (rol == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RespuestaError.crear(
                    CatalogoMensajes.TecnicoController.USUARIO_ERROR_NO_HAY_SESION));
        }
        if (!RolDominio.CIUDADANO.equals(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RespuestaError.crear(
                    CatalogoMensajes.TecnicoController.USUARIO_ERROR_SOLO_CIUDADANOS));
        }

        TecnicoDTO filtro = new TecnicoDTO();
        filtro.getServicio().setId(servicioId);
        filtro.getBarrio().setId(barrioId);

        TecnicoFachada tecnicoFachada = new TecnicoFachadaImpl();
        List<TecnicoDTO> tecnicos = tecnicoFachada.consultarPorFiltro(filtro);
        if (tecnicos.isEmpty()) {
            return ResponseEntity.ok(RespuestaOk.crear(tecnicos,
                    List.of(CatalogoMensajes.TecnicoController.USUARIO_TECNICOS_NO_ENCONTRADOS)));
        }
        return ResponseEntity.ok(RespuestaOk.crear(tecnicos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Respuesta<TecnicoDTO>> consultarPerfil(@PathVariable("id") UUID id,
            HttpServletRequest peticion) {
        String rol = SesionUsuario.rolActual(peticion);
        if (rol == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RespuestaError.crear(
                    CatalogoMensajes.TecnicoController.USUARIO_ERROR_NO_HAY_SESION_PERFIL));
        }
        if (!RolDominio.CIUDADANO.equals(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RespuestaError.crear(
                    CatalogoMensajes.TecnicoController.USUARIO_ERROR_SOLO_CIUDADANOS_PERFIL));
        }

        TecnicoFachada tecnicoFachada = new TecnicoFachadaImpl();
        return ResponseEntity.ok(RespuestaOk.crear(List.of(tecnicoFachada.consultarPorId(id))));
    }

    // HU-04: el técnico consulta su propio perfil (vacío si aún no lo completa)
    @GetMapping("/mi-perfil")
    public ResponseEntity<Respuesta<TecnicoDTO>> consultarPerfilPropio(HttpServletRequest peticion) {
        ResponseEntity<Respuesta<TecnicoDTO>> sinPermiso = validarSesionDeTecnico(peticion);
        if (sinPermiso != null) {
            return sinPermiso;
        }
        TecnicoFachada tecnicoFachada = new TecnicoFachadaImpl();
        return ResponseEntity.ok(RespuestaOk.crear(
                List.of(tecnicoFachada.consultarPerfilPropio(SesionUsuario.idActual(peticion)))));
    }

    // HU-04: el técnico guarda su perfil por primera vez o lo actualiza
    @PutMapping("/mi-perfil")
    public ResponseEntity<Respuesta<TecnicoDTO>> guardarPerfilPropio(@RequestBody TecnicoDTO perfil,
            HttpServletRequest peticion) {
        ResponseEntity<Respuesta<TecnicoDTO>> sinPermiso = validarSesionDeTecnico(peticion);
        if (sinPermiso != null) {
            return sinPermiso;
        }
        UUID usuarioId = SesionUsuario.idActual(peticion);
        // CU04: el mensaje cambia si es la primera vez que guarda el perfil o si lo está actualizando.
        // Cada fachada cierra su conexión al terminar, por eso se crea una para cada llamada.
        boolean teniaPerfil = !UtilUUID.esValorDefecto(
                new TecnicoFachadaImpl().consultarPerfilPropio(usuarioId).getServicio().getId());
        TecnicoDTO perfilGuardado = new TecnicoFachadaImpl().guardarPerfilPropio(usuarioId, perfil);
        String mensaje = teniaPerfil
                ? CatalogoMensajes.TecnicoController.USUARIO_PERFIL_ACTUALIZADO
                : CatalogoMensajes.TecnicoController.USUARIO_PERFIL_GUARDADO;
        return ResponseEntity.ok(RespuestaOk.crear(perfilGuardado, mensaje));
    }

    private ResponseEntity<Respuesta<TecnicoDTO>> validarSesionDeTecnico(HttpServletRequest peticion) {
        String rol = SesionUsuario.rolActual(peticion);
        if (rol == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RespuestaError.crear(
                    CatalogoMensajes.TecnicoController.USUARIO_ERROR_NO_HAY_SESION_PERFIL_PROPIO));
        }
        if (!RolDominio.TECNICO.equals(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RespuestaError.crear(
                    CatalogoMensajes.TecnicoController.USUARIO_ERROR_SOLO_TECNICOS_PERFIL_PROPIO));
        }
        return null;
    }
}
