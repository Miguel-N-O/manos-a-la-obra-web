package com.manosalaobra.negocio.negocio.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.BarrioDominio;
import com.manosalaobra.entidad.BarrioEntidad;
import com.manosalaobra.negocio.negocio.BarrioNegocio;
import com.manosalaobra.negocio.negocio.ensamblador.impl.BarrioEnsambladorEntidad;
import java.util.ArrayList;
import java.util.List;

public final class BarrioNegocioImpl implements BarrioNegocio {

    private final DAOFactory daoFactory;

    public BarrioNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<BarrioDominio> consultarBarrios() {
        List<BarrioDominio> activos = new ArrayList<>();
        for (BarrioEntidad entidad : daoFactory.obtenerBarrioDAO().consultarTodos()) {
            if (entidad.isActivo()) {
                activos.add(BarrioEnsambladorEntidad.getInstancia().convertirADominio(entidad));
            }
        }
        return activos;
    }
}
