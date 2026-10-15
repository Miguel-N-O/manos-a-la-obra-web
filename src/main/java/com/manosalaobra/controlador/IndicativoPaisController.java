package com.manosalaobra.controlador;

import com.manosalaobra.controlador.respuesta.Respuesta;
import com.manosalaobra.controlador.respuesta.RespuestaOk;
import com.manosalaobra.dto.IndicativoPaisDTO;
import com.manosalaobra.negocio.fachada.IndicativoPaisFachada;
import com.manosalaobra.negocio.fachada.impl.IndicativoPaisFachadaImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/indicativos-pais")
public class IndicativoPaisController extends AbstractRestController {

    @GetMapping
    public ResponseEntity<Respuesta<IndicativoPaisDTO>> consultarIndicativosPais() {
        IndicativoPaisFachada indicativoPaisFachada = new IndicativoPaisFachadaImpl();
        return ResponseEntity.status(HttpStatus.OK).body(
                RespuestaOk.crear(indicativoPaisFachada.consultarIndicativosPais()));
    }
}
