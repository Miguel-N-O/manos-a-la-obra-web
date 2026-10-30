package com.manosalaobra.negocio.negocio.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SolicitudNegocioImplTest {

    private static final UUID ID_TECNICO = UtilUUID.generar();
    private static final UUID ID_BARRIO = UtilUUID.generar();
    private static final UUID ID_PENDIENTE = UtilUUID.generar();

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
    }

    // Solo existe el técnico ID_TECNICO, activo y con perfil
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
                return new TecnicoEntidad();
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
                return EstadoSolicitudDominio.PENDIENTE.equals(filtro.getNombre())
                        ? List.of(new EstadoSolicitudEntidad(ID_PENDIENTE, EstadoSolicitudDominio.PENDIENTE, true))
                        : List.of();
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
    void sinCiudadanoNoConsultaNada() {
        negocio.crear(UtilUUID.generar(), solicitudPara(ID_TECNICO).build());
        assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.consultarDelCiudadano(UtilUUID.obtenerUUIDDefecto()));
    }
}
