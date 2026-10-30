package com.manosalaobra.dao.datos.entidad.mysql;

import com.manosalaobra.dao.datos.entidad.SqlDAO;
import com.manosalaobra.dao.datos.entidad.TecnicoDAO;
import com.manosalaobra.entidad.BarrioEntidad;
import com.manosalaobra.entidad.CiudadEntidad;
import com.manosalaobra.entidad.EstadoUsuarioEntidad;
import com.manosalaobra.entidad.ServicioEntidad;
import com.manosalaobra.entidad.TecnicoEntidad;
import com.manosalaobra.entidad.UsuarioEntidad;
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

public final class TecnicoMySqlDAO extends SqlDAO implements TecnicoDAO {

    private static final int CODIGO_ERROR_DUPLICADO = 1062;

    // HU-02: el técnico se crea sin perfil; especialidad, barrio y tarifa se completan en HU-04.
    // ruta_documento queda vacía: el archivo del documento se pospuso para un sprint posterior.
    private static final String INSERTAR = """
            INSERT INTO Tecnico (id, usuario_id, tipo_documento, documento)
            VALUES (?, ?, ?, ?)
            """;

    private static final String CONTAR_POR_DOCUMENTO = "SELECT COUNT(*) FROM Tecnico WHERE documento = ?";

    private static final String SELECCIONAR = """
            SELECT t.id, t.tarifa, t.experiencia, t.promedio,
                   u.id AS usuario_id, u.nombre AS usuario_nombre,
                   e.id AS estado_usuario_id, e.nombre AS estado_usuario_nombre, e.activo AS estado_usuario_activo,
                   s.id AS servicio_id, s.nombre AS servicio_nombre, s.activo AS servicio_activo,
                   b.id AS barrio_id, b.nombre AS barrio_nombre, b.activo AS barrio_activo,
                   c.id AS ciudad_id, c.nombre AS ciudad_nombre, c.activo AS ciudad_activo
            FROM Tecnico t
            JOIN Usuario u ON u.id = t.usuario_id
            JOIN EstadoUsuario e ON e.id = u.estado_usuario_id
            JOIN Servicio s ON s.id = t.servicio_id
            JOIN Barrio b ON b.id = t.barrio_id
            JOIN Ciudad c ON c.id = b.ciudad_id
            WHERE 1 = 1
            """;

    public TecnicoMySqlDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(TecnicoEntidad entidad) {
        try (PreparedStatement sentencia = getConexion().prepareStatement(INSERTAR)) {
            sentencia.setString(1, entidad.getId().toString());
            sentencia.setString(2, entidad.getUsuario().getId().toString());
            sentencia.setString(3, entidad.getTipoDocumento());
            sentencia.setString(4, entidad.getDocumento());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            if (excepcion.getErrorCode() == CODIGO_ERROR_DUPLICADO) {
                throw ManosALaObraDatosExcepcion.crear(
                        CatalogoMensajes.TecnicoMySqlDAO.USUARIO_ERROR_DOCUMENTO_YA_REGISTRADO,
                        excepcion.getMessage(), excepcion);
            }
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.TecnicoMySqlDAO.USUARIO_ERROR_PROBLEMA_CREANDO_TECNICO,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.TecnicoMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CREANDO_TECNICO,
                    excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public boolean existeDocumento(String documento) {
        try (PreparedStatement sentencia = getConexion().prepareStatement(CONTAR_POR_DOCUMENTO)) {
            sentencia.setString(1, documento);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() && resultado.getInt(1) > 0;
            }
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.TecnicoMySqlDAO.USUARIO_ERROR_PROBLEMA_CONSULTANDO_TECNICO,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.TecnicoMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_TECNICO,
                    excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public TecnicoEntidad consultarPorId(UUID id) {
        TecnicoEntidad filtro = new TecnicoEntidad();
        filtro.setId(id);
        List<TecnicoEntidad> encontrados = consultarPorFiltro(filtro);
        return encontrados.isEmpty() ? new TecnicoEntidad() : encontrados.get(0);
    }

    @Override
    public List<TecnicoEntidad> consultarPorFiltro(TecnicoEntidad filtro) {
        TecnicoEntidad filtroSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(filtro, new TecnicoEntidad());
        List<String> parametros = new ArrayList<>();
        StringBuilder consulta = new StringBuilder(SELECCIONAR);

        agregarCondicionDeId(consulta, parametros, "t.id", filtroSeguro.getId());
        agregarCondicionDeId(consulta, parametros, "s.id", filtroSeguro.getServicio().getId());
        agregarCondicionDeId(consulta, parametros, "b.id", filtroSeguro.getBarrio().getId());
        if (!UtilTexto.getUtilTexto().esVacia(filtroSeguro.getUsuario().getEstadoUsuario().getNombre())) {
            consulta.append(" AND e.nombre = ?");
            parametros.add(filtroSeguro.getUsuario().getEstadoUsuario().getNombre());
        }
        consulta.append(" ORDER BY t.promedio DESC, u.nombre ASC");

        return ejecutarConsulta(consulta.toString(), parametros);
    }

    @Override
    public List<TecnicoEntidad> consultarTodos() {
        return consultarPorFiltro(new TecnicoEntidad());
    }

    private void agregarCondicionDeId(StringBuilder consulta, List<String> parametros, String columna, UUID id) {
        if (!UtilUUID.esValorDefecto(id)) {
            consulta.append(" AND ").append(columna).append(" = ?");
            parametros.add(id.toString());
        }
    }

    private List<TecnicoEntidad> ejecutarConsulta(String consulta, List<String> parametros) {
        List<TecnicoEntidad> tecnicos = new ArrayList<>();
        try (PreparedStatement sentencia = getConexion().prepareStatement(consulta)) {
            for (int posicion = 0; posicion < parametros.size(); posicion++) {
                sentencia.setString(posicion + 1, parametros.get(posicion));
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    tecnicos.add(convertirFilaAEntidad(resultado));
                }
            }
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.TecnicoMySqlDAO.USUARIO_ERROR_PROBLEMA_CONSULTANDO_TECNICO,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.TecnicoMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_TECNICO,
                    excepcion.getMessage(), excepcion);
        }
        return tecnicos;
    }

    private TecnicoEntidad convertirFilaAEntidad(ResultSet fila) throws SQLException {
        UsuarioEntidad usuario = new UsuarioEntidad();
        usuario.setId(UtilUUID.convertirAUUID(fila.getString("usuario_id")));
        usuario.setNombre(fila.getString("usuario_nombre"));
        usuario.setEstadoUsuario(new EstadoUsuarioEntidad(
                UtilUUID.convertirAUUID(fila.getString("estado_usuario_id")),
                fila.getString("estado_usuario_nombre"),
                fila.getBoolean("estado_usuario_activo")));
        ServicioEntidad servicio = new ServicioEntidad(
                UtilUUID.convertirAUUID(fila.getString("servicio_id")),
                fila.getString("servicio_nombre"),
                fila.getBoolean("servicio_activo"));
        CiudadEntidad ciudad = new CiudadEntidad(
                UtilUUID.convertirAUUID(fila.getString("ciudad_id")),
                fila.getString("ciudad_nombre"),
                fila.getBoolean("ciudad_activo"));
        BarrioEntidad barrio = new BarrioEntidad(
                UtilUUID.convertirAUUID(fila.getString("barrio_id")),
                ciudad,
                fila.getString("barrio_nombre"),
                fila.getBoolean("barrio_activo"));
        return new TecnicoEntidad(
                UtilUUID.convertirAUUID(fila.getString("id")),
                usuario,
                servicio,
                barrio,
                fila.getBigDecimal("tarifa"),
                fila.getInt("experiencia"),
                UtilTexto.VACIO,
                fila.getBigDecimal("promedio"));
    }
}
