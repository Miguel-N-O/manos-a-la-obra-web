package com.manosalaobra.dao.datos.entidad.mysql;

import com.manosalaobra.dao.datos.entidad.SolicitudDAO;
import com.manosalaobra.dao.datos.entidad.SqlDAO;
import com.manosalaobra.entidad.BarrioEntidad;
import com.manosalaobra.entidad.CiudadEntidad;
import com.manosalaobra.entidad.EstadoSolicitudEntidad;
import com.manosalaobra.entidad.IndicativoPaisEntidad;
import com.manosalaobra.entidad.ServicioEntidad;
import com.manosalaobra.entidad.SolicitudEntidad;
import com.manosalaobra.entidad.TecnicoEntidad;
import com.manosalaobra.entidad.UsuarioEntidad;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraDatosExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilFecha;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class SolicitudMySqlDAO extends SqlDAO implements SolicitudDAO {

    // HU-09: notificada y creada_en toman su valor por defecto en la base
    private static final String INSERTAR = """
            INSERT INTO Solicitud (id, ciudadano_id, tecnico_id, barrio_id, estado_solicitud_id, descripcion, fecha_hora)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    // HU-10: el técnico ya vio el aviso de sus solicitudes nuevas
    private static final String MARCAR_NOTIFICADAS = """
            UPDATE Solicitud SET notificada = TRUE WHERE tecnico_id = ? AND notificada = FALSE
            """;

    // HU-11 y HU-12: respuesta del técnico (estado, motivo de rechazo y fecha de respuesta)
    private static final String ACTUALIZAR_RESPUESTA = """
            UPDATE Solicitud SET estado_solicitud_id = ?, motivo_rechazo = ?, respondida_en = ? WHERE id = ?
            """;

    // Trae el teléfono del ciudadano, pero el negocio solo lo entrega al técnico si aceptó la solicitud (CU18).
    // El teléfono del técnico no se trae: se entrega al ciudadano en HU-13.
    private static final String SELECCIONAR = """
            SELECT s.id, s.descripcion, s.fecha_hora, s.motivo_rechazo, s.notificada, s.respondida_en, s.creada_en,
                   es.id AS estado_id, es.nombre AS estado_nombre, es.activo AS estado_activo,
                   ci.id AS ciudadano_id, ci.nombre AS ciudadano_nombre, ci.numero_telefono AS ciudadano_telefono,
                   ip.id AS indicativo_id, ip.pais AS indicativo_pais, ip.indicativo AS indicativo_valor,
                   ip.activo AS indicativo_activo,
                   t.id AS tecnico_id, t.tarifa AS tecnico_tarifa, t.tipo_tarifa AS tecnico_tipo_tarifa,
                   ut.id AS tecnico_usuario_id, ut.nombre AS tecnico_nombre,
                   sv.id AS servicio_id, sv.nombre AS servicio_nombre, sv.activo AS servicio_activo,
                   b.id AS barrio_id, b.nombre AS barrio_nombre, b.activo AS barrio_activo,
                   c.id AS ciudad_id, c.nombre AS ciudad_nombre, c.activo AS ciudad_activo
            FROM Solicitud s
            JOIN EstadoSolicitud es ON es.id = s.estado_solicitud_id
            JOIN Usuario ci ON ci.id = s.ciudadano_id
            JOIN IndicativoPais ip ON ip.id = ci.indicativo_pais_id
            JOIN Tecnico t ON t.id = s.tecnico_id
            JOIN Usuario ut ON ut.id = t.usuario_id
            LEFT JOIN Servicio sv ON sv.id = t.servicio_id
            JOIN Barrio b ON b.id = s.barrio_id
            JOIN Ciudad c ON c.id = b.ciudad_id
            WHERE 1 = 1
            """;

    public SolicitudMySqlDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(SolicitudEntidad entidad) {
        try (PreparedStatement sentencia = getConexion().prepareStatement(INSERTAR)) {
            sentencia.setString(1, entidad.getId().toString());
            sentencia.setString(2, entidad.getCiudadano().getId().toString());
            sentencia.setString(3, entidad.getTecnico().getId().toString());
            sentencia.setString(4, entidad.getBarrio().getId().toString());
            sentencia.setString(5, entidad.getEstadoSolicitud().getId().toString());
            sentencia.setString(6, entidad.getDescripcion());
            sentencia.setTimestamp(7, Timestamp.valueOf(entidad.getFechaHora()));
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.SolicitudMySqlDAO.USUARIO_ERROR_PROBLEMA_CREANDO_SOLICITUD,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.SolicitudMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CREANDO_SOLICITUD,
                    excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public void actualizar(UUID id, SolicitudEntidad entidad) {
        try (PreparedStatement sentencia = getConexion().prepareStatement(ACTUALIZAR_RESPUESTA)) {
            sentencia.setString(1, entidad.getEstadoSolicitud().getId().toString());
            sentencia.setString(2, UtilTexto.getUtilTexto().esVacia(entidad.getMotivoRechazo())
                    ? null : entidad.getMotivoRechazo());
            sentencia.setTimestamp(3, UtilFecha.esValorDefecto(entidad.getRespondidaEn())
                    ? null : Timestamp.valueOf(entidad.getRespondidaEn()));
            sentencia.setString(4, id.toString());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.SolicitudMySqlDAO.USUARIO_ERROR_PROBLEMA_ACTUALIZANDO_SOLICITUD,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.SolicitudMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_ACTUALIZANDO_SOLICITUD,
                    excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public void marcarComoNotificadas(UUID tecnicoId) {
        try (PreparedStatement sentencia = getConexion().prepareStatement(MARCAR_NOTIFICADAS)) {
            sentencia.setString(1, tecnicoId.toString());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.SolicitudMySqlDAO.USUARIO_ERROR_PROBLEMA_MARCANDO_NOTIFICADAS,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.SolicitudMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_MARCANDO_NOTIFICADAS,
                    excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public SolicitudEntidad consultarPorId(UUID id) {
        SolicitudEntidad filtro = new SolicitudEntidad();
        filtro.setId(id);
        List<SolicitudEntidad> encontradas = consultarPorFiltro(filtro);
        return encontradas.isEmpty() ? new SolicitudEntidad() : encontradas.get(0);
    }

    @Override
    public List<SolicitudEntidad> consultarPorFiltro(SolicitudEntidad filtro) {
        SolicitudEntidad filtroSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(filtro, new SolicitudEntidad());
        List<String> parametros = new ArrayList<>();
        StringBuilder consulta = new StringBuilder(SELECCIONAR);

        agregarCondicionDeId(consulta, parametros, "s.id", filtroSeguro.getId());
        agregarCondicionDeId(consulta, parametros, "s.ciudadano_id", filtroSeguro.getCiudadano().getId());
        agregarCondicionDeId(consulta, parametros, "s.tecnico_id", filtroSeguro.getTecnico().getId());
        if (!UtilTexto.getUtilTexto().esVacia(filtroSeguro.getEstadoSolicitud().getNombre())) {
            consulta.append(" AND es.nombre = ?");
            parametros.add(filtroSeguro.getEstadoSolicitud().getNombre());
        }
        // CU19 y CU18: de la más reciente a la más antigua
        consulta.append(" ORDER BY s.creada_en DESC, s.fecha_hora DESC");

        return ejecutarConsulta(consulta.toString(), parametros);
    }

    @Override
    public List<SolicitudEntidad> consultarTodos() {
        return consultarPorFiltro(new SolicitudEntidad());
    }

    private void agregarCondicionDeId(StringBuilder consulta, List<String> parametros, String columna, UUID id) {
        if (!UtilUUID.esValorDefecto(id)) {
            consulta.append(" AND ").append(columna).append(" = ?");
            parametros.add(id.toString());
        }
    }

    private List<SolicitudEntidad> ejecutarConsulta(String consulta, List<String> parametros) {
        List<SolicitudEntidad> solicitudes = new ArrayList<>();
        try (PreparedStatement sentencia = getConexion().prepareStatement(consulta)) {
            for (int posicion = 0; posicion < parametros.size(); posicion++) {
                sentencia.setString(posicion + 1, parametros.get(posicion));
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    solicitudes.add(convertirFilaAEntidad(resultado));
                }
            }
        } catch (SQLException excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.SolicitudMySqlDAO.USUARIO_ERROR_PROBLEMA_CONSULTANDO_SOLICITUD,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw ManosALaObraDatosExcepcion.crear(
                    CatalogoMensajes.SolicitudMySqlDAO.USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_SOLICITUD,
                    excepcion.getMessage(), excepcion);
        }
        return solicitudes;
    }

    private SolicitudEntidad convertirFilaAEntidad(ResultSet fila) throws SQLException {
        UsuarioEntidad ciudadano = new UsuarioEntidad();
        ciudadano.setId(UtilUUID.convertirAUUID(fila.getString("ciudadano_id")));
        ciudadano.setNombre(fila.getString("ciudadano_nombre"));
        ciudadano.setNumeroTelefono(fila.getString("ciudadano_telefono"));
        ciudadano.setIndicativoPais(new IndicativoPaisEntidad(
                UtilUUID.convertirAUUID(fila.getString("indicativo_id")),
                fila.getString("indicativo_pais"),
                fila.getString("indicativo_valor"),
                fila.getBoolean("indicativo_activo")));

        UsuarioEntidad usuarioTecnico = new UsuarioEntidad();
        usuarioTecnico.setId(UtilUUID.convertirAUUID(fila.getString("tecnico_usuario_id")));
        usuarioTecnico.setNombre(fila.getString("tecnico_nombre"));
        ServicioEntidad servicio = new ServicioEntidad(
                UtilUUID.convertirAUUID(UtilUUID.obtenerValorDefectoComoTexto(fila.getString("servicio_id"))),
                fila.getString("servicio_nombre"),
                fila.getBoolean("servicio_activo"));
        TecnicoEntidad tecnico = new TecnicoEntidad(
                UtilUUID.convertirAUUID(fila.getString("tecnico_id")),
                usuarioTecnico,
                servicio,
                new BarrioEntidad(),
                fila.getBigDecimal("tecnico_tarifa"),
                0,
                UtilTexto.VACIO,
                BigDecimal.ZERO);
        tecnico.setTipoTarifa(fila.getString("tecnico_tipo_tarifa"));

        CiudadEntidad ciudad = new CiudadEntidad(
                UtilUUID.convertirAUUID(fila.getString("ciudad_id")),
                fila.getString("ciudad_nombre"),
                fila.getBoolean("ciudad_activo"));
        BarrioEntidad barrio = new BarrioEntidad(
                UtilUUID.convertirAUUID(fila.getString("barrio_id")),
                ciudad,
                fila.getString("barrio_nombre"),
                fila.getBoolean("barrio_activo"));
        EstadoSolicitudEntidad estado = new EstadoSolicitudEntidad(
                UtilUUID.convertirAUUID(fila.getString("estado_id")),
                fila.getString("estado_nombre"),
                fila.getBoolean("estado_activo"));

        SolicitudEntidad solicitud = new SolicitudEntidad(
                UtilUUID.convertirAUUID(fila.getString("id")),
                ciudadano,
                tecnico,
                fila.getString("descripcion"),
                aFechaHora(fila.getTimestamp("fecha_hora")),
                estado,
                fila.getBoolean("notificada"));
        solicitud.setBarrio(barrio);
        solicitud.setMotivoRechazo(fila.getString("motivo_rechazo"));
        solicitud.setRespondidaEn(aFechaHora(fila.getTimestamp("respondida_en")));
        solicitud.setCreadaEn(aFechaHora(fila.getTimestamp("creada_en")));
        return solicitud;
    }

    private LocalDateTime aFechaHora(Timestamp valor) {
        return UtilObjeto.esNulo(valor) ? null : valor.toLocalDateTime();
    }
}
