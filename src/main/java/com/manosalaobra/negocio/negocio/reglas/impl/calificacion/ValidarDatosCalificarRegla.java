package com.manosalaobra.negocio.negocio.reglas.impl.calificacion;

import com.manosalaobra.dominio.CalificacionDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

// HU-14: de 1 a 5 estrellas. HU-15: el comentario es opcional, pero si se escribe tiene de 10 a 300 caracteres
public final class ValidarDatosCalificarRegla implements Regla<CalificacionDominio> {

    private static final Regla<CalificacionDominio> INSTANCIA = new ValidarDatosCalificarRegla();

    private static final int ESTRELLAS_MINIMAS = 1;
    private static final int ESTRELLAS_MAXIMAS = 5;
    private static final int COMENTARIO_LONGITUD_MINIMA = 10;
    private static final int COMENTARIO_LONGITUD_MAXIMA = 300;

    private ValidarDatosCalificarRegla() {
    }

    public static Regla<CalificacionDominio> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    @SafeVarargs
    public final void ejecutar(CalificacionDominio... datos) {
        CalificacionDominio calificacion = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                datos.length > 0 ? datos[0] : null, new CalificacionDominio.Builder().build());
        if (UtilUUID.esValorDefecto(calificacion.getSolicitud().getId())) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.CalificacionNegocioImpl.USUARIO_ERROR_SOLICITUD_NO_INDICADA);
        }
        if (calificacion.getEstrellas() < ESTRELLAS_MINIMAS || calificacion.getEstrellas() > ESTRELLAS_MAXIMAS) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.CalificacionNegocioImpl.USUARIO_ERROR_ESTRELLAS_NO_VALIDAS);
        }
        int longitudComentario = UtilTexto.getUtilTexto().obtenerLongitudCadena(calificacion.getComentario());
        if (longitudComentario > 0 && (longitudComentario < COMENTARIO_LONGITUD_MINIMA
                || longitudComentario > COMENTARIO_LONGITUD_MAXIMA)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.CalificacionNegocioImpl.USUARIO_ERROR_COMENTARIO_LONGITUD_NO_VALIDA);
        }
    }
}
