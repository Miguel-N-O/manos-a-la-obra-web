package com.manosalaobra.negocio.negocio.reglas.impl.solicitud;

import com.manosalaobra.dominio.SolicitudDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilFecha;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.util.ArrayList;
import java.util.List;

// HU-09 CA-03 y CA-04: datos obligatorios, descripción de 10 a 500 caracteres y fecha futura
public final class ValidarDatosCrearSolicitudRegla implements Regla<SolicitudDominio> {

    private static final Regla<SolicitudDominio> INSTANCIA = new ValidarDatosCrearSolicitudRegla();

    private static final int LONGITUD_MINIMA_DESCRIPCION = 10;
    private static final int LONGITUD_MAXIMA_DESCRIPCION = 500;

    private ValidarDatosCrearSolicitudRegla() {
    }

    public static Regla<SolicitudDominio> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    @SafeVarargs
    public final void ejecutar(SolicitudDominio... datos) {
        SolicitudDominio solicitud = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                datos.length > 0 ? datos[0] : null, new SolicitudDominio.Builder().build());
        asegurarQueNoFaltenCampos(solicitud);
        asegurarDescripcionValida(solicitud.getDescripcion());
        asegurarFechaHoraFutura(solicitud);
    }

    private void asegurarQueNoFaltenCampos(SolicitudDominio solicitud) {
        List<String> faltantes = new ArrayList<>();
        if (UtilUUID.esValorDefecto(solicitud.getTecnico().getId())) {
            faltantes.add(CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_CAMPO_TECNICO);
        }
        if (UtilTexto.getUtilTexto().esVacia(solicitud.getDescripcion())) {
            faltantes.add(CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_CAMPO_DESCRIPCION);
        }
        if (UtilUUID.esValorDefecto(solicitud.getBarrio().getId())) {
            faltantes.add(CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_CAMPO_BARRIO);
        }
        if (UtilFecha.esValorDefecto(solicitud.getFechaHora())) {
            faltantes.add(CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_CAMPO_FECHA_HORA);
        }
        if (!faltantes.isEmpty()) {
            throw ManosALaObraNegocioExcepcion.crear(String.format(
                    CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_CAMPOS_FALTANTES,
                    String.join(", ", faltantes)));
        }
    }

    private void asegurarDescripcionValida(String descripcion) {
        if (!UtilTexto.getUtilTexto().obtenerLongitudCadenaEsValida(descripcion,
                LONGITUD_MINIMA_DESCRIPCION, LONGITUD_MAXIMA_DESCRIPCION, true)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_DESCRIPCION_LONGITUD_NO_VALIDA);
        }
    }

    private void asegurarFechaHoraFutura(SolicitudDominio solicitud) {
        if (!UtilFecha.estaEnElFuturo(solicitud.getFechaHora())) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_FECHA_HORA_NO_FUTURA);
        }
    }
}
