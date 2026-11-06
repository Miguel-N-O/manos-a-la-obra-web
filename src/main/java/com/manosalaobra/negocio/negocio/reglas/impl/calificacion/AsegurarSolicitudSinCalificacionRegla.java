package com.manosalaobra.negocio.negocio.reglas.impl.calificacion;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.entidad.CalificacionEntidad;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import java.util.UUID;

// HU-14: una solicitud se califica una sola vez. Recibe: [0] id de la solicitud y [1] la DAOFactory
public final class AsegurarSolicitudSinCalificacionRegla implements Regla<Object> {

    private static final Regla<Object> INSTANCIA = new AsegurarSolicitudSinCalificacionRegla();

    private AsegurarSolicitudSinCalificacionRegla() {
    }

    public static Regla<Object> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        UUID solicitudId = (UUID) datos[0];
        DAOFactory daoFactory = (DAOFactory) datos[1];

        CalificacionEntidad filtro = new CalificacionEntidad();
        filtro.getSolicitud().setId(solicitudId);
        if (!daoFactory.obtenerCalificacionDAO().consultarPorFiltro(filtro).isEmpty()) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.CalificacionNegocioImpl.USUARIO_ERROR_SOLICITUD_YA_CALIFICADA);
        }
    }
}
