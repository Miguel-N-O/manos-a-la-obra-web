package com.manosalaobra.dao.factoria;

import com.manosalaobra.dao.datos.entidad.IndicativoPaisDAO;

import com.manosalaobra.dao.datos.entidad.EstadoUsuarioDAO;
import com.manosalaobra.dao.datos.entidad.RolDAO;
import com.manosalaobra.dao.datos.entidad.UsuarioDAO;
import com.manosalaobra.dao.factoria.enums.FactoriaEnum;
import com.manosalaobra.dao.factoria.impl.MySqlDAOFactory;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraDatosExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilSQL;

import java.sql.Connection;

public abstract class DAOFactory {

    private static final FactoriaEnum FACTORIA = FactoriaEnum.MYSQL;

    private Connection conexion;

    protected DAOFactory() {
        abrirConexion();
    }

    public static DAOFactory obtenerFactoria() {
        switch (FACTORIA) {
            case MYSQL:
                return new MySqlDAOFactory();
            default:
                throw ManosALaObraDatosExcepcion.crear(
                        CatalogoMensajes.DAOFactory.USUARIO_ERROR_FACTORIA_NO_HABILITADA,
                        CatalogoMensajes.DAOFactory.TECNICO_ERROR_FACTORIA_NO_HABILITADA);
        }
    }

    protected Connection getConexion() {
        return conexion;
    }

    protected void setConexion(Connection conexion) {
        UtilSQL.asegurarConexionAbierta(conexion);
        this.conexion = conexion;
    }

    protected abstract void abrirConexion();

    public void cerrarConexion() {
        UtilSQL.cerrarConexion(conexion);
    }

    public void iniciarTransaccion() {
        UtilSQL.iniciarTransaccion(conexion);
    }

    public void confirmarTransaccion() {
        UtilSQL.confirmarTransaccion(conexion);
    }

    public void cancelarTransaccion() {
        UtilSQL.cancelarTransaccion(conexion);
    }

    public abstract UsuarioDAO obtenerUsuarioDAO();

    public abstract RolDAO obtenerRolDAO();

    public abstract EstadoUsuarioDAO obtenerEstadoUsuarioDAO();

    public abstract IndicativoPaisDAO obtenerIndicativoPaisDAO();
}
