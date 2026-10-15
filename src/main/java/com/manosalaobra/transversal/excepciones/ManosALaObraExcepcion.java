package com.manosalaobra.transversal.excepciones;

import com.manosalaobra.transversal.excepciones.enums.Capa;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;

public class ManosALaObraExcepcion extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Capa capa;
    private String mensajeUsuario;
    private String mensajeTecnico;
    private Exception excepcionRaiz;

    protected ManosALaObraExcepcion(Capa capa, String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        super(UtilTexto.getUtilTexto().obtenerValorDefecto(mensajeTecnico, mensajeUsuario), excepcionRaiz);
        setCapa(capa);
        setMensajeUsuario(mensajeUsuario);
        setMensajeTecnico(mensajeTecnico);
        setExcepcionRaiz(excepcionRaiz);
    }

    public Capa getCapa() {
        return capa;
    }

    public String getMensajeUsuario() {
        return mensajeUsuario;
    }

    public String getMensajeTecnico() {
        return mensajeTecnico;
    }

    public Exception getExcepcionRaiz() {
        return excepcionRaiz;
    }

    private void setCapa(Capa capa) {
        this.capa = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(capa, Capa.GENERAL);
    }

    private void setMensajeUsuario(String mensajeUsuario) {
        this.mensajeUsuario = UtilTexto.getUtilTexto().obtenerValorDefecto(mensajeUsuario);
    }

    private void setMensajeTecnico(String mensajeTecnico) {
        this.mensajeTecnico = UtilTexto.getUtilTexto().obtenerValorDefecto(mensajeTecnico, getMensajeUsuario());
    }

    private void setExcepcionRaiz(Exception excepcionRaiz) {
        this.excepcionRaiz = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                excepcionRaiz, new Exception(getMensajeTecnico()));
    }
}
