package com.manosalaobra.controlador;

import com.manosalaobra.controlador.respuesta.RespuestaError;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public abstract class AbstractRestController {

    private static final Logger REGISTRO = LoggerFactory.getLogger(AbstractRestController.class);

    @ExceptionHandler(ManosALaObraExcepcion.class)
    public ResponseEntity<RespuestaError<String>> manejarExcepcionPropia(ManosALaObraExcepcion excepcion) {
        REGISTRO.warn("[{}] {}", excepcion.getCapa(), excepcion.getMensajeTecnico());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(RespuestaError.crear(excepcion.getMensajeUsuario()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RespuestaError<String>> manejarCuerpoInvalido(HttpMessageNotReadableException excepcion) {
        REGISTRO.warn("Cuerpo de la petición ilegible: {}", excepcion.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RespuestaError.crear(
                CatalogoMensajes.AbstractRestController.USUARIO_ERROR_CUERPO_DE_LA_PETICION_INVALIDO));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<RespuestaError<String>> manejarRecursoNoEncontrado(NoResourceFoundException excepcion) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(RespuestaError.crear(
                CatalogoMensajes.AbstractRestController.USUARIO_ERROR_RECURSO_NO_ENCONTRADO));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespuestaError<String>> manejarExcepcionInesperada(Exception excepcion) {
        REGISTRO.error("Problema inesperado atendiendo la petición", excepcion);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RespuestaError.crear(
                CatalogoMensajes.AbstractRestController.USUARIO_ERROR_PROBLEMA_INESPERADO));
    }
}
