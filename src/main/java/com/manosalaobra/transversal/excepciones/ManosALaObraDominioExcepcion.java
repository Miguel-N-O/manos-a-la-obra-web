package com.manosalaobra.transversal.excepciones;

import com.manosalaobra.transversal.excepciones.enums.Capa;

public final class ManosALaObraDominioExcepcion extends ManosALaObraExcepcion {

    private static final long serialVersionUID = 1L;

    private ManosALaObraDominioExcepcion(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        super(Capa.DOMINIO, mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }

    public static ManosALaObraExcepcion crear(String mensajeUsuario) {
        return new ManosALaObraDominioExcepcion(mensajeUsuario, mensajeUsuario, new Exception(mensajeUsuario));
    }

    public static ManosALaObraExcepcion crear(String mensajeUsuario, String mensajeTecnico) {
        return new ManosALaObraDominioExcepcion(mensajeUsuario, mensajeTecnico, new Exception(mensajeTecnico));
    }

    public static ManosALaObraExcepcion crear(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        return new ManosALaObraDominioExcepcion(mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }
}
