package com.manosalaobra.transversal.excepciones;

import com.manosalaobra.transversal.excepciones.enums.Capa;

public final class ManosALaObraDatosExcepcion extends ManosALaObraExcepcion {

    private static final long serialVersionUID = 1L;

    private ManosALaObraDatosExcepcion(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        super(Capa.DATOS, mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }

    public static ManosALaObraExcepcion crear(String mensajeUsuario) {
        return new ManosALaObraDatosExcepcion(mensajeUsuario, mensajeUsuario, new Exception(mensajeUsuario));
    }

    public static ManosALaObraExcepcion crear(String mensajeUsuario, String mensajeTecnico) {
        return new ManosALaObraDatosExcepcion(mensajeUsuario, mensajeTecnico, new Exception(mensajeTecnico));
    }

    public static ManosALaObraExcepcion crear(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        return new ManosALaObraDatosExcepcion(mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }
}
