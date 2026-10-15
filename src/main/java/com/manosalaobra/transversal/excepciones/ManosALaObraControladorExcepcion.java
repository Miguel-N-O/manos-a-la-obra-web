package com.manosalaobra.transversal.excepciones;

import com.manosalaobra.transversal.excepciones.enums.Capa;

public final class ManosALaObraControladorExcepcion extends ManosALaObraExcepcion {

    private static final long serialVersionUID = 1L;

    private ManosALaObraControladorExcepcion(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        super(Capa.CONTROLADOR, mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }

    public static ManosALaObraExcepcion crear(String mensajeUsuario) {
        return new ManosALaObraControladorExcepcion(mensajeUsuario, mensajeUsuario, new Exception(mensajeUsuario));
    }

    public static ManosALaObraExcepcion crear(String mensajeUsuario, String mensajeTecnico) {
        return new ManosALaObraControladorExcepcion(mensajeUsuario, mensajeTecnico, new Exception(mensajeTecnico));
    }

    public static ManosALaObraExcepcion crear(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        return new ManosALaObraControladorExcepcion(mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }
}
