package com.manosalaobra.controlador.respuesta;

import com.manosalaobra.transversal.utilitarios.UtilFecha;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Respuesta<D> {

    private final String correlationId;
    private final LocalDateTime timestamp;
    private List<D> datos;
    private List<String> mensajes;
    private EstadoRespuesta estado;

    protected Respuesta(List<D> datos, List<String> mensajes, EstadoRespuesta estado) {
        setDatos(datos);
        setMensajes(mensajes);
        setEstado(estado);
        this.timestamp = UtilFecha.obtenerFechaHoraActual();
        this.correlationId = UtilUUID.generar().toString();
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<D> getDatos() {
        return datos;
    }

    public List<String> getMensajes() {
        return mensajes;
    }

    public EstadoRespuesta getEstado() {
        return estado;
    }

    private void setDatos(List<D> datos) {
        this.datos = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(datos, new ArrayList<>());
    }

    private void setMensajes(List<String> mensajes) {
        this.mensajes = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(mensajes, new ArrayList<>());
    }

    private void setEstado(EstadoRespuesta estado) {
        this.estado = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(estado, EstadoRespuesta.ERROR);
    }
}
