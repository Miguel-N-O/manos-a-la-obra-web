package com.manosalaobra.dao.datos.entidad.mysql;

import com.manosalaobra.dao.datos.entidad.EstadoSolicitudDAO;
import com.manosalaobra.dao.datos.entidad.SqlDAO;
import com.manosalaobra.entidad.EstadoSolicitudEntidad;
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

public final class EstadoSolicitudMySqlDAO extends SqlDAO implements EstadoSolicitudDAO {

    private static final String SELECCIONAR = "SELECT id, nombre, activo FROM EstadoSolicitud WHERE 1 = 1";

    public EstadoSolicitudMySqlDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public EstadoSolicitudEntidad consultarPorId(UUID id) {
        EstadoSolicitudEntidad filtro = new EstadoSolicitudEntidad();
        filtro.setId(id);
        List<EstadoSolicitudEntidad> encontrados = consultarPorFiltro(filtro);
        return encontrados.isEmpty() ? new EstadoSolicitudEntidad() : encontrados.get(0);
    }

    @Override
    public List<EstadoSolicitudEntidad> consultarPorFiltro(EstadoSolicitudEntidad filtro) {
        EstadoSolicitudEntidad filtroSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                filtro, new EstadoSolicitudEntidad());
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
    public List<EstadoSolicitudEntidad> consultarTodos() {
        return consultarPorFiltro(new EstadoSolicitudEntidad());
    }

    private List<EstadoSolicitudEntidad> ejecutarConsulta(String consulta, List<String> parametros) {
        List<EstadoSolicitudEntidad> estados = new ArrayList<>();
        try (PreparedStatement sentencia = getConexion().prepareStatement(consulta)) {
            for (int posicion = 0; posicion < parametros.size(); posicion++) {
                sentencia.setString(posicion + 1, parametros.get(posicion));
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    estados.add(new EstadoSolicitudEntidad(
                            UtilUUID.convertirAUUID(resultado.getString("id")),
                            resultado.getString("nombre"),
                            resultado.getBoolean("activo")));
                }
            }
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.EstadoSolicitudMySqlDAO.USUARIO_ERROR_PROBLEMA_CONSULTANDO_ESTADO_SOLICITUD,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.EstadoSolicitudMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_ESTADO_SOLICITUD,
                    excepcion.getMessage(), excepcion);
        }
        return estados;
    }
}
