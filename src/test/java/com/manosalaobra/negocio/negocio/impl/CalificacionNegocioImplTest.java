package com.manosalaobra.negocio.negocio.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.manosalaobra.dao.datos.entidad.CalificacionDAO;
import com.manosalaobra.dao.datos.entidad.SolicitudDAO;
import com.manosalaobra.dominio.CalificacionDominio;
import com.manosalaobra.dominio.EstadoSolicitudDominio;
import com.manosalaobra.dominio.SolicitudDominio;
import com.manosalaobra.entidad.CalificacionEntidad;
import com.manosalaobra.entidad.SolicitudEntidad;
import com.manosalaobra.pruebas.FabricaDeDAOParaPruebas;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CalificacionNegocioImplTest {

    private static final UUID ID_TECNICO = UtilUUID.generar();

    // Solo responde por id de solicitud y por ciudadano, como el DAO real
    private static final class SolicitudDAOEnMemoria implements SolicitudDAO {
        private final List<SolicitudEntidad> solicitudes = new ArrayList<>();

        @Override
        public void crear(SolicitudEntidad entidad) {
            solicitudes.add(entidad);
        }

        @Override
        public SolicitudEntidad consultarPorId(UUID id) {
            return new SolicitudEntidad();
        }

        @Override
        public List<SolicitudEntidad> consultarPorFiltro(SolicitudEntidad filtro) {
            List<SolicitudEntidad> encontradas = new ArrayList<>();
            for (SolicitudEntidad solicitud : solicitudes) {
                boolean mismoId = UtilUUID.esValorDefecto(filtro.getId()) || filtro.getId().equals(solicitud.getId());
                boolean mismoCiudadano = UtilUUID.esValorDefecto(filtro.getCiudadano().getId())
                        || filtro.getCiudadano().getId().equals(solicitud.getCiudadano().getId());
                if (mismoId && mismoCiudadano) {
                    encontradas.add(solicitud);
                }
            }
            return encontradas;
        }

        @Override
        public List<SolicitudEntidad> consultarTodos() {
            return solicitudes;
        }

        @Override
        public void actualizar(UUID id, SolicitudEntidad entidad) {
        }

        @Override
        public void marcarComoNotificadas(UUID tecnicoId) {
        }
    }

    // Guarda en memoria y anota a qué técnicos se les recalculó el promedio
    private static final class CalificacionDAOEnMemoria implements CalificacionDAO {
        private final List<CalificacionEntidad> calificaciones = new ArrayList<>();
        private final List<UUID> tecnicosConPromedioActualizado = new ArrayList<>();

        @Override
        public void crear(CalificacionEntidad entidad) {
            calificaciones.add(entidad);
        }

        @Override
        public CalificacionEntidad consultarPorId(UUID id) {
            for (CalificacionEntidad calificacion : calificaciones) {
                if (id.equals(calificacion.getId())) {
                    return calificacion;
                }
            }
            return new CalificacionEntidad();
        }

        @Override
        public List<CalificacionEntidad> consultarPorFiltro(CalificacionEntidad filtro) {
            List<CalificacionEntidad> encontradas = new ArrayList<>();
            for (CalificacionEntidad calificacion : calificaciones) {
                boolean mismaSolicitud = UtilUUID.esValorDefecto(filtro.getSolicitud().getId())
                        || filtro.getSolicitud().getId().equals(calificacion.getSolicitud().getId());
                boolean mismoTecnico = UtilUUID.esValorDefecto(filtro.getSolicitud().getTecnico().getId())
                        || filtro.getSolicitud().getTecnico().getId()
                                .equals(calificacion.getSolicitud().getTecnico().getId());
                if (mismaSolicitud && mismoTecnico) {
                    encontradas.add(calificacion);
                }
            }
            return encontradas;
        }

        @Override
        public List<CalificacionEntidad> consultarTodos() {
            return calificaciones;
        }

        @Override
        public void actualizar(UUID id, CalificacionEntidad entidad) {
            CalificacionEntidad guardada = consultarPorId(id);
            guardada.setVisible(entidad.isVisible());
            guardada.setMotivoModeracion(entidad.getMotivoModeracion());
        }

        @Override
        public void actualizarPromedioDelTecnico(UUID tecnicoId) {
            tecnicosConPromedioActualizado.add(tecnicoId);
        }
    }

    private final SolicitudDAOEnMemoria solicitudDAO = new SolicitudDAOEnMemoria();
    private final CalificacionDAOEnMemoria calificacionDAO = new CalificacionDAOEnMemoria();
    private final CalificacionNegocioImpl negocio = new CalificacionNegocioImpl(new FabricaDeDAOParaPruebas()
            .conSolicitudDAO(solicitudDAO).conCalificacionDAO(calificacionDAO));

    private SolicitudEntidad solicitud(UUID ciudadano, String estado) {
        SolicitudEntidad solicitud = new SolicitudEntidad();
        solicitud.setId(UtilUUID.generar());
        solicitud.getCiudadano().setId(ciudadano);
        solicitud.getTecnico().setId(ID_TECNICO);
        solicitud.getEstadoSolicitud().setNombre(estado);
        solicitudDAO.solicitudes.add(solicitud);
        return solicitud;
    }

    private static CalificacionDominio calificacionPara(SolicitudEntidad solicitud, int estrellas, String comentario) {
        return new CalificacionDominio.Builder()
                .solicitud(new SolicitudDominio.Builder().id(solicitud.getId()).build())
                .estrellas(estrellas)
                .comentario(comentario)
                .build();
    }

    @Test
    void calificaUnServicioFinalizadoYActualizaElPromedioDelTecnico() {
        UUID ciudadano = UtilUUID.generar();
        SolicitudEntidad finalizada = solicitud(ciudadano, EstadoSolicitudDominio.FINALIZADA);

        CalificacionDominio registrada = negocio.calificar(ciudadano, calificacionPara(finalizada, 5, "Excelente."));

        assertEquals(1, calificacionDAO.calificaciones.size());
        assertEquals(5, registrada.getEstrellas());
        assertEquals("Excelente.", registrada.getComentario());
        assertTrue(registrada.isVisible());
        assertEquals(List.of(ID_TECNICO), calificacionDAO.tecnicosConPromedioActualizado);
    }

    @Test
    void calificaSinComentario() {
        UUID ciudadano = UtilUUID.generar();
        SolicitudEntidad finalizada = solicitud(ciudadano, EstadoSolicitudDominio.FINALIZADA);

        negocio.calificar(ciudadano, calificacionPara(finalizada, 3, ""));

        assertEquals("", calificacionDAO.calificaciones.get(0).getComentario());
    }

    @Test
    void noCalificaUnServicioQueNoEstaFinalizado() {
        UUID ciudadano = UtilUUID.generar();
        for (String estado : new String[] {EstadoSolicitudDominio.PENDIENTE, EstadoSolicitudDominio.ACEPTADA,
                EstadoSolicitudDominio.RECHAZADA}) {
            SolicitudEntidad solicitud = solicitud(ciudadano, estado);
            ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                    () -> negocio.calificar(ciudadano, calificacionPara(solicitud, 5, "")));
            assertEquals("Solo puedes calificar al técnico cuando el servicio ya fue finalizado.",
                    excepcion.getMensajeUsuario());
        }
        assertEquals(0, calificacionDAO.calificaciones.size());
        assertEquals(0, calificacionDAO.tecnicosConPromedioActualizado.size());
    }

    @Test
    void noCalificaDosVecesLaMismaSolicitud() {
        UUID ciudadano = UtilUUID.generar();
        SolicitudEntidad finalizada = solicitud(ciudadano, EstadoSolicitudDominio.FINALIZADA);
        negocio.calificar(ciudadano, calificacionPara(finalizada, 4, ""));

        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.calificar(ciudadano, calificacionPara(finalizada, 1, "Cambié de opinión.")));
        assertEquals("Ya calificaste este servicio.", excepcion.getMensajeUsuario());
        assertEquals(1, calificacionDAO.calificaciones.size());
    }

    @Test
    void noCalificaUnaSolicitudDeOtroCiudadano() {
        SolicitudEntidad deAna = solicitud(UtilUUID.generar(), EstadoSolicitudDominio.FINALIZADA);

        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.calificar(UtilUUID.generar(), calificacionPara(deAna, 5, "")));
        assertEquals("No encontramos esa solicitud.", excepcion.getMensajeUsuario());
        assertEquals(0, calificacionDAO.calificaciones.size());
    }

    @Test
    void noCalificaConEstrellasInvalidasNiSinSesion() {
        UUID ciudadano = UtilUUID.generar();
        SolicitudEntidad finalizada = solicitud(ciudadano, EstadoSolicitudDominio.FINALIZADA);

        assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.calificar(ciudadano, calificacionPara(finalizada, 0, "")));
        assertThrows(ManosALaObraExcepcion.class, () -> negocio.calificar(UtilUUID.obtenerUUIDDefecto(),
                calificacionPara(finalizada, 5, "")));
        assertEquals(0, calificacionDAO.calificaciones.size());
    }

    @Test
    void elPerfilDelTecnicoMuestraSusCalificacionesPeroNoElComentarioOculto() {
        UUID ciudadano = UtilUUID.generar();
        SolicitudEntidad primera = solicitud(ciudadano, EstadoSolicitudDominio.FINALIZADA);
        SolicitudEntidad segunda = solicitud(ciudadano, EstadoSolicitudDominio.FINALIZADA);
        negocio.calificar(ciudadano, calificacionPara(primera, 5, "Muy bien, gracias."));
        CalificacionDominio ofensiva = negocio.calificar(ciudadano, calificacionPara(segunda, 1, "Comentario ofensivo"));

        negocio.ocultarComentario(ofensiva.getId(), "LENGUAJE_OFENSIVO");
        List<CalificacionDominio> publicas = negocio.consultarDelTecnico(ID_TECNICO);

        assertEquals(2, publicas.size());
        CalificacionDominio oculta = publicas.stream().filter(c -> c.getId().equals(ofensiva.getId())).findFirst().get();
        assertEquals("", oculta.getComentario());
        assertFalse(oculta.isVisible());
        assertEquals(1, oculta.getEstrellas());
        // El administrador sí ve el texto original
        assertEquals("Comentario ofensivo", negocio.consultarTodasParaModeracion().stream()
                .filter(c -> c.getId().equals(ofensiva.getId())).findFirst().get().getComentario());
    }

    @Test
    void elAdministradorPuedeOcultarYVolverAMostrarUnComentario() {
        UUID ciudadano = UtilUUID.generar();
        CalificacionDominio calificacion = negocio.calificar(ciudadano,
                calificacionPara(solicitud(ciudadano, EstadoSolicitudDominio.FINALIZADA), 2, "Servicio regular."));

        CalificacionDominio oculta = negocio.ocultarComentario(calificacion.getId(), "OTRO");
        assertFalse(oculta.isVisible());
        assertEquals("OTRO", oculta.getMotivoModeracion());
        CalificacionDominio publicada = negocio.mostrarComentario(calificacion.getId());
        assertTrue(publicada.isVisible());
        assertEquals("", publicada.getMotivoModeracion());
    }

    @Test
    void paraOcultarUnComentarioElMotivoEsObligatorioYDebeSerUnoDeLaLista() {
        UUID ciudadano = UtilUUID.generar();
        CalificacionDominio calificacion = negocio.calificar(ciudadano,
                calificacionPara(solicitud(ciudadano, EstadoSolicitudDominio.FINALIZADA), 1, "Comentario ofensivo."));
        for (String motivo : new String[] {"", "SPAM", "otro"}) {
            ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                    () -> negocio.ocultarComentario(calificacion.getId(), motivo));
            assertEquals("Elige el motivo: lenguaje ofensivo, información personal, contenido no relacionado u otro.",
                    excepcion.getMensajeUsuario());
        }
        assertTrue(negocio.consultarTodasParaModeracion().get(0).isVisible());
        // El motivo lo ve el administrador, no el público
        negocio.ocultarComentario(calificacion.getId(), "INFORMACION_PERSONAL");
        assertEquals("", negocio.consultarDelTecnico(ID_TECNICO).get(0).getMotivoModeracion());
        assertEquals("INFORMACION_PERSONAL", negocio.consultarTodasParaModeracion().get(0).getMotivoModeracion());
    }

    @Test
    void noModeraUnaCalificacionQueNoExiste() {
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.ocultarComentario(UtilUUID.generar(), "OTRO"));
        assertEquals("No encontramos esa calificación.", excepcion.getMensajeUsuario());
        assertThrows(ManosALaObraExcepcion.class, () -> negocio.mostrarComentario(UtilUUID.obtenerUUIDDefecto()));
    }

    @Test
    void sinTecnicoNoConsultaCalificaciones() {
        assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.consultarDelTecnico(UtilUUID.obtenerUUIDDefecto()));
    }
}
