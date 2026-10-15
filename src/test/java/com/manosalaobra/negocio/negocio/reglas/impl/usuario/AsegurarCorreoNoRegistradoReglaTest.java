package com.manosalaobra.negocio.negocio.reglas.impl.usuario;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.manosalaobra.dao.datos.entidad.EstadoUsuarioDAO;
import com.manosalaobra.dao.datos.entidad.IndicativoPaisDAO;
import com.manosalaobra.dao.datos.entidad.RolDAO;
import com.manosalaobra.dao.datos.entidad.UsuarioDAO;
import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.entidad.UsuarioEntidad;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class AsegurarCorreoNoRegistradoReglaTest {

    private final Regla<Object> regla = AsegurarCorreoNoRegistradoRegla.obtenerInstancia();

    private static DAOFactory fabricaDondeSoloExisteElCorreo(String correoRegistrado) {
        UsuarioDAO usuarioDAO = new UsuarioDAO() {
            @Override
            public void crear(UsuarioEntidad entidad) {
            }

            @Override
            public UsuarioEntidad consultarPorId(UUID id) {
                return new UsuarioEntidad();
            }

            @Override
            public List<UsuarioEntidad> consultarPorFiltro(UsuarioEntidad filtro) {
                return filtro.getCorreo().equals(correoRegistrado) ? List.of(filtro) : List.of();
            }

            @Override
            public List<UsuarioEntidad> consultarTodos() {
                return List.of();
            }
        };
        return new DAOFactory() {
            @Override
            protected void abrirConexion() {
            }

            @Override
            public UsuarioDAO obtenerUsuarioDAO() {
                return usuarioDAO;
            }

            @Override
            public RolDAO obtenerRolDAO() {
                return null;
            }

            @Override
            public EstadoUsuarioDAO obtenerEstadoUsuarioDAO() {
                return null;
            }

            @Override
            public IndicativoPaisDAO obtenerIndicativoPaisDAO() {
                return null;
            }
        };
    }

    @Test
    void conCorreoYaRegistradoLanzaExcepcion() {
        DAOFactory fabrica = fabricaDondeSoloExisteElCorreo("ana@example.com");
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> regla.ejecutar("ana@example.com", fabrica));
        assertEquals("Ese correo ya está registrado.", excepcion.getMensajeUsuario());
    }

    @Test
    void conCorreoNuevoNoLanzaExcepcion() {
        DAOFactory fabrica = fabricaDondeSoloExisteElCorreo("ana@example.com");
        assertDoesNotThrow(() -> regla.ejecutar("otra@example.com", fabrica));
    }
}
