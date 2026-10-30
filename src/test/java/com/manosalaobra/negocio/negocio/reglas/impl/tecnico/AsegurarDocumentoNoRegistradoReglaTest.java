package com.manosalaobra.negocio.negocio.reglas.impl.tecnico;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.manosalaobra.dao.datos.entidad.TecnicoDAO;
import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.entidad.TecnicoEntidad;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.pruebas.FabricaDeDAOParaPruebas;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AsegurarDocumentoNoRegistradoReglaTest {

    private final Regla<Object> regla = AsegurarDocumentoNoRegistradoRegla.obtenerInstancia();

    private static DAOFactory fabricaDondeSoloExisteElDocumento(String documentoRegistrado) {
        TecnicoDAO tecnicoDAO = new TecnicoDAO() {
            @Override
            public void crear(TecnicoEntidad entidad) {
            }

            @Override
            public boolean existeDocumento(String documento) {
                return documentoRegistrado.equals(documento);
            }

            @Override
            public TecnicoEntidad consultarPorId(UUID id) {
                return new TecnicoEntidad();
            }

            @Override
            public List<TecnicoEntidad> consultarPorFiltro(TecnicoEntidad filtro) {
                return List.of();
            }

            @Override
            public List<TecnicoEntidad> consultarTodos() {
                return List.of();
            }
        };
        return new FabricaDeDAOParaPruebas().conTecnicoDAO(tecnicoDAO);
    }

    @Test
    void conDocumentoYaRegistradoLanzaExcepcion() {
        DAOFactory fabrica = fabricaDondeSoloExisteElDocumento("1036123456");
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> regla.ejecutar("1036123456", fabrica));
        assertEquals("Ya existe una cuenta registrada con este correo o documento.", excepcion.getMensajeUsuario());
    }

    @Test
    void conDocumentoNuevoNoLanzaExcepcion() {
        DAOFactory fabrica = fabricaDondeSoloExisteElDocumento("1036123456");
        assertDoesNotThrow(() -> regla.ejecutar("98765432", fabrica));
    }
}
