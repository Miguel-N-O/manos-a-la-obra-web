package com.manosalaobra.dao.datos.entidad.mysql;

import com.manosalaobra.dao.datos.entidad.IndicativoPaisDAO;
import com.manosalaobra.dao.datos.entidad.SqlDAO;
import com.manosalaobra.entidad.IndicativoPaisEntidad;
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

public final class IndicativoPaisMySqlDAO extends SqlDAO implements IndicativoPaisDAO {

    private static final String SELECCIONAR = "SELECT id, pais, indicativo, activo FROM IndicativoPais WHERE 1 = 1";

    public IndicativoPaisMySqlDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public IndicativoPaisEntidad consultarPorId(UUID id) {
        IndicativoPaisEntidad filtro = new IndicativoPaisEntidad();
        filtro.setId(id);
        List<IndicativoPaisEntidad> encontrados = consultarPorFiltro(filtro);
        return encontrados.isEmpty() ? new IndicativoPaisEntidad() : encontrados.get(0);
    }

    @Override
    public List<IndicativoPaisEntidad> consultarPorFiltro(IndicativoPaisEntidad filtro) {
        IndicativoPaisEntidad filtroSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                filtro, new IndicativoPaisEntidad());
        List<String> parametros = new ArrayList<>();
        StringBuilder consulta = new StringBuilder(SELECCIONAR);
        if (!UtilUUID.esValorDefecto(filtroSeguro.getId())) {
            consulta.append(" AND id = ?");
            parametros.add(filtroSeguro.getId().toString());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtroSeguro.getPais())) {
            consulta.append(" AND pais = ?");
            parametros.add(filtroSeguro.getPais());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtroSeguro.getIndicativo())) {
            consulta.append(" AND indicativo = ?");
            parametros.add(filtroSeguro.getIndicativo());
        }
        consulta.append(" ORDER BY pais ASC");
        return ejecutarConsulta(consulta.toString(), parametros);
    }

    @Override
    public List<IndicativoPaisEntidad> consultarTodos() {
        return consultarPorFiltro(new IndicativoPaisEntidad());
    }

    private List<IndicativoPaisEntidad> ejecutarConsulta(String consulta, List<String> parametros) {
        List<IndicativoPaisEntidad> indicativos = new ArrayList<>();
        try (PreparedStatement sentencia = getConexion().prepareStatement(consulta)) {
            for (int posicion = 0; posicion < parametros.size(); posicion++) {
                sentencia.setString(posicion + 1, parametros.get(posicion));
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    indicativos.add(new IndicativoPaisEntidad(
                            UtilUUID.convertirAUUID(resultado.getString("id")),
                            resultado.getString("pais"),
                            resultado.getString("indicativo"),
                            resultado.getBoolean("activo")));
                }
            }
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.IndicativoPaisMySqlDAO.USUARIO_ERROR_PROBLEMA_CONSULTANDO_INDICATIVO_PAIS,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.IndicativoPaisMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_INDICATIVO_PAIS,
                    excepcion.getMessage(), excepcion);
        }
        return indicativos;
    }
}
