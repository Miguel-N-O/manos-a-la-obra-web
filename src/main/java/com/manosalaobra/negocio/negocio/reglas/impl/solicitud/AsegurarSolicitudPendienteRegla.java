package com.manosalaobra.negocio.negocio.reglas.impl.solicitud;

import com.manosalaobra.dominio.EstadoSolicitudDominio;
import com.manosalaobra.dominio.SolicitudDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

// HU-11 CA-04 y HU-12 CA-04: solo se responde una solicitud que sigue PENDIENTE
public final class AsegurarSolicitudPendienteRegla implements Regla<SolicitudDominio> {

    private static final Regla<SolicitudDominio> INSTANCIA = new AsegurarSolicitudPendienteRegla();

    private AsegurarSolicitudPendienteRegla() {
    }

    public static Regla<SolicitudDominio> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    @SafeVarargs
    public final void ejecutar(SolicitudDominio... datos) {
        SolicitudDominio solicitud = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                datos.length > 0 ? datos[0] : null, new SolicitudDominio.Builder().build());
        if (!EstadoSolicitudDominio.PENDIENTE.equals(solicitud.getEstadoSolicitud().getNombre())) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_SOLICITUD_YA_RESPONDIDA);
        }
    }
}
