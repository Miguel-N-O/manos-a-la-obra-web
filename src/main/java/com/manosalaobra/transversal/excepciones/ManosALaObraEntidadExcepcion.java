package com.manosalaobra.transversal.excepciones;

import com.manosalaobra.transversal.excepciones.enums.Capa;

public final class ManosALaObraEntidadExcepcion extends ManosALaObraExcepcion {

    private static final long serialVersionUID = 1L;

    private ManosALaObraEntidadExcepcion(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        super(Capa.ENTIDAD, mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }

    public static ManosALaObraExcepcion crear(String mensajeUsuario) {
        return new ManosALaObraEntidadExcepcion(mensajeUsuario, mensajeUsuario, new Exception(mensajeUsuario));
    }

    public static ManosALaObraExcepcion crear(String mensajeUsuario, String mensajeTecnico) {
        return new ManosALaObraEntidadExcepcion(mensajeUsuario, mensajeTecnico, new Exception(mensajeTecnico));
    }

    public static ManosALaObraExcepcion crear(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        return new ManosALaObraEntidadExcepcion(mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }
}
