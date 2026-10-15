package com.manosalaobra.controlador;

import com.manosalaobra.controlador.respuesta.Respuesta;
import com.manosalaobra.controlador.respuesta.RespuestaOk;
import com.manosalaobra.dto.ServicioDTO;
import com.manosalaobra.negocio.fachada.ServicioFachada;
import com.manosalaobra.negocio.fachada.impl.ServicioFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController extends AbstractRestController {

    @GetMapping
    public ResponseEntity<Respuesta<ServicioDTO>> consultarServicios() {
        ServicioFachada servicioFachada = new ServicioFachadaImpl();
        return ResponseEntity.status(HttpStatus.OK).body(RespuestaOk.crear(servicioFachada.consultarServicios()));
    }
}
