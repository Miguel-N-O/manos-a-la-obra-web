package com.manosalaobra.dao.datos.entidad;

import com.manosalaobra.transversal.utilitarios.UtilSQL;

import java.sql.Connection;

public abstract class SqlDAO {

    private Connection conexion;

    protected SqlDAO(Connection conexion) {
        setConexion(conexion);
    }

    protected Connection getConexion() {
        return conexion;
    }

    private void setConexion(Connection conexion) {
        UtilSQL.asegurarConexionAbierta(conexion);
        this.conexion = conexion;
    }
}
