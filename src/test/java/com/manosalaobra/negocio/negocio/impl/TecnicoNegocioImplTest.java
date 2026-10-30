package com.manosalaobra.negocio.negocio.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.manosalaobra.dao.datos.entidad.BarrioDAO;
import com.manosalaobra.dao.datos.entidad.ServicioDAO;
import com.manosalaobra.dao.datos.entidad.TecnicoDAO;
import com.manosalaobra.dominio.BarrioDominio;
import com.manosalaobra.dominio.EstadoUsuarioDominio;
import com.manosalaobra.dominio.ServicioDominio;
import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.entidad.BarrioEntidad;
import com.manosalaobra.entidad.CiudadEntidad;
import com.manosalaobra.entidad.ServicioEntidad;
import com.manosalaobra.entidad.TecnicoEntidad;
import com.manosalaobra.entidad.UsuarioEntidad;
import com.manosalaobra.pruebas.FabricaDeDAOParaPruebas;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TecnicoNegocioImplTest {

    private static final class TecnicoDAOEnMemoria implements TecnicoDAO {
        private final List<TecnicoEntidad> tecnicos;
        private TecnicoEntidad ultimoFiltro;
        private UUID ultimoIdActualizado;
        private TecnicoEntidad ultimaActualizacion;

        private TecnicoDAOEnMemoria(List<TecnicoEntidad> tecnicos) {
            this.tecnicos = tecnicos;
        }

        @Override
        public TecnicoEntidad consultarPorId(UUID id) {
            return new TecnicoEntidad();
        }

        @Override
        public List<TecnicoEntidad> consultarPorFiltro(TecnicoEntidad filtro) {
            ultimoFiltro = filtro;
            return new ArrayList<>(tecnicos);
        }

        @Override
        public List<TecnicoEntidad> consultarTodos() {
            return tecnicos;
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
            for (TecnicoEntidad tecnico : tecnicos) {
                if (tecnico.getUsuario().getId().equals(usuarioId)) {
                    return tecnico;
                }
            }
            return new TecnicoEntidad();
        }

        @Override
        public void actualizar(UUID id, TecnicoEntidad entidad) {
            ultimoIdActualizado = id;
            ultimaActualizacion = entidad;
        }
    }

    // Catálogo de especialidades o barrios donde solo existe el id indicado, activo o no
    private static ServicioDAO servicioDAOCon(UUID id, boolean activo) {
        return new ServicioDAO() {
            @Override
            public ServicioEntidad consultarPorId(UUID buscado) {
                return buscado.equals(id) ? new ServicioEntidad(id, "Plomero", activo) : new ServicioEntidad();
            }

            @Override
            public List<ServicioEntidad> consultarPorFiltro(ServicioEntidad filtro) {
                return List.of();
            }

            @Override
            public List<ServicioEntidad> consultarTodos() {
                return List.of();
            }
        };
    }

    private static BarrioDAO barrioDAOCon(UUID id, boolean activo) {
        return new BarrioDAO() {
            @Override
            public BarrioEntidad consultarPorId(UUID buscado) {
                return buscado.equals(id) ? new BarrioEntidad(id, new CiudadEntidad(), "Centro", activo)
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

    private static TecnicoEntidad tecnicoSinPerfil(UUID idTecnico, UUID idUsuario) {
        UsuarioEntidad usuario = new UsuarioEntidad();
        usuario.setId(idUsuario);
        usuario.setNombre("Pedro Gómez");
        TecnicoEntidad tecnico = new TecnicoEntidad();
        tecnico.setId(idTecnico);
        tecnico.setUsuario(usuario);
        return tecnico;
    }

    private static TecnicoDominio perfil(UUID idServicio, UUID idBarrio) {
        return new TecnicoDominio.Builder()
                .servicio(new ServicioDominio.Builder().id(idServicio).build())
                .barrio(new BarrioDominio.Builder().id(idBarrio).build())
                .tarifa(new BigDecimal("60000")).tipoTarifa(TecnicoDominio.TARIFA_POR_HORA)
                .experiencia(7).descripcion("Instalaciones y reparaciones")
                .build();
    }

    private static TecnicoEntidad tecnico(String nombre, String promedio) {
        UsuarioEntidad usuario = new UsuarioEntidad();
        usuario.setNombre(nombre);
        return new TecnicoEntidad(UtilUUID.generar(), usuario, new ServicioEntidad(), new BarrioEntidad(),
                new BigDecimal("50000"), 3, "", new BigDecimal(promedio));
    }

    private static List<String> nombresDe(List<TecnicoDominio> tecnicos) {
        List<String> nombres = new ArrayList<>();
        for (TecnicoDominio tecnico : tecnicos) {
            nombres.add(tecnico.getUsuario().getNombre());
        }
        return nombres;
    }

    @Test
    void ordenaPorPromedioDescendenteYLuegoPorNombre() {
        TecnicoDAOEnMemoria dao = new TecnicoDAOEnMemoria(List.of(
                tecnico("Zoila", "4.2"), tecnico("Beto", "4.8"), tecnico("Ana", "4.8"), tecnico("Sin notas", "0.0")));
        TecnicoNegocioImpl negocio = new TecnicoNegocioImpl(new FabricaDeDAOParaPruebas().conTecnicoDAO(dao));

        List<TecnicoDominio> resultado = negocio.consultarPorFiltro(new TecnicoDominio.Builder().build());

        assertEquals(List.of("Ana", "Beto", "Zoila", "Sin notas"), nombresDe(resultado));
    }

    @Test
    void consultaSoloTecnicosConUsuarioActivo() {
        TecnicoDAOEnMemoria dao = new TecnicoDAOEnMemoria(List.of());
        TecnicoNegocioImpl negocio = new TecnicoNegocioImpl(new FabricaDeDAOParaPruebas().conTecnicoDAO(dao));

        negocio.consultarPorFiltro(new TecnicoDominio.Builder().build());

        assertEquals(EstadoUsuarioDominio.ACTIVO, dao.ultimoFiltro.getUsuario().getEstadoUsuario().getNombre());
    }

    @Test
    void entregaAlDaoLosFiltrosDeEspecialidadYBarrio() {
        UUID idServicio = UtilUUID.generar();
        UUID idBarrio = UtilUUID.generar();
        TecnicoDAOEnMemoria dao = new TecnicoDAOEnMemoria(List.of());
        TecnicoNegocioImpl negocio = new TecnicoNegocioImpl(new FabricaDeDAOParaPruebas().conTecnicoDAO(dao));

        negocio.consultarPorFiltro(new TecnicoDominio.Builder()
                .servicio(new ServicioDominio.Builder().id(idServicio).build())
                .barrio(new BarrioDominio.Builder().id(idBarrio).build())
                .build());

        assertEquals(idServicio, dao.ultimoFiltro.getServicio().getId());
        assertEquals(idBarrio, dao.ultimoFiltro.getBarrio().getId());
    }

    @Test
    void sinCoincidenciasDevuelveUnaListaVacia() {
        TecnicoNegocioImpl negocio = new TecnicoNegocioImpl(
                new FabricaDeDAOParaPruebas().conTecnicoDAO(new TecnicoDAOEnMemoria(List.of())));

        assertTrue(negocio.consultarPorFiltro(new TecnicoDominio.Builder().build()).isEmpty());
    }

    @Test
    void guardarPerfilActualizaElRegistroDelTecnicoDeLaSesion() {
        UUID idTecnico = UtilUUID.generar();
        UUID idUsuario = UtilUUID.generar();
        UUID idServicio = UtilUUID.generar();
        UUID idBarrio = UtilUUID.generar();
        TecnicoDAOEnMemoria dao = new TecnicoDAOEnMemoria(List.of(tecnicoSinPerfil(idTecnico, idUsuario)));
        TecnicoNegocioImpl negocio = new TecnicoNegocioImpl(new FabricaDeDAOParaPruebas().conTecnicoDAO(dao)
                .conServicioDAO(servicioDAOCon(idServicio, true)).conBarrioDAO(barrioDAOCon(idBarrio, true)));

        // Aunque el perfil traiga otro id, se actualiza el técnico del usuario que tiene la sesión
        TecnicoDominio conOtroId = new TecnicoDominio.Builder().id(UtilUUID.generar())
                .servicio(perfil(idServicio, idBarrio).getServicio()).barrio(perfil(idServicio, idBarrio).getBarrio())
                .tarifa(new BigDecimal("60000")).tipoTarifa("POR_HORA").experiencia(7)
                .descripcion("Instalaciones y reparaciones").build();
        negocio.guardarPerfil(idUsuario, conOtroId);

        assertEquals(idTecnico, dao.ultimoIdActualizado);
        assertEquals(idServicio, dao.ultimaActualizacion.getServicio().getId());
        assertEquals(idBarrio, dao.ultimaActualizacion.getBarrio().getId());
        assertEquals(0, new BigDecimal("60000").compareTo(dao.ultimaActualizacion.getTarifa()));
        assertEquals("POR_HORA", dao.ultimaActualizacion.getTipoTarifa());
        assertEquals(7, dao.ultimaActualizacion.getExperiencia());
        assertEquals("Instalaciones y reparaciones", dao.ultimaActualizacion.getDescripcion());
    }

    @Test
    void guardarPerfilConEspecialidadInactivaNoActualiza() {
        UUID idUsuario = UtilUUID.generar();
        UUID idServicio = UtilUUID.generar();
        UUID idBarrio = UtilUUID.generar();
        TecnicoDAOEnMemoria dao = new TecnicoDAOEnMemoria(List.of(tecnicoSinPerfil(UtilUUID.generar(), idUsuario)));
        TecnicoNegocioImpl negocio = new TecnicoNegocioImpl(new FabricaDeDAOParaPruebas().conTecnicoDAO(dao)
                .conServicioDAO(servicioDAOCon(idServicio, false)).conBarrioDAO(barrioDAOCon(idBarrio, true)));

        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.guardarPerfil(idUsuario, perfil(idServicio, idBarrio)));
        assertEquals("La especialidad elegida no está disponible. Elige una de la lista.",
                excepcion.getMensajeUsuario());
        assertNull(dao.ultimaActualizacion);
    }

    @Test
    void guardarPerfilConBarrioQueNoExisteNoActualiza() {
        UUID idUsuario = UtilUUID.generar();
        UUID idServicio = UtilUUID.generar();
        TecnicoDAOEnMemoria dao = new TecnicoDAOEnMemoria(List.of(tecnicoSinPerfil(UtilUUID.generar(), idUsuario)));
        TecnicoNegocioImpl negocio = new TecnicoNegocioImpl(new FabricaDeDAOParaPruebas().conTecnicoDAO(dao)
                .conServicioDAO(servicioDAOCon(idServicio, true)).conBarrioDAO(barrioDAOCon(UtilUUID.generar(), true)));

        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.guardarPerfil(idUsuario, perfil(idServicio, UtilUUID.generar())));
        assertEquals("El barrio elegido no está disponible. Elige uno de la lista.", excepcion.getMensajeUsuario());
        assertNull(dao.ultimaActualizacion);
    }

    @Test
    void consultarPorUsuarioSinRegistroDeTecnicoLanzaExcepcion() {
        TecnicoNegocioImpl negocio = new TecnicoNegocioImpl(
                new FabricaDeDAOParaPruebas().conTecnicoDAO(new TecnicoDAOEnMemoria(List.of())));

        assertThrows(ManosALaObraExcepcion.class, () -> negocio.consultarPorUsuario(UtilUUID.generar()));
    }

    @Test
    void elTecnicoRecienRegistradoNoTienePerfil() {
        UUID idUsuario = UtilUUID.generar();
        TecnicoNegocioImpl negocio = new TecnicoNegocioImpl(new FabricaDeDAOParaPruebas()
                .conTecnicoDAO(new TecnicoDAOEnMemoria(List.of(tecnicoSinPerfil(UtilUUID.generar(), idUsuario)))));

        assertFalse(negocio.consultarPorUsuario(idUsuario).tienePerfil());
    }
}
