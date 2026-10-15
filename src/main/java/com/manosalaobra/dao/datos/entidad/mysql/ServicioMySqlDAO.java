package com.manosalaobra.dao.datos.entidad.mysql;

import com.manosalaobra.dao.datos.entidad.ServicioDAO;
import com.manosalaobra.dao.datos.entidad.SqlDAO;
import com.manosalaobra.entidad.ServicioEntidad;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraDatosExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ServicioMySqlDAO extends SqlDAO implements ServicioDAO {

    private static final String SELECCIONAR = "SELECT id, nombre, activo FROM Servicio WHERE 1 = 1";

    public ServicioMySqlDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public ServicioEntidad consultarPorId(UUID id) {
        ServicioEntidad filtro = new ServicioEntidad();
        filtro.setId(id);
        List<ServicioEntidad> encontrados = consultarPorFiltro(filtro);
        return encontrados.isEmpty() ? new ServicioEntidad() : encontrados.get(0);
    }

    @Override
    public List<ServicioEntidad> consultarPorFiltro(ServicioEntidad filtro) {
        ServicioEntidad filtroSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                filtro, new ServicioEntidad());
        List<String> parametros = new ArrayList<>();
        StringBuilder consulta = new StringBuilder(SELECCIONAR);
        if (!UtilUUID.esValorDefecto(filtroSeguro.getId())) {
            consulta.append(" AND id = ?");
            parametros.add(filtroSeguro.getId().toString());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtroSeguro.getNombre())) {
            consulta.append(" AND nombre = ?");
            parametros.add(filtroSeguro.getNombre());
        }
        consulta.append(" ORDER BY nombre ASC");
        return ejecutarConsulta(consulta.toString(), parametros);
    }

    @Override
    public List<ServicioEntidad> consultarTodos() {
        return consultarPorFiltro(new ServicioEntidad());
    }

    private List<ServicioEntidad> ejecutarConsulta(String consulta, List<String> parametros) {
        List<ServicioEntidad> servicios = new ArrayList<>();
        try (PreparedStatement sentencia = getConexion().prepareStatement(consulta)) {
            for (int posicion = 0; posicion < parametros.size(); posicion++) {
                sentencia.setString(posicion + 1, parametros.get(posicion));
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    servicios.add(new ServicioEntidad(
                            UtilUUID.convertirAUUID(resultado.getString("id")),
                            resultado.getString("nombre"),
                            resultado.getBoolean("activo")));
                }
            }
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.ServicioMySqlDAO.USUARIO_ERROR_PROBLEMA_CONSULTANDO_SERVICIO,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.ServicioMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_SERVICIO,
                    excepcion.getMessage(), excepcion);
        }
        return servicios;
    }
}
