package com.manosalaobra.negocio.negocio.reglas.impl.solicitud;

import com.manosalaobra.dominio.EstadoSolicitudDominio;
import com.manosalaobra.dominio.SolicitudDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

// Solo se finaliza un servicio cuya solicitud el técnico ya aceptó (y todavía no finalizó)
public final class AsegurarSolicitudAceptadaRegla implements Regla<SolicitudDominio> {

    private static final Regla<SolicitudDominio> INSTANCIA = new AsegurarSolicitudAceptadaRegla();

    private AsegurarSolicitudAceptadaRegla() {
    }

    public static Regla<SolicitudDominio> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    @SafeVarargs
    public final void ejecutar(SolicitudDominio... datos) {
        SolicitudDominio solicitud = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                datos.length > 0 ? datos[0] : null, new SolicitudDominio.Builder().build());
        if (!EstadoSolicitudDominio.ACEPTADA.equals(solicitud.getEstadoSolicitud().getNombre())) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_SOLO_SE_FINALIZA_ACEPTADA);
        }
    }
}
