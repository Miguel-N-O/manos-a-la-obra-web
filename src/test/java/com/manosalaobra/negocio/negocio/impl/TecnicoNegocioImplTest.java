package com.manosalaobra.negocio.negocio.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.manosalaobra.dao.datos.entidad.TecnicoDAO;
import com.manosalaobra.dominio.BarrioDominio;
import com.manosalaobra.dominio.EstadoUsuarioDominio;
import com.manosalaobra.dominio.ServicioDominio;
import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.entidad.BarrioEntidad;
import com.manosalaobra.entidad.ServicioEntidad;
import com.manosalaobra.entidad.TecnicoEntidad;
import com.manosalaobra.entidad.UsuarioEntidad;
import com.manosalaobra.pruebas.FabricaDeDAOParaPruebas;
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
}
