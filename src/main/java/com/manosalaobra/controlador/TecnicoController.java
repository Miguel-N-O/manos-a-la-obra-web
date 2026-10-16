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
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
