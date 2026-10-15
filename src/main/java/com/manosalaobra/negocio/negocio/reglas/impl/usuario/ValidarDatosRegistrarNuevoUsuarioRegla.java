package com.manosalaobra.negocio.negocio.reglas.impl.usuario;

import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;

import java.util.ArrayList;
import java.util.List;

public final class ValidarDatosRegistrarNuevoUsuarioRegla implements Regla<UsuarioDominio> {

    private static final Regla<UsuarioDominio> INSTANCIA = new ValidarDatosRegistrarNuevoUsuarioRegla();

    private static final int LONGITUD_MAXIMA_NOMBRE = 100;
    private static final int LONGITUD_MAXIMA_CORREO = 150;
    private static final int LONGITUD_MINIMA_CLAVE = 8;
    private static final int LONGITUD_MAXIMA_TELEFONO = 20;

    private ValidarDatosRegistrarNuevoUsuarioRegla() {
    }

    public static Regla<UsuarioDominio> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    @SafeVarargs
    public final void ejecutar(UsuarioDominio... datos) {
        UsuarioDominio usuario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                datos.length > 0 ? datos[0] : null, new UsuarioDominio.Builder().build());

        asegurarQueNoFaltenCampos(usuario);
        asegurarNombreValido(usuario.getNombre());
        asegurarCorreoValido(usuario.getCorreo());
        asegurarClaveValida(usuario.getClave());
        asegurarTelefonoValido(usuario.getTelefono());
    }

    private void asegurarQueNoFaltenCampos(UsuarioDominio usuario) {
        UtilTexto utilTexto = UtilTexto.getUtilTexto();
        List<String> faltantes = new ArrayList<>();
        if (utilTexto.esVacia(usuario.getNombre())) {
            faltantes.add(CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CAMPO_NOMBRE);
        }
        if (utilTexto.esVacia(usuario.getCorreo())) {
            faltantes.add(CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CAMPO_CORREO);
        }
        if (utilTexto.esVacia(usuario.getClave())) {
            faltantes.add(CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CAMPO_CLAVE);
        }
        if (!faltantes.isEmpty()) {
            throw ManosALaObraNegocioExcepcion.crear(String.format(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CAMPOS_FALTANTES, String.join(", ", faltantes)));
        }
    }

    private void asegurarNombreValido(String nombre) {
        if (!UtilTexto.getUtilTexto().obtenerLongitudCadenaEsValida(nombre, 1, LONGITUD_MAXIMA_NOMBRE, true)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_NOMBRE_LONGITUD_NO_VALIDA);
        }
    }

    private void asegurarCorreoValido(String correo) {
        UtilTexto utilTexto = UtilTexto.getUtilTexto();
        if (!utilTexto.formatoEsValido(correo, UtilTexto.CORREO)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CORREO_FORMATO_NO_VALIDO);
        }
        if (!utilTexto.obtenerLongitudCadenaEsValida(correo, 1, LONGITUD_MAXIMA_CORREO, true)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CORREO_LONGITUD_NO_VALIDA);
        }
    }

    private void asegurarClaveValida(String clave) {
        if (UtilTexto.getUtilTexto().obtenerLongitudCadena(clave) < LONGITUD_MINIMA_CLAVE) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CLAVE_LONGITUD_NO_VALIDA);
        }
    }

    private void asegurarTelefonoValido(String telefono) {
        if (UtilTexto.getUtilTexto().obtenerLongitudCadena(telefono, true) > LONGITUD_MAXIMA_TELEFONO) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_TELEFONO_LONGITUD_NO_VALIDA);
        }
    }
}
