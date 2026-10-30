package com.manosalaobra.negocio.negocio.reglas.impl.servicio;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.entidad.ServicioEntidad;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.util.UUID;

public final class AsegurarServicioExisteRegla implements Regla<Object> {

    private static final Regla<Object> INSTANCIA = new AsegurarServicioExisteRegla();

    private AsegurarServicioExisteRegla() {
    }

    public static Regla<Object> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        UUID idServicio = (UUID) datos[0];
        DAOFactory daoFactory = (DAOFactory) datos[1];
        ServicioEntidad encontrado = daoFactory.obtenerServicioDAO().consultarPorId(idServicio);
        if (UtilUUID.esValorDefecto(encontrado.getId()) || !encontrado.isActivo()) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_ESPECIALIDAD_NO_VALIDA);
        }
    }
}
