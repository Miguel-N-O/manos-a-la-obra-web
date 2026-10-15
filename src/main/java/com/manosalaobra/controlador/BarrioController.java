package com.manosalaobra.controlador;

import com.manosalaobra.controlador.respuesta.Respuesta;
import com.manosalaobra.controlador.respuesta.RespuestaOk;
import com.manosalaobra.dto.BarrioDTO;
import com.manosalaobra.negocio.fachada.BarrioFachada;
import com.manosalaobra.negocio.fachada.impl.BarrioFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/barrios")
public class BarrioController extends AbstractRestController {

    @GetMapping
    public ResponseEntity<Respuesta<BarrioDTO>> consultarBarrios() {
        BarrioFachada barrioFachada = new BarrioFachadaImpl();
        return ResponseEntity.status(HttpStatus.OK).body(RespuestaOk.crear(barrioFachada.consultarBarrios()));
    }
}
