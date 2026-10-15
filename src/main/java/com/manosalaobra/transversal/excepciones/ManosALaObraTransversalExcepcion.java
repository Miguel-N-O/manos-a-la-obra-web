package com.manosalaobra.transversal.excepciones;

import com.manosalaobra.transversal.excepciones.enums.Capa;

public final class ManosALaObraTransversalExcepcion extends ManosALaObraExcepcion {

    private static final long serialVersionUID = 1L;

    private ManosALaObraTransversalExcepcion(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        super(Capa.TRANSVERSAL, mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }

    public static ManosALaObraExcepcion crear(String mensajeUsuario) {
        return new ManosALaObraTransversalExcepcion(mensajeUsuario, mensajeUsuario, new Exception(mensajeUsuario));
    }

    public static ManosALaObraExcepcion crear(String mensajeUsuario, String mensajeTecnico) {
        return new ManosALaObraTransversalExcepcion(mensajeUsuario, mensajeTecnico, new Exception(mensajeTecnico));
    }

    public static ManosALaObraExcepcion crear(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        return new ManosALaObraTransversalExcepcion(mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }
}
