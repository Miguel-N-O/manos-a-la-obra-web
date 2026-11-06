package com.manosalaobra.negocio.negocio.reglas.impl.solicitud;

import com.manosalaobra.dominio.EstadoSolicitudDominio;
import com.manosalaobra.dominio.SolicitudDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

// HU-13: los datos de contacto del técnico se entregan solo después de que acepte la solicitud
public final class AsegurarContactoDisponibleRegla implements Regla<SolicitudDominio> {

    private static final Regla<SolicitudDominio> INSTANCIA = new AsegurarContactoDisponibleRegla();

    private AsegurarContactoDisponibleRegla() {
    }

    public static Regla<SolicitudDominio> obtenerInstancia() {
        return INSTANCIA;
    }

    public static boolean contactoHabilitado(SolicitudDominio solicitud) {
        String estado = solicitud.getEstadoSolicitud().getNombre();
        return EstadoSolicitudDominio.ACEPTADA.equals(estado) || EstadoSolicitudDominio.FINALIZADA.equals(estado);
    }

    @Override
    @SafeVarargs
    public final void ejecutar(SolicitudDominio... datos) {
        SolicitudDominio solicitud = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                datos.length > 0 ? datos[0] : null, new SolicitudDominio.Builder().build());
        if (!contactoHabilitado(solicitud)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_CONTACTO_NO_DISPONIBLE);
        }
    }
}
