package com.manosalaobra.negocio.negocio.reglas.impl.tecnico;

import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.util.ArrayList;
import java.util.List;

// HU-02: revisa los datos propios del técnico. El formato de nombre, correo, teléfono y
// contraseña lo revisa ValidarDatosRegistrarNuevoUsuarioRegla, igual que para el ciudadano.
public final class ValidarDatosRegistrarNuevoTecnicoRegla implements Regla<TecnicoDominio> {

    private static final Regla<TecnicoDominio> INSTANCIA = new ValidarDatosRegistrarNuevoTecnicoRegla();

    private static final int LONGITUD_MAXIMA_DOCUMENTO = 30;

    private ValidarDatosRegistrarNuevoTecnicoRegla() {
    }

    public static Regla<TecnicoDominio> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    @SafeVarargs
    public final void ejecutar(TecnicoDominio... datos) {
        TecnicoDominio tecnico = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                datos.length > 0 ? datos[0] : null, new TecnicoDominio.Builder().build());
        asegurarQueNoFaltenCampos(tecnico);
        asegurarTipoDocumentoValido(tecnico.getTipoDocumento());
        asegurarDocumentoValido(tecnico.getDocumento());
    }

    // Lista en un solo mensaje todo lo que falta, también los datos de la cuenta (HU-02 CA-03)
    private void asegurarQueNoFaltenCampos(TecnicoDominio tecnico) {
        UtilTexto utilTexto = UtilTexto.getUtilTexto();
        UsuarioDominio usuario = tecnico.getUsuario();
        List<String> faltantes = new ArrayList<>();
        if (utilTexto.esVacia(usuario.getNombre())) {
            faltantes.add(CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CAMPO_NOMBRE);
        }
        if (utilTexto.esVacia(tecnico.getTipoDocumento())) {
            faltantes.add(CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CAMPO_TIPO_DOCUMENTO);
        }
        if (utilTexto.esVacia(tecnico.getDocumento())) {
            faltantes.add(CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CAMPO_DOCUMENTO);
        }
        if (utilTexto.esVacia(usuario.getCorreo())) {
            faltantes.add(CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CAMPO_CORREO);
        }
        if (utilTexto.esVacia(usuario.getClave())) {
            faltantes.add(CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CAMPO_CLAVE);
        }
        if (UtilUUID.esValorDefecto(usuario.getIndicativoPais().getId())) {
            faltantes.add(CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CAMPO_INDICATIVO_PAIS);
        }
        if (utilTexto.esVacia(usuario.getNumeroTelefono())) {
            faltantes.add(CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CAMPO_NUMERO_TELEFONO);
        }
        if (!faltantes.isEmpty()) {
            throw ManosALaObraNegocioExcepcion.crear(String.format(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CAMPOS_FALTANTES, String.join(", ", faltantes)));
        }
    }

    private void asegurarTipoDocumentoValido(String tipoDocumento) {
        if (!TecnicoDominio.CEDULA_CIUDADANIA.equals(tipoDocumento)
                && !TecnicoDominio.CEDULA_EXTRANJERIA.equals(tipoDocumento)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_TIPO_DOCUMENTO_NO_VALIDO);
        }
    }

    private void asegurarDocumentoValido(String documento) {
        UtilTexto utilTexto = UtilTexto.getUtilTexto();
        if (!utilTexto.formatoEsValido(documento, UtilTexto.LETRAS_DIGITOS)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_DOCUMENTO_FORMATO_NO_VALIDO);
        }
        if (!utilTexto.obtenerLongitudCadenaEsValida(documento, 1, LONGITUD_MAXIMA_DOCUMENTO, true)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_DOCUMENTO_LONGITUD_NO_VALIDA);
        }
    }
}
