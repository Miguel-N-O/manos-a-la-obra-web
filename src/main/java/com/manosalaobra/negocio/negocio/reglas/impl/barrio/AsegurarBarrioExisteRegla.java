package com.manosalaobra.negocio.negocio.reglas.impl.barrio;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.entidad.BarrioEntidad;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.util.UUID;

public final class AsegurarBarrioExisteRegla implements Regla<Object> {

    private static final Regla<Object> INSTANCIA = new AsegurarBarrioExisteRegla();

    private AsegurarBarrioExisteRegla() {
    }

    public static Regla<Object> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        UUID idBarrio = (UUID) datos[0];
        DAOFactory daoFactory = (DAOFactory) datos[1];
        BarrioEntidad encontrado = daoFactory.obtenerBarrioDAO().consultarPorId(idBarrio);
        if (UtilUUID.esValorDefecto(encontrado.getId()) || !encontrado.isActivo()) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_BARRIO_NO_VALIDO);
        }
    }
}
