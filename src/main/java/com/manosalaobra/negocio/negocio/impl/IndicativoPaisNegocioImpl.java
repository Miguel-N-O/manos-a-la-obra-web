package com.manosalaobra.negocio.negocio.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.IndicativoPaisDominio;
import com.manosalaobra.entidad.IndicativoPaisEntidad;
import com.manosalaobra.negocio.negocio.IndicativoPaisNegocio;
import com.manosalaobra.negocio.negocio.ensamblador.impl.IndicativoPaisEnsambladorEntidad;
import java.util.ArrayList;
import java.util.List;

public final class IndicativoPaisNegocioImpl implements IndicativoPaisNegocio {

    private final DAOFactory daoFactory;

    public IndicativoPaisNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<IndicativoPaisDominio> consultarIndicativosPais() {
        List<IndicativoPaisDominio> activos = new ArrayList<>();
        for (IndicativoPaisEntidad entidad : daoFactory.obtenerIndicativoPaisDAO().consultarTodos()) {
            if (entidad.isActivo()) {
                activos.add(IndicativoPaisEnsambladorEntidad.getInstancia().convertirADominio(entidad));
            }
        }
        return activos;
    }
}
