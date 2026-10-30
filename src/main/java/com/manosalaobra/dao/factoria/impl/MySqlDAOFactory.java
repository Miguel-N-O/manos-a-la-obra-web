package com.manosalaobra.dao.factoria.impl;

import com.manosalaobra.dao.datos.entidad.BarrioDAO;
import com.manosalaobra.dao.datos.entidad.EstadoSolicitudDAO;
import com.manosalaobra.dao.datos.entidad.ServicioDAO;
import com.manosalaobra.dao.datos.entidad.SolicitudDAO;
import com.manosalaobra.dao.datos.entidad.TecnicoDAO;
import com.manosalaobra.dao.datos.entidad.mysql.BarrioMySqlDAO;
import com.manosalaobra.dao.datos.entidad.mysql.EstadoSolicitudMySqlDAO;
import com.manosalaobra.dao.datos.entidad.mysql.IndicativoPaisMySqlDAO;
import com.manosalaobra.dao.datos.entidad.mysql.ServicioMySqlDAO;
import com.manosalaobra.dao.datos.entidad.mysql.SolicitudMySqlDAO;
import com.manosalaobra.dao.datos.entidad.mysql.TecnicoMySqlDAO;

import com.manosalaobra.dao.datos.entidad.IndicativoPaisDAO;

import com.manosalaobra.dao.datos.entidad.EstadoUsuarioDAO;
import com.manosalaobra.dao.datos.entidad.RolDAO;
import com.manosalaobra.dao.datos.entidad.UsuarioDAO;
import com.manosalaobra.dao.datos.entidad.mysql.EstadoUsuarioMySqlDAO;
import com.manosalaobra.dao.datos.entidad.mysql.RolMySqlDAO;
import com.manosalaobra.dao.datos.entidad.mysql.UsuarioMySqlDAO;
import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraDatosExcepcion;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;

import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class MySqlDAOFactory extends DAOFactory {

    private static final String ARCHIVO_CONFIGURACION = "db.properties";
    private static final String VARIABLE_URL = "MANOSALAOBRA_DB_URL";
    private static final String VARIABLE_USUARIO = "MANOSALAOBRA_DB_USUARIO";
    private static final String VARIABLE_CLAVE = "MANOSALAOBRA_DB_CLAVE";

    @Override
    protected void abrirConexion() {
        try {
            Properties archivo = leerArchivoConfiguracion();
            String url = leerValor(VARIABLE_URL, archivo, "db.url");
            String usuario = leerValor(VARIABLE_USUARIO, archivo, "db.user");
            String clave = leerValor(VARIABLE_CLAVE, archivo, "db.password");
            asegurarConfiguracion(url, usuario);
            setConexion(DriverManager.getConnection(url, usuario, clave));
        } catch (ManosALaObraExcepcion excepcion) {
            throw excepcion;
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.MySqlDAOFactory.USUARIO_ERROR_ABRIENDO_CONEXION,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.MySqlDAOFactory.USUARIO_ERROR_NO_CONTROLADO_ABRIENDO_CONEXION,
                    excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public UsuarioDAO obtenerUsuarioDAO() {
        return new UsuarioMySqlDAO(getConexion());
    }

    @Override
    public RolDAO obtenerRolDAO() {
        return new RolMySqlDAO(getConexion());
    }

    @Override
    public EstadoUsuarioDAO obtenerEstadoUsuarioDAO() {
        return new EstadoUsuarioMySqlDAO(getConexion());
    }

    @Override
    public IndicativoPaisDAO obtenerIndicativoPaisDAO() {
        return new IndicativoPaisMySqlDAO(getConexion());
    }

    @Override
    public ServicioDAO obtenerServicioDAO() {
        return new ServicioMySqlDAO(getConexion());
    }

    @Override
    public BarrioDAO obtenerBarrioDAO() {
        return new BarrioMySqlDAO(getConexion());
    }

    @Override
    public TecnicoDAO obtenerTecnicoDAO() {
        return new TecnicoMySqlDAO(getConexion());
    }

    @Override
    public EstadoSolicitudDAO obtenerEstadoSolicitudDAO() {
        return new EstadoSolicitudMySqlDAO(getConexion());
    }

    @Override
    public SolicitudDAO obtenerSolicitudDAO() {
        return new SolicitudMySqlDAO(getConexion());
    }

    private static void asegurarConfiguracion(String url, String usuario) {
        UtilTexto utilTexto = UtilTexto.getUtilTexto();
        if (utilTexto.esVacia(url) || utilTexto.esVacia(usuario)) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.MySqlDAOFactory.USUARIO_ERROR_CONFIGURACION_BASE_DE_DATOS,
                    CatalogoMensajes.MySqlDAOFactory.TECNICO_ERROR_FALTA_CONFIGURACION);
        }
    }

    private static Properties leerArchivoConfiguracion() {
        Properties propiedades = new Properties();
        try (InputStream entrada = MySqlDAOFactory.class.getClassLoader().getResourceAsStream(ARCHIVO_CONFIGURACION)) {
            if (!UtilObjeto.esNulo(entrada)) {
                propiedades.load(entrada);
            }
        } catch (IOException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.MySqlDAOFactory.USUARIO_ERROR_CONFIGURACION_BASE_DE_DATOS,
                    excepcion.getMessage(), excepcion);
        }
        return propiedades;
    }

    private static String leerValor(String variableDeEntorno, Properties archivo, String claveDelArchivo) {
        String variable = System.getenv(variableDeEntorno);
        if (UtilObjeto.esNulo(variable) || variable.isEmpty()) {
            return UtilTexto.getUtilTexto().obtenerValorDefecto(archivo.getProperty(claveDelArchivo));
        }
        return variable;
    }
}
