package com.manosalaobra.negocio.negocio.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.ServicioDominio;
import com.manosalaobra.entidad.ServicioEntidad;
import com.manosalaobra.negocio.negocio.ServicioNegocio;
import com.manosalaobra.negocio.negocio.ensamblador.impl.ServicioEnsambladorEntidad;
import java.util.ArrayList;
import java.util.List;

public final class ServicioNegocioImpl implements ServicioNegocio {

    private final DAOFactory daoFactory;

    public ServicioNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<ServicioDominio> consultarServicios() {
        List<ServicioDominio> activos = new ArrayList<>();
        for (ServicioEntidad entidad : daoFactory.obtenerServicioDAO().consultarTodos()) {
            if (entidad.isActivo()) {
                activos.add(ServicioEnsambladorEntidad.getInstancia().convertirADominio(entidad));
            }
        }
        return activos;
    }
}
