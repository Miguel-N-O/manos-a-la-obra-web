package com.manosalaobra.negocio.negocio.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.manosalaobra.dao.datos.entidad.BarrioDAO;
import com.manosalaobra.dao.datos.entidad.EstadoSolicitudDAO;
import com.manosalaobra.dao.datos.entidad.SolicitudDAO;
import com.manosalaobra.dao.datos.entidad.TecnicoDAO;
import com.manosalaobra.dominio.BarrioDominio;
import com.manosalaobra.dominio.EstadoSolicitudDominio;
import com.manosalaobra.dominio.SolicitudDominio;
import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.entidad.BarrioEntidad;
import com.manosalaobra.entidad.CiudadEntidad;
import com.manosalaobra.entidad.EstadoSolicitudEntidad;
import com.manosalaobra.entidad.SolicitudEntidad;
import com.manosalaobra.entidad.TecnicoEntidad;
import com.manosalaobra.pruebas.FabricaDeDAOParaPruebas;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilFecha;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SolicitudNegocioImplTest {

    private static final UUID ID_TECNICO = UtilUUID.generar();
    private static final UUID ID_USUARIO_TECNICO = UtilUUID.generar();
    private static final UUID ID_BARRIO = UtilUUID.generar();
    private static final UUID ID_PENDIENTE = UtilUUID.generar();
    private static final UUID ID_ACEPTADA = UtilUUID.generar();
    private static final UUID ID_RECHAZADA = UtilUUID.generar();

    // Guarda en memoria lo que se crea y responde las consultas filtrando por id y ciudadano
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
                boolean mismoTecnico = UtilUUID.esValorDefecto(filtro.getTecnico().getId())
                        || filtro.getTecnico().getId().equals(solicitud.getTecnico().getId());
                if (mismoId && mismoCiudadano && mismoTecnico) {
                    encontradas.add(solicitud);
                }
            }
            return encontradas;
        }

        @Override
        public List<SolicitudEntidad> consultarTodos() {
            return solicitudes;
        }

        // Igual que el UPDATE real: solo cambia el estado, el motivo y la fecha de respuesta
        @Override
        public void actualizar(UUID id, SolicitudEntidad entidad) {
            for (SolicitudEntidad solicitud : solicitudes) {
                if (id.equals(solicitud.getId())) {
                    solicitud.setEstadoSolicitud(entidad.getEstadoSolicitud());
                    solicitud.setMotivoRechazo(entidad.getMotivoRechazo());
                    solicitud.setRespondidaEn(entidad.getRespondidaEn());
                }
            }
        }

        @Override
        public void marcarComoNotificadas(UUID tecnicoId) {
            for (SolicitudEntidad solicitud : solicitudes) {
                if (tecnicoId.equals(solicitud.getTecnico().getId())) {
                    solicitud.setNotificada(true);
                }
            }
        }
    }

    // Solo existe el técnico ID_TECNICO (usuario ID_USUARIO_TECNICO), activo y con perfil
    private static TecnicoDAO tecnicoDAO() {
        return new TecnicoDAO() {
            @Override
            public List<TecnicoEntidad> consultarPorFiltro(TecnicoEntidad filtro) {
                TecnicoEntidad tecnico = new TecnicoEntidad();
                tecnico.setId(ID_TECNICO);
                return ID_TECNICO.equals(filtro.getId()) ? List.of(tecnico) : List.of();
            }

            @Override
            public TecnicoEntidad consultarPorId(UUID id) {
                return new TecnicoEntidad();
            }

            @Override
            public List<TecnicoEntidad> consultarTodos() {
                return List.of();
            }

            @Override
            public void crear(TecnicoEntidad entidad) {
            }

            @Override
            public boolean existeDocumento(String documento) {
                return false;
            }

            @Override
            public TecnicoEntidad consultarPorUsuario(UUID usuarioId) {
                TecnicoEntidad tecnico = new TecnicoEntidad();
                if (ID_USUARIO_TECNICO.equals(usuarioId)) {
                    tecnico.setId(ID_TECNICO);
                }
                return tecnico;
            }

            @Override
            public void actualizar(UUID id, TecnicoEntidad entidad) {
            }
        };
    }

    private static BarrioDAO barrioDAO() {
        return new BarrioDAO() {
            @Override
            public BarrioEntidad consultarPorId(UUID id) {
                return ID_BARRIO.equals(id) ? new BarrioEntidad(ID_BARRIO, new CiudadEntidad(), "Centro", true)
                        : new BarrioEntidad();
            }

            @Override
            public List<BarrioEntidad> consultarPorFiltro(BarrioEntidad filtro) {
                return List.of();
            }

            @Override
            public List<BarrioEntidad> consultarTodos() {
                return List.of();
            }
        };
    }

    private static EstadoSolicitudDAO estadoSolicitudDAO() {
        return new EstadoSolicitudDAO() {
            @Override
            public List<EstadoSolicitudEntidad> consultarPorFiltro(EstadoSolicitudEntidad filtro) {
                if (EstadoSolicitudDominio.PENDIENTE.equals(filtro.getNombre())) {
                    return List.of(new EstadoSolicitudEntidad(ID_PENDIENTE, EstadoSolicitudDominio.PENDIENTE, true));
                }
                if (EstadoSolicitudDominio.ACEPTADA.equals(filtro.getNombre())) {
                    return List.of(new EstadoSolicitudEntidad(ID_ACEPTADA, EstadoSolicitudDominio.ACEPTADA, true));
                }
                if (EstadoSolicitudDominio.RECHAZADA.equals(filtro.getNombre())) {
                    return List.of(new EstadoSolicitudEntidad(ID_RECHAZADA, EstadoSolicitudDominio.RECHAZADA, true));
                }
                return List.of();
            }

            @Override
            public EstadoSolicitudEntidad consultarPorId(UUID id) {
                return new EstadoSolicitudEntidad();
            }

            @Override
            public List<EstadoSolicitudEntidad> consultarTodos() {
                return List.of();
            }
        };
    }

    private final SolicitudDAOEnMemoria solicitudDAO = new SolicitudDAOEnMemoria();
    private final SolicitudNegocioImpl negocio = new SolicitudNegocioImpl(new FabricaDeDAOParaPruebas()
            .conSolicitudDAO(solicitudDAO).conTecnicoDAO(tecnicoDAO()).conBarrioDAO(barrioDAO())
            .conEstadoSolicitudDAO(estadoSolicitudDAO()));

    private static SolicitudDominio.Builder solicitudPara(UUID idTecnico) {
        return new SolicitudDominio.Builder()
                .tecnico(new TecnicoDominio.Builder().id(idTecnico).build())
                .barrio(new BarrioDominio.Builder().id(ID_BARRIO).build())
                .descripcion("Cambiar el breaker que se dispara.")
                .fechaHora(LocalDateTime.now().plusDays(1));
    }

    @Test
    void creaLaSolicitudPendienteParaElCiudadanoDeLaSesion() {
        UUID ciudadano = UtilUUID.generar();
        // Aunque llegue otro ciudadano en los datos, se usa el de la sesión
        SolicitudDominio conOtroCiudadano = solicitudPara(ID_TECNICO)
                .ciudadano(new UsuarioDominio.Builder().id(UtilUUID.generar()).build()).build();

        negocio.crear(ciudadano, conOtroCiudadano);

        assertEquals(1, solicitudDAO.solicitudes.size());
        SolicitudEntidad creada = solicitudDAO.solicitudes.get(0);
        assertEquals(ciudadano, creada.getCiudadano().getId());
        assertEquals(ID_TECNICO, creada.getTecnico().getId());
        assertEquals(ID_BARRIO, creada.getBarrio().getId());
        assertEquals(ID_PENDIENTE, creada.getEstadoSolicitud().getId());
    }

    @Test
    void noCreaLaSolicitudSiElTecnicoNoEstaDisponible() {
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.crear(UtilUUID.generar(), solicitudPara(UtilUUID.generar()).build()));
        assertEquals("El técnico que elegiste no está disponible. Busca otro técnico.", excepcion.getMensajeUsuario());
        assertEquals(0, solicitudDAO.solicitudes.size());
    }

    @Test
    void noCreaLaSolicitudConFechaPasada() {
        assertThrows(ManosALaObraExcepcion.class, () -> negocio.crear(UtilUUID.generar(),
                solicitudPara(ID_TECNICO).fechaHora(LocalDateTime.now().minusHours(1)).build()));
        assertEquals(0, solicitudDAO.solicitudes.size());
    }

    @Test
    void elCiudadanoSoloVeSusPropiasSolicitudes() {
        UUID ana = UtilUUID.generar();
        UUID beto = UtilUUID.generar();
        SolicitudDominio deAna = negocio.crear(ana, solicitudPara(ID_TECNICO).build());
        negocio.crear(beto, solicitudPara(ID_TECNICO).build());

        assertEquals(1, negocio.consultarDelCiudadano(ana).size());
        assertEquals(deAna.getId(), negocio.consultarUnaDelCiudadano(ana, deAna.getId()).getId());
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.consultarUnaDelCiudadano(beto, deAna.getId()));
        assertEquals("No encontramos esa solicitud.", excepcion.getMensajeUsuario());
    }

    @Test
    void elTecnicoVeSusSolicitudesConLasPendientesPrimero() {
        SolicitudDominio primera = negocio.crear(UtilUUID.generar(), solicitudPara(ID_TECNICO).build());
        SolicitudDominio segunda = negocio.crear(UtilUUID.generar(), solicitudPara(ID_TECNICO).build());
        // El DAO entrega primero la rechazada, pero la pendiente debe quedar arriba
        solicitudDAO.solicitudes.get(0).setEstadoSolicitud(
                new EstadoSolicitudEntidad(UtilUUID.generar(), EstadoSolicitudDominio.RECHAZADA, true));

        List<SolicitudDominio> recibidas = negocio.consultarRecibidas(ID_USUARIO_TECNICO);

        assertEquals(2, recibidas.size());
        assertEquals(segunda.getId(), recibidas.get(0).getId());
        assertEquals(primera.getId(), recibidas.get(1).getId());
    }

    @Test
    void elTecnicoSoloVeLasSolicitudesDirigidasAEl() {
        SolicitudDominio suya = negocio.crear(UtilUUID.generar(), solicitudPara(ID_TECNICO).build());
        SolicitudEntidad deOtroTecnico = new SolicitudEntidad();
        deOtroTecnico.setId(UtilUUID.generar());
        deOtroTecnico.getTecnico().setId(UtilUUID.generar());
        solicitudDAO.solicitudes.add(deOtroTecnico);

        assertEquals(1, negocio.consultarRecibidas(ID_USUARIO_TECNICO).size());
        assertEquals(suya.getId(), negocio.consultarUnaRecibida(ID_USUARIO_TECNICO, suya.getId()).getId());
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.consultarUnaRecibida(ID_USUARIO_TECNICO, deOtroTecnico.getId()));
        assertEquals("No encontramos esa solicitud.", excepcion.getMensajeUsuario());
    }

    @Test
    void sinRegistroDeTecnicoNoConsultaSolicitudesRecibidas() {
        negocio.crear(UtilUUID.generar(), solicitudPara(ID_TECNICO).build());
        assertThrows(ManosALaObraExcepcion.class, () -> negocio.consultarRecibidas(UtilUUID.generar()));
        assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.consultarRecibidas(UtilUUID.obtenerUUIDDefecto()));
    }

    @Test
    void marcaComoNotificadasSoloLasDelTecnico() {
        negocio.crear(UtilUUID.generar(), solicitudPara(ID_TECNICO).build());
        SolicitudEntidad deOtroTecnico = new SolicitudEntidad();
        deOtroTecnico.getTecnico().setId(UtilUUID.generar());
        solicitudDAO.solicitudes.add(deOtroTecnico);

        negocio.marcarRecibidasComoNotificadas(ID_USUARIO_TECNICO);

        assertTrue(solicitudDAO.solicitudes.get(0).isNotificada());
        assertFalse(deOtroTecnico.isNotificada());
    }

    @Test
    void aceptaUnaSolicitudPendienteYRegistraLaFechaDeRespuesta() {
        SolicitudDominio solicitud = negocio.crear(UtilUUID.generar(), solicitudPara(ID_TECNICO).build());

        SolicitudDominio aceptada = negocio.aceptar(ID_USUARIO_TECNICO, solicitud.getId());

        assertEquals(EstadoSolicitudDominio.ACEPTADA, aceptada.getEstadoSolicitud().getNombre());
        assertEquals(ID_ACEPTADA, solicitudDAO.solicitudes.get(0).getEstadoSolicitud().getId());
        assertFalse(UtilFecha.esValorDefecto(solicitudDAO.solicitudes.get(0).getRespondidaEn()));
    }

    @Test
    void noAceptaUnaSolicitudQueYaFueRespondida() {
        SolicitudDominio solicitud = negocio.crear(UtilUUID.generar(), solicitudPara(ID_TECNICO).build());
        negocio.aceptar(ID_USUARIO_TECNICO, solicitud.getId());

        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.aceptar(ID_USUARIO_TECNICO, solicitud.getId()));
        assertEquals("Esta solicitud ya fue respondida y no puede modificarse.", excepcion.getMensajeUsuario());
    }

    @Test
    void noAceptaUnaSolicitudDeOtroTecnico() {
        SolicitudEntidad deOtroTecnico = new SolicitudEntidad();
        deOtroTecnico.setId(UtilUUID.generar());
        deOtroTecnico.getTecnico().setId(UtilUUID.generar());
        deOtroTecnico.getEstadoSolicitud().setNombre(EstadoSolicitudDominio.PENDIENTE);
        solicitudDAO.solicitudes.add(deOtroTecnico);

        assertThrows(ManosALaObraExcepcion.class, () -> negocio.aceptar(ID_USUARIO_TECNICO, deOtroTecnico.getId()));
        assertEquals(EstadoSolicitudDominio.PENDIENTE, deOtroTecnico.getEstadoSolicitud().getNombre());
    }

    @Test
    void elTecnicoSoloVeElTelefonoDelCiudadanoCuandoAceptaLaSolicitud() {
        UUID ciudadano = UtilUUID.generar();
        SolicitudDominio solicitud = negocio.crear(ciudadano, solicitudPara(ID_TECNICO).build());
        solicitudDAO.solicitudes.get(0).getCiudadano().setNumeroTelefono("3001234567");

        assertEquals("", negocio.consultarUnaRecibida(ID_USUARIO_TECNICO, solicitud.getId())
                .getCiudadano().getNumeroTelefono());
        assertEquals("3001234567", negocio.aceptar(ID_USUARIO_TECNICO, solicitud.getId())
                .getCiudadano().getNumeroTelefono());
        // El ciudadano no recibe su propio teléfono en "Mis solicitudes"
        assertEquals("", negocio.consultarUnaDelCiudadano(ciudadano, solicitud.getId())
                .getCiudadano().getNumeroTelefono());
    }

    @Test
    void rechazaUnaSolicitudPendienteGuardandoElMotivo() {
        SolicitudDominio solicitud = negocio.crear(UtilUUID.generar(), solicitudPara(ID_TECNICO).build());

        SolicitudDominio rechazada = negocio.rechazar(ID_USUARIO_TECNICO, solicitud.getId(), "FUERA_DE_ZONA");

        assertEquals(EstadoSolicitudDominio.RECHAZADA, rechazada.getEstadoSolicitud().getNombre());
        SolicitudEntidad guardada = solicitudDAO.solicitudes.get(0);
        assertEquals(ID_RECHAZADA, guardada.getEstadoSolicitud().getId());
        assertEquals("FUERA_DE_ZONA", guardada.getMotivoRechazo());
        assertFalse(UtilFecha.esValorDefecto(guardada.getRespondidaEn()));
    }

    @Test
    void rechazaSinMotivoPorqueEsOpcional() {
        SolicitudDominio solicitud = negocio.crear(UtilUUID.generar(), solicitudPara(ID_TECNICO).build());

        negocio.rechazar(ID_USUARIO_TECNICO, solicitud.getId(), "");

        assertEquals(ID_RECHAZADA, solicitudDAO.solicitudes.get(0).getEstadoSolicitud().getId());
        assertEquals("", solicitudDAO.solicitudes.get(0).getMotivoRechazo());
    }

    @Test
    void noRechazaConUnMotivoQueNoEstaEnLaLista() {
        SolicitudDominio solicitud = negocio.crear(UtilUUID.generar(), solicitudPara(ID_TECNICO).build());

        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.rechazar(ID_USUARIO_TECNICO, solicitud.getId(), "NO_ME_GUSTA"));
        assertEquals("El motivo del rechazo no es válido. Elige uno de la lista o deja el campo vacío.",
                excepcion.getMensajeUsuario());
        assertEquals(ID_PENDIENTE, solicitudDAO.solicitudes.get(0).getEstadoSolicitud().getId());
    }

    @Test
    void noRechazaUnaSolicitudQueYaFueAceptadaYNoMuestraElContacto() {
        SolicitudDominio aceptada = negocio.crear(UtilUUID.generar(), solicitudPara(ID_TECNICO).build());
        negocio.aceptar(ID_USUARIO_TECNICO, aceptada.getId());
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.rechazar(ID_USUARIO_TECNICO, aceptada.getId(), "OTRO"));
        assertEquals("Esta solicitud ya fue respondida y no puede modificarse.", excepcion.getMensajeUsuario());

        // CU12: al rechazar no se habilitan los datos de contacto
        SolicitudDominio otra = negocio.crear(UtilUUID.generar(), solicitudPara(ID_TECNICO).build());
        solicitudDAO.solicitudes.get(1).getCiudadano().setNumeroTelefono("3001234567");
        assertEquals("", negocio.rechazar(ID_USUARIO_TECNICO, otra.getId(), "OTRO").getCiudadano().getNumeroTelefono());
    }

    @Test
    void sinCiudadanoNoConsultaNada() {
        negocio.crear(UtilUUID.generar(), solicitudPara(ID_TECNICO).build());
        assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.consultarDelCiudadano(UtilUUID.obtenerUUIDDefecto()));
    }
}
