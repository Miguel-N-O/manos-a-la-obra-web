package com.manosalaobra.negocio.negocio.reglas.impl.indicativopais;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.entidad.IndicativoPaisEntidad;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.util.UUID;

public final class AsegurarIndicativoPaisExisteRegla implements Regla<Object> {

    private static final Regla<Object> INSTANCIA = new AsegurarIndicativoPaisExisteRegla();

    private AsegurarIndicativoPaisExisteRegla() {
    }

    public static Regla<Object> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        UUID idIndicativoPais = (UUID) datos[0];
        DAOFactory daoFactory = (DAOFactory) datos[1];
        IndicativoPaisEntidad encontrado = daoFactory.obtenerIndicativoPaisDAO().consultarPorId(idIndicativoPais);
        if (UtilUUID.esValorDefecto(encontrado.getId()) || !encontrado.isActivo()) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_INDICATIVO_PAIS_NO_VALIDO);
        }
    }
}
