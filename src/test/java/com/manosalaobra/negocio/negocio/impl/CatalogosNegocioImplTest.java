package com.manosalaobra.negocio.negocio.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.manosalaobra.dao.datos.entidad.BarrioDAO;
import com.manosalaobra.dao.datos.entidad.ServicioDAO;
import com.manosalaobra.dominio.BarrioDominio;
import com.manosalaobra.dominio.ServicioDominio;
import com.manosalaobra.entidad.BarrioEntidad;
import com.manosalaobra.entidad.CiudadEntidad;
import com.manosalaobra.entidad.ServicioEntidad;
import com.manosalaobra.pruebas.FabricaDeDAOParaPruebas;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CatalogosNegocioImplTest {

    private static ServicioDAO servicioDAOCon(List<ServicioEntidad> servicios) {
        return new ServicioDAO() {
            @Override
            public ServicioEntidad consultarPorId(UUID id) {
                return new ServicioEntidad();
            }

            @Override
            public List<ServicioEntidad> consultarPorFiltro(ServicioEntidad filtro) {
                return servicios;
            }

            @Override
            public List<ServicioEntidad> consultarTodos() {
                return servicios;
            }
        };
    }

    private static BarrioDAO barrioDAOCon(List<BarrioEntidad> barrios) {
        return new BarrioDAO() {
            @Override
            public BarrioEntidad consultarPorId(UUID id) {
                return new BarrioEntidad();
            }

            @Override
            public List<BarrioEntidad> consultarPorFiltro(BarrioEntidad filtro) {
                return barrios;
            }

            @Override
            public List<BarrioEntidad> consultarTodos() {
                return barrios;
            }
        };
    }

    @Test
    void consultarServiciosDevuelveSoloLosActivos() {
        ServicioNegocioImpl negocio = new ServicioNegocioImpl(new FabricaDeDAOParaPruebas().conServicioDAO(
                servicioDAOCon(List.of(
                        new ServicioEntidad(UtilUUID.generar(), "Plomero", true),
                        new ServicioEntidad(UtilUUID.generar(), "Retirado", false)))));

        List<ServicioDominio> servicios = negocio.consultarServicios();

        assertEquals(1, servicios.size());
        assertEquals("Plomero", servicios.get(0).getNombre());
    }

    @Test
    void consultarBarriosDevuelveSoloLosActivosConSuCiudad() {
        CiudadEntidad ciudad = new CiudadEntidad(UtilUUID.generar(), "Rionegro", true);
        BarrioNegocioImpl negocio = new BarrioNegocioImpl(new FabricaDeDAOParaPruebas().conBarrioDAO(
                barrioDAOCon(List.of(
                        new BarrioEntidad(UtilUUID.generar(), ciudad, "San Antonio", true),
                        new BarrioEntidad(UtilUUID.generar(), ciudad, "Cerrado", false)))));

        List<BarrioDominio> barrios = negocio.consultarBarrios();

        assertEquals(1, barrios.size());
        assertEquals("San Antonio", barrios.get(0).getNombre());
        assertEquals("Rionegro", barrios.get(0).getCiudad().getNombre());
    }
}
