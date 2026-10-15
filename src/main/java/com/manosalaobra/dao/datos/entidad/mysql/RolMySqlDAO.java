package com.manosalaobra.dao.datos.entidad.mysql;

import com.manosalaobra.dao.datos.entidad.RolDAO;
import com.manosalaobra.dao.datos.entidad.SqlDAO;
import com.manosalaobra.entidad.RolEntidad;
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

public final class RolMySqlDAO extends SqlDAO implements RolDAO {

    private static final String SELECCIONAR = "SELECT id, nombre, activo FROM Rol WHERE 1 = 1";

    public RolMySqlDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public RolEntidad consultarPorId(UUID id) {
        RolEntidad filtro = new RolEntidad();
        filtro.setId(id);
        List<RolEntidad> encontrados = consultarPorFiltro(filtro);
        return encontrados.isEmpty() ? new RolEntidad() : encontrados.get(0);
    }

    @Override
    public List<RolEntidad> consultarPorFiltro(RolEntidad filtro) {
        RolEntidad filtroSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(filtro, new RolEntidad());
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
    public List<RolEntidad> consultarTodos() {
        return consultarPorFiltro(new RolEntidad());
    }

    private List<RolEntidad> ejecutarConsulta(String consulta, List<String> parametros) {
        List<RolEntidad> roles = new ArrayList<>();
        try (PreparedStatement sentencia = getConexion().prepareStatement(consulta)) {
            for (int posicion = 0; posicion < parametros.size(); posicion++) {
                sentencia.setString(posicion + 1, parametros.get(posicion));
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    roles.add(new RolEntidad(
                            UtilUUID.convertirAUUID(resultado.getString("id")),
                            resultado.getString("nombre"),
                            resultado.getBoolean("activo")));
                }
            }
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.RolMySqlDAO.USUARIO_ERROR_PROBLEMA_CONSULTANDO_ROL,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.RolMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_ROL,
                    excepcion.getMessage(), excepcion);
        }
        return roles;
    }
}
