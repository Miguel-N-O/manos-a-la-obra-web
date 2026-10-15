package com.manosalaobra.dao.datos.entidad.mysql;

import com.manosalaobra.dao.datos.entidad.BarrioDAO;
import com.manosalaobra.dao.datos.entidad.SqlDAO;
import com.manosalaobra.entidad.BarrioEntidad;
import com.manosalaobra.entidad.CiudadEntidad;
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

public final class BarrioMySqlDAO extends SqlDAO implements BarrioDAO {

    private static final String SELECCIONAR = """
            SELECT b.id, b.nombre, b.activo,
                   c.id AS ciudad_id, c.nombre AS ciudad_nombre, c.activo AS ciudad_activo
            FROM Barrio b
            JOIN Ciudad c ON c.id = b.ciudad_id
            WHERE 1 = 1
            """;

    public BarrioMySqlDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public BarrioEntidad consultarPorId(UUID id) {
        BarrioEntidad filtro = new BarrioEntidad();
        filtro.setId(id);
        List<BarrioEntidad> encontrados = consultarPorFiltro(filtro);
        return encontrados.isEmpty() ? new BarrioEntidad() : encontrados.get(0);
    }

    @Override
    public List<BarrioEntidad> consultarPorFiltro(BarrioEntidad filtro) {
        BarrioEntidad filtroSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(filtro, new BarrioEntidad());
        List<String> parametros = new ArrayList<>();
        StringBuilder consulta = new StringBuilder(SELECCIONAR);
        if (!UtilUUID.esValorDefecto(filtroSeguro.getId())) {
            consulta.append(" AND b.id = ?");
            parametros.add(filtroSeguro.getId().toString());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtroSeguro.getNombre())) {
            consulta.append(" AND b.nombre = ?");
            parametros.add(filtroSeguro.getNombre());
        }
        consulta.append(" ORDER BY b.nombre ASC");
        return ejecutarConsulta(consulta.toString(), parametros);
    }

    @Override
    public List<BarrioEntidad> consultarTodos() {
        return consultarPorFiltro(new BarrioEntidad());
    }

    private List<BarrioEntidad> ejecutarConsulta(String consulta, List<String> parametros) {
        List<BarrioEntidad> barrios = new ArrayList<>();
        try (PreparedStatement sentencia = getConexion().prepareStatement(consulta)) {
            for (int posicion = 0; posicion < parametros.size(); posicion++) {
                sentencia.setString(posicion + 1, parametros.get(posicion));
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    barrios.add(convertirFilaAEntidad(resultado));
                }
            }
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.BarrioMySqlDAO.USUARIO_ERROR_PROBLEMA_CONSULTANDO_BARRIO,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.BarrioMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_BARRIO,
                    excepcion.getMessage(), excepcion);
        }
        return barrios;
    }

    private BarrioEntidad convertirFilaAEntidad(ResultSet fila) throws SQLException {
        CiudadEntidad ciudad = new CiudadEntidad(
                UtilUUID.convertirAUUID(fila.getString("ciudad_id")),
                fila.getString("ciudad_nombre"),
                fila.getBoolean("ciudad_activo"));
        return new BarrioEntidad(
                UtilUUID.convertirAUUID(fila.getString("id")),
                ciudad,
                fila.getString("nombre"),
                fila.getBoolean("activo"));
    }
}
