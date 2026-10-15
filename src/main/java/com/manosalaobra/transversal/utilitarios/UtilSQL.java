package com.manosalaobra.transversal.utilitarios;

import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraTransversalExcepcion;

import java.sql.Connection;
import java.sql.SQLException;

public final class UtilSQL {

    private UtilSQL() {
    }

    public static boolean conexionEstaVacia(Connection conexion) {
        return UtilObjeto.esNulo(conexion);
    }

    public static boolean conexionEstaAbierta(Connection conexion) {
        try {
            return !conexionEstaVacia(conexion) && !conexion.isClosed();
        } catch (SQLException excepcion) {
            throw ManosALaObraTransversalExcepcion.crear(
                    CatalogoMensajes.UtilSQL.USUARIO_ERROR_PROBLEMA_VERIFICANDO_CONEXION,
                    excepcion.getMessage(), excepcion);
        }
    }

    public static void asegurarConexionAbierta(Connection conexion) {
        if (!conexionEstaAbierta(conexion)) {
            throw ManosALaObraTransversalExcepcion.crear(
                    CatalogoMensajes.UtilSQL.USUARIO_ERROR_CONEXION_NO_ESTA_ABIERTA,
                    CatalogoMensajes.UtilSQL.TECNICO_ERROR_CONEXION_NO_ESTA_ABIERTA);
        }
    }

    public static boolean transaccionEstaIniciada(Connection conexion) {
        if (!conexionEstaAbierta(conexion)) {
            return false;
        }
        try {
            return !conexion.getAutoCommit();
        } catch (SQLException excepcion) {
            throw ManosALaObraTransversalExcepcion.crear(
                    CatalogoMensajes.UtilSQL.USUARIO_ERROR_PROBLEMA_VERIFICANDO_CONEXION,
                    excepcion.getMessage(), excepcion);
        }
    }

    public static void iniciarTransaccion(Connection conexion) {
        asegurarConexionAbierta(conexion);
        if (transaccionEstaIniciada(conexion)) {
            throw ManosALaObraTransversalExcepcion.crear(
                    CatalogoMensajes.UtilSQL.USUARIO_ERROR_TRANSACCION_YA_INICIADA,
                    CatalogoMensajes.UtilSQL.TECNICO_ERROR_TRANSACCION_YA_INICIADA);
        }
        try {
            conexion.setAutoCommit(false);
        } catch (SQLException excepcion) {
            throw ManosALaObraTransversalExcepcion.crear(
                    CatalogoMensajes.UtilSQL.USUARIO_ERROR_PROBLEMA_INICIANDO_TRANSACCION,
                    excepcion.getMessage(), excepcion);
        }
    }

    public static void confirmarTransaccion(Connection conexion) {
        asegurarTransaccionIniciada(conexion);
        try {
            conexion.commit();
            conexion.setAutoCommit(true);
        } catch (SQLException excepcion) {
            throw ManosALaObraTransversalExcepcion.crear(
                    CatalogoMensajes.UtilSQL.USUARIO_ERROR_PROBLEMA_CONFIRMANDO_TRANSACCION,
                    excepcion.getMessage(), excepcion);
        }
    }

    public static void cancelarTransaccion(Connection conexion) {
        asegurarTransaccionIniciada(conexion);
        try {
            conexion.rollback();
            conexion.setAutoCommit(true);
        } catch (SQLException excepcion) {
            throw ManosALaObraTransversalExcepcion.crear(
                    CatalogoMensajes.UtilSQL.USUARIO_ERROR_PROBLEMA_CANCELANDO_TRANSACCION,
                    excepcion.getMessage(), excepcion);
        }
    }

    public static void cerrarConexion(Connection conexion) {
        asegurarConexionAbierta(conexion);
        try {
            conexion.close();
        } catch (SQLException excepcion) {
            throw ManosALaObraTransversalExcepcion.crear(
                    CatalogoMensajes.UtilSQL.USUARIO_ERROR_PROBLEMA_CERRANDO_CONEXION,
                    excepcion.getMessage(), excepcion);
        }
    }

    private static void asegurarTransaccionIniciada(Connection conexion) {
        if (!transaccionEstaIniciada(conexion)) {
            throw ManosALaObraTransversalExcepcion.crear(
                    CatalogoMensajes.UtilSQL.USUARIO_ERROR_TRANSACCION_NO_INICIADA,
                    CatalogoMensajes.UtilSQL.TECNICO_ERROR_TRANSACCION_NO_INICIADA);
        }
    }
}
