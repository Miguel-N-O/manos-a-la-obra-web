package com.manosalaobra.negocio.negocio.reglas.impl.usuario;

import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilTexto;

public final class ValidarDatosIniciarSesionRegla implements Regla<UsuarioDominio> {

    private static final Regla<UsuarioDominio> INSTANCIA = new ValidarDatosIniciarSesionRegla();

    private ValidarDatosIniciarSesionRegla() {
    }

    public static Regla<UsuarioDominio> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(UsuarioDominio... datos) {
        UsuarioDominio usuario = datos[0];
        if (UtilTexto.getUtilTexto().esVacia(usuario.getCorreo())
                || UtilTexto.getUtilTexto().esVacia(usuario.getClave())) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_DATOS_INICIO_SESION_FALTANTES);
        }
    }
}
