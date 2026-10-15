package com.manosalaobra.dao.datos.entidad.mysql;

import com.manosalaobra.dao.datos.entidad.SqlDAO;
import com.manosalaobra.dao.datos.entidad.UsuarioDAO;
import com.manosalaobra.entidad.EstadoUsuarioEntidad;
import com.manosalaobra.entidad.RolEntidad;
import com.manosalaobra.entidad.UsuarioEntidad;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraDatosExcepcion;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
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

public final class UsuarioMySqlDAO extends SqlDAO implements UsuarioDAO {

    private static final String INSERTAR = """
            INSERT INTO Usuario (id, rol_id, estado_usuario_id, nombre, correo, clave_hash, telefono)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECCIONAR = """
            SELECT u.id, u.nombre, u.correo, u.clave_hash, u.telefono,
                   r.id AS rol_id, r.nombre AS rol_nombre, r.activo AS rol_activo,
                   e.id AS estado_usuario_id, e.nombre AS estado_usuario_nombre, e.activo AS estado_usuario_activo
            FROM Usuario u
            JOIN Rol r ON r.id = u.rol_id
            JOIN EstadoUsuario e ON e.id = u.estado_usuario_id
            WHERE 1 = 1
            """;

    public UsuarioMySqlDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(UsuarioEntidad entidad) {
        try (PreparedStatement sentencia = getConexion().prepareStatement(INSERTAR)) {
            sentencia.setString(1, entidad.getId().toString());
            sentencia.setString(2, entidad.getRol().getId().toString());
            sentencia.setString(3, entidad.getEstadoUsuario().getId().toString());
            sentencia.setString(4, entidad.getNombre());
            sentencia.setString(5, entidad.getCorreo());
            sentencia.setString(6, entidad.getClaveHash());
            sentencia.setString(7, entidad.getTelefono());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.UsuarioMySqlDAO.USUARIO_ERROR_PROBLEMA_CREANDO_USUARIO,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.UsuarioMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CREANDO_USUARIO,
                    excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public UsuarioEntidad consultarPorId(UUID id) {
        UsuarioEntidad filtro = new UsuarioEntidad();
        filtro.setId(id);
        List<UsuarioEntidad> encontrados = consultarPorFiltro(filtro);
        return encontrados.isEmpty() ? new UsuarioEntidad() : encontrados.get(0);
    }

    @Override
    public List<UsuarioEntidad> consultarPorFiltro(UsuarioEntidad filtro) {
        UsuarioEntidad filtroSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(filtro, new UsuarioEntidad());
        List<String> parametros = new ArrayList<>();
        StringBuilder consulta = new StringBuilder(SELECCIONAR);

        if (!UtilUUID.esValorDefecto(filtroSeguro.getId())) {
            agregarCondicion(consulta, parametros, "u.id", filtroSeguro.getId().toString());
        }
        agregarCondicionDeTexto(consulta, parametros, "u.correo", filtroSeguro.getCorreo());
        agregarCondicionDeTexto(consulta, parametros, "r.nombre", filtroSeguro.getRol().getNombre());
        agregarCondicionDeTexto(consulta, parametros, "e.nombre", filtroSeguro.getEstadoUsuario().getNombre());
        consulta.append(" ORDER BY u.nombre ASC");

        return ejecutarConsulta(consulta.toString(), parametros);
    }

    @Override
    public List<UsuarioEntidad> consultarTodos() {
        return consultarPorFiltro(new UsuarioEntidad());
    }

    private void agregarCondicionDeTexto(StringBuilder consulta, List<String> parametros, String columna,
            String texto) {
        if (!UtilTexto.getUtilTexto().esVacia(texto)) {
            agregarCondicion(consulta, parametros, columna, texto);
        }
    }

    private void agregarCondicion(StringBuilder consulta, List<String> parametros, String columna, String valor) {
        consulta.append(" AND ").append(columna).append(" = ?");
        parametros.add(valor);
    }

    private List<UsuarioEntidad> ejecutarConsulta(String consulta, List<String> parametros) {
        List<UsuarioEntidad> usuarios = new ArrayList<>();
        try (PreparedStatement sentencia = getConexion().prepareStatement(consulta)) {
            for (int posicion = 0; posicion < parametros.size(); posicion++) {
                sentencia.setString(posicion + 1, parametros.get(posicion));
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    usuarios.add(convertirFilaAEntidad(resultado));
                }
            }
        } catch (ManosALaObraExcepcion excepcion) {
            throw excepcion;
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.UsuarioMySqlDAO.USUARIO_ERROR_PROBLEMA_CONSULTANDO_USUARIO,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.UsuarioMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_USUARIO,
                    excepcion.getMessage(), excepcion);
        }
        return usuarios;
    }

    private UsuarioEntidad convertirFilaAEntidad(ResultSet fila) throws SQLException {
        RolEntidad rol = new RolEntidad(
                UtilUUID.convertirAUUID(fila.getString("rol_id")),
                fila.getString("rol_nombre"),
                fila.getBoolean("rol_activo"));
        EstadoUsuarioEntidad estadoUsuario = new EstadoUsuarioEntidad(
                UtilUUID.convertirAUUID(fila.getString("estado_usuario_id")),
                fila.getString("estado_usuario_nombre"),
                fila.getBoolean("estado_usuario_activo"));
        return new UsuarioEntidad(
                UtilUUID.convertirAUUID(fila.getString("id")),
                rol,
                estadoUsuario,
                fila.getString("nombre"),
                fila.getString("correo"),
                fila.getString("clave_hash"),
                fila.getString("telefono"));
    }
}
