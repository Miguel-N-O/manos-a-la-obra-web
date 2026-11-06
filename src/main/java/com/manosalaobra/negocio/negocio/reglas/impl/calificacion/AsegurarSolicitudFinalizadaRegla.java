package com.manosalaobra.negocio.negocio.reglas.impl.calificacion;

import com.manosalaobra.dominio.EstadoSolicitudDominio;
import com.manosalaobra.dominio.SolicitudDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

// HU-14: solo se califica un servicio que ya fue finalizado
public final class AsegurarSolicitudFinalizadaRegla implements Regla<SolicitudDominio> {

    private static final Regla<SolicitudDominio> INSTANCIA = new AsegurarSolicitudFinalizadaRegla();

    private AsegurarSolicitudFinalizadaRegla() {
    }

    public static Regla<SolicitudDominio> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    @SafeVarargs
    public final void ejecutar(SolicitudDominio... datos) {
        SolicitudDominio solicitud = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                datos.length > 0 ? datos[0] : null, new SolicitudDominio.Builder().build());
        if (!EstadoSolicitudDominio.FINALIZADA.equals(solicitud.getEstadoSolicitud().getNombre())) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.CalificacionNegocioImpl.USUARIO_ERROR_SERVICIO_NO_FINALIZADO);
        }
    }
}
