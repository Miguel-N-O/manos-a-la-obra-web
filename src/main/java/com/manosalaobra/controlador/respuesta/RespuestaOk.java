package com.manosalaobra.controlador.respuesta;

import java.util.ArrayList;
import java.util.List;

public final class RespuestaOk<D> extends Respuesta<D> {

    private RespuestaOk(List<D> datos, List<String> mensajes) {
        super(datos, mensajes, EstadoRespuesta.OK);
    }

    public static <D> RespuestaOk<D> crear(List<D> datos, List<String> mensajes) {
        return new RespuestaOk<>(datos, mensajes);
    }

    public static <D> RespuestaOk<D> crear(List<D> datos) {
        return new RespuestaOk<>(datos, new ArrayList<>());
    }

    public static <D> RespuestaOk<D> crear(D dato, String mensaje) {
        return new RespuestaOk<>(List.of(dato), List.of(mensaje));
    }

    public static <D> RespuestaOk<D> crear() {
        return new RespuestaOk<>(new ArrayList<>(), new ArrayList<>());
    }
}
