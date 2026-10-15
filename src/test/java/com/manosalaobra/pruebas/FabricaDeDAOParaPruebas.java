package com.manosalaobra.pruebas;

import com.manosalaobra.dao.datos.entidad.BarrioDAO;
import com.manosalaobra.dao.datos.entidad.EstadoUsuarioDAO;
import com.manosalaobra.dao.datos.entidad.IndicativoPaisDAO;
import com.manosalaobra.dao.datos.entidad.RolDAO;
import com.manosalaobra.dao.datos.entidad.ServicioDAO;
import com.manosalaobra.dao.datos.entidad.TecnicoDAO;
import com.manosalaobra.dao.datos.entidad.UsuarioDAO;
import com.manosalaobra.dao.factoria.DAOFactory;

public final class FabricaDeDAOParaPruebas extends DAOFactory {

    private ServicioDAO servicioDAO;
    private BarrioDAO barrioDAO;
    private TecnicoDAO tecnicoDAO;

    public FabricaDeDAOParaPruebas conServicioDAO(ServicioDAO servicioDAO) {
        this.servicioDAO = servicioDAO;
        return this;
    }

    public FabricaDeDAOParaPruebas conBarrioDAO(BarrioDAO barrioDAO) {
        this.barrioDAO = barrioDAO;
        return this;
    }

    public FabricaDeDAOParaPruebas conTecnicoDAO(TecnicoDAO tecnicoDAO) {
        this.tecnicoDAO = tecnicoDAO;
        return this;
    }

    @Override
    protected void abrirConexion() {
    }

    @Override
    public UsuarioDAO obtenerUsuarioDAO() {
        return null;
    }

    @Override
    public RolDAO obtenerRolDAO() {
        return null;
    }

    @Override
    public EstadoUsuarioDAO obtenerEstadoUsuarioDAO() {
        return null;
    }

    @Override
    public IndicativoPaisDAO obtenerIndicativoPaisDAO() {
        return null;
    }

    @Override
    public ServicioDAO obtenerServicioDAO() {
        return servicioDAO;
    }

    @Override
    public BarrioDAO obtenerBarrioDAO() {
        return barrioDAO;
    }

    @Override
    public TecnicoDAO obtenerTecnicoDAO() {
        return tecnicoDAO;
    }
}
