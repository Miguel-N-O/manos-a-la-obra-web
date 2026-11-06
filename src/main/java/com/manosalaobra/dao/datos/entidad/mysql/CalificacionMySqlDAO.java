package com.manosalaobra.dao.datos.entidad.mysql;

import com.manosalaobra.dao.datos.entidad.CalificacionDAO;
import com.manosalaobra.dao.datos.entidad.SqlDAO;
import com.manosalaobra.entidad.CalificacionEntidad;
import com.manosalaobra.entidad.SolicitudEntidad;
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

public final class CalificacionMySqlDAO extends SqlDAO implements CalificacionDAO {

    private static final int CODIGO_ERROR_DUPLICADO = 1062;

    // HU-14 y HU-15: la visibilidad y la fecha toman su valor por defecto en la base
    private static final String INSERTAR = """
            INSERT INTO Calificacion (id, solicitud_id, estrellas, comentario)
            VALUES (?, ?, ?, ?)
            """;

    // HU-17: la moderación cambia si el comentario se muestra o no y por qué motivo se oculta
    private static final String ACTUALIZAR_VISIBILIDAD = """
            UPDATE Calificacion SET visible = ?, motivo_moderacion = ? WHERE id = ?
            """;

    // HU-15: promedio con una cifra decimal; sin calificaciones vuelve a 0
    private static final String ACTUALIZAR_PROMEDIO = """
            UPDATE Tecnico
            SET promedio = COALESCE((SELECT ROUND(AVG(c.estrellas), 1)
                                     FROM Calificacion c
                                     JOIN Solicitud s ON s.id = c.solicitud_id
                                     WHERE s.tecnico_id = ?), 0)
            WHERE id = ?
            """;

    // No trae teléfonos ni correos: solo lo necesario para mostrar quién calificó y a quién
    private static final String SELECCIONAR = """
            SELECT c.id, c.estrellas, c.comentario, c.visible, c.motivo_moderacion,
                   s.id AS solicitud_id, s.descripcion AS solicitud_descripcion,
                   ci.id AS ciudadano_id, ci.nombre AS ciudadano_nombre,
                   t.id AS tecnico_id, ut.id AS tecnico_usuario_id, ut.nombre AS tecnico_nombre
            FROM Calificacion c
            JOIN Solicitud s ON s.id = c.solicitud_id
            JOIN Usuario ci ON ci.id = s.ciudadano_id
            JOIN Tecnico t ON t.id = s.tecnico_id
            JOIN Usuario ut ON ut.id = t.usuario_id
            WHERE 1 = 1
            """;

    public CalificacionMySqlDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(CalificacionEntidad entidad) {
        try (PreparedStatement sentencia = getConexion().prepareStatement(INSERTAR)) {
            sentencia.setString(1, entidad.getId().toString());
            sentencia.setString(2, entidad.getSolicitud().getId().toString());
            sentencia.setInt(3, entidad.getEstrellas());
            sentencia.setString(4, entidad.getComentario());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            if (excepcion.getErrorCode() == CODIGO_ERROR_DUPLICADO) {
                throw ManosALaObraDatosExcepcion.crear(
                        CatalogoMensajes.CalificacionMySqlDAO.USUARIO_ERROR_SOLICITUD_YA_CALIFICADA,
                        excepcion.getMessage(), excepcion);
            }
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.CalificacionMySqlDAO.USUARIO_ERROR_PROBLEMA_CREANDO_CALIFICACION,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.CalificacionMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CREANDO_CALIFICACION,
                    excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public void actualizar(UUID id, CalificacionEntidad entidad) {
        try (PreparedStatement sentencia = getConexion().prepareStatement(ACTUALIZAR_VISIBILIDAD)) {
            sentencia.setBoolean(1, entidad.isVisible());
            sentencia.setString(2, UtilTexto.getUtilTexto().esVacia(entidad.getMotivoModeracion()) ? null : entidad.getMotivoModeracion());
            sentencia.setString(3, id.toString());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.CalificacionMySqlDAO.USUARIO_ERROR_PROBLEMA_ACTUALIZANDO_CALIFICACION,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.CalificacionMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_ACTUALIZANDO_CALIFICACION,
                    excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public void actualizarPromedioDelTecnico(UUID tecnicoId) {
        try (PreparedStatement sentencia = getConexion().prepareStatement(ACTUALIZAR_PROMEDIO)) {
            sentencia.setString(1, tecnicoId.toString());
            sentencia.setString(2, tecnicoId.toString());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.CalificacionMySqlDAO.USUARIO_ERROR_PROBLEMA_ACTUALIZANDO_PROMEDIO,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.CalificacionMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_ACTUALIZANDO_PROMEDIO,
                    excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public CalificacionEntidad consultarPorId(UUID id) {
        CalificacionEntidad filtro = new CalificacionEntidad();
        filtro.setId(id);
        List<CalificacionEntidad> encontradas = consultarPorFiltro(filtro);
        return encontradas.isEmpty() ? new CalificacionEntidad() : encontradas.get(0);
    }

    // Filtra por id de la calificación, por solicitud y por técnico; sin filtros trae todas (moderación, HU-17)
    @Override
    public List<CalificacionEntidad> consultarPorFiltro(CalificacionEntidad filtro) {
        CalificacionEntidad filtroSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                filtro, new CalificacionEntidad());
        List<String> parametros = new ArrayList<>();
        StringBuilder consulta = new StringBuilder(SELECCIONAR);

        agregarCondicionDeId(consulta, parametros, "c.id", filtroSeguro.getId());
        agregarCondicionDeId(consulta, parametros, "c.solicitud_id", filtroSeguro.getSolicitud().getId());
        agregarCondicionDeId(consulta, parametros, "s.tecnico_id", filtroSeguro.getSolicitud().getTecnico().getId());
        consulta.append(" ORDER BY c.creada_en DESC");

        return ejecutarConsulta(consulta.toString(), parametros);
    }

    @Override
    public List<CalificacionEntidad> consultarTodos() {
        return consultarPorFiltro(new CalificacionEntidad());
    }

    private void agregarCondicionDeId(StringBuilder consulta, List<String> parametros, String columna, UUID id) {
        if (!UtilUUID.esValorDefecto(id)) {
            consulta.append(" AND ").append(columna).append(" = ?");
            parametros.add(id.toString());
        }
    }

    private List<CalificacionEntidad> ejecutarConsulta(String consulta, List<String> parametros) {
        List<CalificacionEntidad> calificaciones = new ArrayList<>();
        try (PreparedStatement sentencia = getConexion().prepareStatement(consulta)) {
            for (int posicion = 0; posicion < parametros.size(); posicion++) {
                sentencia.setString(posicion + 1, parametros.get(posicion));
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    calificaciones.add(convertirFilaAEntidad(resultado));
                }
            }
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.CalificacionMySqlDAO.USUARIO_ERROR_PROBLEMA_CONSULTANDO_CALIFICACION,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.CalificacionMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_CALIFICACION,
                    excepcion.getMessage(), excepcion);
        }
        return calificaciones;
    }

    private CalificacionEntidad convertirFilaAEntidad(ResultSet fila) throws SQLException {
        UsuarioEntidad ciudadano = new UsuarioEntidad();
        ciudadano.setId(UtilUUID.convertirAUUID(fila.getString("ciudadano_id")));
        ciudadano.setNombre(fila.getString("ciudadano_nombre"));

        UsuarioEntidad usuarioTecnico = new UsuarioEntidad();
        usuarioTecnico.setId(UtilUUID.convertirAUUID(fila.getString("tecnico_usuario_id")));
        usuarioTecnico.setNombre(fila.getString("tecnico_nombre"));
        TecnicoEntidad tecnico = new TecnicoEntidad();
        tecnico.setId(UtilUUID.convertirAUUID(fila.getString("tecnico_id")));
        tecnico.setUsuario(usuarioTecnico);

        SolicitudEntidad solicitud = new SolicitudEntidad();
        solicitud.setId(UtilUUID.convertirAUUID(fila.getString("solicitud_id")));
        solicitud.setDescripcion(fila.getString("solicitud_descripcion"));
        solicitud.setCiudadano(ciudadano);
        solicitud.setTecnico(tecnico);

        CalificacionEntidad calificacion = new CalificacionEntidad(
                UtilUUID.convertirAUUID(fila.getString("id")),
                solicitud,
                fila.getInt("estrellas"),
                fila.getString("comentario"),
                fila.getBoolean("visible"));
        calificacion.setMotivoModeracion(fila.getString("motivo_moderacion"));
        return calificacion;
    }
}
