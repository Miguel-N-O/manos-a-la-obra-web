package com.manosalaobra.controlador.respuesta;

import java.util.ArrayList;
import java.util.List;

public final class RespuestaError<D> extends Respuesta<D> {

    private RespuestaError(List<String> mensajes) {
        super(new ArrayList<>(), mensajes, EstadoRespuesta.ERROR);
    }

    public static <D> RespuestaError<D> crear(String mensaje) {
        return new RespuestaError<>(List.of(mensaje));
    }

    public static <D> RespuestaError<D> crear(List<String> mensajes) {
        return new RespuestaError<>(mensajes);
    }
}
