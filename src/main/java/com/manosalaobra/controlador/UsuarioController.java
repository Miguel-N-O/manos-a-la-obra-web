package com.manosalaobra.controlador;

import com.manosalaobra.controlador.respuesta.Respuesta;
import com.manosalaobra.controlador.respuesta.RespuestaOk;
import com.manosalaobra.dto.TecnicoDTO;
import com.manosalaobra.dto.UsuarioDTO;
import com.manosalaobra.negocio.fachada.UsuarioFachada;
import com.manosalaobra.negocio.fachada.impl.UsuarioFachadaImpl;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController extends AbstractRestController {

    @PostMapping("/ciudadanos")
    public ResponseEntity<Respuesta<UsuarioDTO>> registrarCiudadano(@RequestBody UsuarioDTO usuario) {
        UsuarioFachada usuarioFachada = new UsuarioFachadaImpl();
        UsuarioDTO ciudadanoRegistrado = usuarioFachada.registrarCiudadano(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(RespuestaOk.crear(
                ciudadanoRegistrado, CatalogoMensajes.UsuarioController.USUARIO_CIUDADANO_REGISTRADO));
    }

    @PostMapping("/tecnicos")
    public ResponseEntity<Respuesta<UsuarioDTO>> registrarTecnico(@RequestBody TecnicoDTO tecnico) {
        UsuarioFachada usuarioFachada = new UsuarioFachadaImpl();
        UsuarioDTO tecnicoRegistrado = usuarioFachada.registrarTecnico(tecnico);
        return ResponseEntity.status(HttpStatus.CREATED).body(RespuestaOk.crear(
                tecnicoRegistrado, CatalogoMensajes.UsuarioController.USUARIO_TECNICO_REGISTRADO));
    }
}
