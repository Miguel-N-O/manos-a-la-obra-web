package com.manosalaobra.negocio.negocio.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.manosalaobra.dao.datos.entidad.EstadoUsuarioDAO;
import com.manosalaobra.dao.datos.entidad.UsuarioDAO;
import com.manosalaobra.dominio.EstadoUsuarioDominio;
import com.manosalaobra.dominio.RolDominio;
import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.entidad.EstadoUsuarioEntidad;
import com.manosalaobra.entidad.RolEntidad;
import com.manosalaobra.entidad.UsuarioEntidad;
import com.manosalaobra.pruebas.FabricaDeDAOParaPruebas;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

// HU-16: administración de cuentas
class UsuarioNegocioAdministracionTest {

    private static final UUID ID_ACTIVO = UtilUUID.generar();
    private static final UUID ID_SUSPENDIDO = UtilUUID.generar();
    private static final UUID ID_BLOQUEADO = UtilUUID.generar();

    private static final class UsuarioDAOEnMemoria implements UsuarioDAO {
        private final List<UsuarioEntidad> usuarios = new ArrayList<>();

        @Override
        public void crear(UsuarioEntidad entidad) {
            usuarios.add(entidad);
        }

        @Override
        public UsuarioEntidad consultarPorId(UUID id) {
            for (UsuarioEntidad usuario : usuarios) {
                if (id.equals(usuario.getId())) {
                    return usuario;
                }
            }
            return new UsuarioEntidad();
        }

        @Override
        public List<UsuarioEntidad> consultarPorFiltro(UsuarioEntidad filtro) {
            return usuarios;
        }

        @Override
        public List<UsuarioEntidad> consultarTodos() {
            return usuarios;
        }

        // Igual que el UPDATE real: solo cambia el estado
        @Override
        public void actualizar(UUID id, UsuarioEntidad entidad) {
            UsuarioEntidad guardado = consultarPorId(id);
            guardado.setEstadoUsuario(entidad.getEstadoUsuario());
            guardado.setMotivoEstado(entidad.getMotivoEstado());
        }
    }

    private static EstadoUsuarioDAO estadoUsuarioDAO() {
        return new EstadoUsuarioDAO() {
            @Override
            public EstadoUsuarioEntidad consultarPorId(UUID id) {
                return new EstadoUsuarioEntidad();
            }

            @Override
            public List<EstadoUsuarioEntidad> consultarPorFiltro(EstadoUsuarioEntidad filtro) {
                if (EstadoUsuarioDominio.ACTIVO.equals(filtro.getNombre())) {
                    return List.of(new EstadoUsuarioEntidad(ID_ACTIVO, EstadoUsuarioDominio.ACTIVO, true));
                }
                if (EstadoUsuarioDominio.SUSPENDIDO.equals(filtro.getNombre())) {
                    return List.of(new EstadoUsuarioEntidad(ID_SUSPENDIDO, EstadoUsuarioDominio.SUSPENDIDO, true));
                }
                if (EstadoUsuarioDominio.BLOQUEADO.equals(filtro.getNombre())) {
                    return List.of(new EstadoUsuarioEntidad(ID_BLOQUEADO, EstadoUsuarioDominio.BLOQUEADO, true));
                }
                return List.of();
            }

            @Override
            public List<EstadoUsuarioEntidad> consultarTodos() {
                return List.of();
            }
        };
    }

    private final UsuarioDAOEnMemoria usuarioDAO = new UsuarioDAOEnMemoria();
    private final UsuarioNegocioImpl negocio = new UsuarioNegocioImpl(new FabricaDeDAOParaPruebas()
            .conUsuarioDAO(usuarioDAO).conEstadoUsuarioDAO(estadoUsuarioDAO()));
    private final UUID administrador = UtilUUID.generar();

    private UsuarioEntidad usuario(UUID id, String rol) {
        UsuarioEntidad usuario = new UsuarioEntidad();
        usuario.setId(id);
        usuario.setRol(new RolEntidad(UtilUUID.generar(), rol, true));
        usuario.setEstadoUsuario(new EstadoUsuarioEntidad(ID_ACTIVO, EstadoUsuarioDominio.ACTIVO, true));
        usuarioDAO.usuarios.add(usuario);
        return usuario;
    }

    @Test
    void suspendeBloqueaYReactivaUnaCuenta() {
        UsuarioEntidad ciudadano = usuario(UtilUUID.generar(), RolDominio.CIUDADANO);

        UsuarioDominio suspendido = negocio.cambiarEstado(administrador, ciudadano.getId(), "SUSPENDIDO", "Insultos a técnicos");
        assertEquals(EstadoUsuarioDominio.SUSPENDIDO, suspendido.getEstadoUsuario().getNombre());
        assertEquals(ID_SUSPENDIDO, ciudadano.getEstadoUsuario().getId());

        assertEquals(EstadoUsuarioDominio.BLOQUEADO,
                negocio.cambiarEstado(administrador, ciudadano.getId(), "BLOQUEADO", "Reincidencia").getEstadoUsuario().getNombre());
        assertEquals(EstadoUsuarioDominio.ACTIVO,
                negocio.cambiarEstado(administrador, ciudadano.getId(), "ACTIVO", "").getEstadoUsuario().getNombre());
    }

    @Test
    void tambienAdministraLasCuentasDeTecnicos() {
        UsuarioEntidad tecnico = usuario(UtilUUID.generar(), RolDominio.TECNICO);
        negocio.cambiarEstado(administrador, tecnico.getId(), "SUSPENDIDO", "Datos falsos");
        assertEquals(ID_SUSPENDIDO, tecnico.getEstadoUsuario().getId());
    }

    @Test
    void noPermiteUnEstadoQueNoEsteEnLaLista() {
        UsuarioEntidad ciudadano = usuario(UtilUUID.generar(), RolDominio.CIUDADANO);
        for (String estado : new String[] {"ELIMINADO", "", "activo"}) {
            ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                    () -> negocio.cambiarEstado(administrador, ciudadano.getId(), estado, "Motivo"));
            assertEquals("El estado no es válido. Elige ACTIVO, SUSPENDIDO o BLOQUEADO.",
                    excepcion.getMensajeUsuario());
        }
        assertEquals(ID_ACTIVO, ciudadano.getEstadoUsuario().getId());
    }

    @Test
    void noPermiteCambiarLaPropiaCuenta() {
        UsuarioEntidad propia = usuario(administrador, RolDominio.CIUDADANO);
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.cambiarEstado(administrador, propia.getId(), "BLOQUEADO", "Motivo"));
        assertEquals("No puedes cambiar el estado de tu propia cuenta.", excepcion.getMensajeUsuario());
        assertEquals(ID_ACTIVO, propia.getEstadoUsuario().getId());
    }

    @Test
    void noPermiteSuspenderOtraCuentaDeAdministrador() {
        UsuarioEntidad otroAdmin = usuario(UtilUUID.generar(), RolDominio.ADMIN);
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.cambiarEstado(administrador, otroAdmin.getId(), "SUSPENDIDO", "Motivo"));
        assertEquals("Las cuentas de administrador no se pueden suspender ni bloquear.",
                excepcion.getMensajeUsuario());
    }

    @Test
    void noEncuentraUnaCuentaQueNoExisteNiSinId() {
        usuario(UtilUUID.generar(), RolDominio.CIUDADANO);
        ManosALaObraExcepcion excepcion = assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.cambiarEstado(administrador, UtilUUID.generar(), "SUSPENDIDO", "Motivo"));
        assertEquals("No encontramos esa cuenta.", excepcion.getMensajeUsuario());
        assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.cambiarEstado(administrador, UtilUUID.obtenerUUIDDefecto(), "SUSPENDIDO", "Motivo"));
    }

    @Test
    void suspenderOBloquearExigeUnMotivoYReactivarLoBorra() {
        UsuarioEntidad ciudadano = usuario(UtilUUID.generar(), RolDominio.CIUDADANO);
        for (String motivo : new String[] {"", "   ", "a".repeat(201)}) {
            assertThrows(ManosALaObraExcepcion.class,
                    () -> negocio.cambiarEstado(administrador, ciudadano.getId(), "SUSPENDIDO", motivo));
        }
        ManosALaObraExcepcion sinMotivo = assertThrows(ManosALaObraExcepcion.class,
                () -> negocio.cambiarEstado(administrador, ciudadano.getId(), "BLOQUEADO", null));
        assertEquals("Indica el motivo de la suspensión o el bloqueo.", sinMotivo.getMensajeUsuario());
        assertEquals(ID_ACTIVO, ciudadano.getEstadoUsuario().getId());

        UsuarioDominio suspendido = negocio.cambiarEstado(administrador, ciudadano.getId(), "SUSPENDIDO", "Insultos");
        assertEquals("Insultos", suspendido.getMotivoEstado());
        assertEquals("", negocio.cambiarEstado(administrador, ciudadano.getId(), "ACTIVO", "ignorado").getMotivoEstado());
    }

    @Test
    void buscaPorNombreOCorreoYFiltraPorRolYEstado() {
        UsuarioEntidad ana = usuario(UtilUUID.generar(), RolDominio.CIUDADANO);
        ana.setNombre("Ana Gómez");
        ana.setCorreo("ana@example.com");
        UsuarioEntidad beto = usuario(UtilUUID.generar(), RolDominio.TECNICO);
        beto.setNombre("Beto Ruiz");
        beto.setCorreo("beto@example.com");
        negocio.cambiarEstado(administrador, beto.getId(), "SUSPENDIDO", "Datos falsos");

        assertEquals(2, negocio.consultarTodos(null, null, null).size());
        assertEquals(1, negocio.consultarTodos("ANA", null, null).size());
        assertEquals(1, negocio.consultarTodos("beto@", "", "").size());
        assertEquals(1, negocio.consultarTodos(null, "TECNICO", null).size());
        assertEquals(1, negocio.consultarTodos(null, null, "SUSPENDIDO").size());
        assertEquals(0, negocio.consultarTodos("ana", "TECNICO", null).size());
        assertEquals(0, negocio.consultarTodos("nadie", null, null).size());
    }

    @Test
    void listaTodasLasCuentas() {
        usuario(UtilUUID.generar(), RolDominio.CIUDADANO);
        usuario(UtilUUID.generar(), RolDominio.TECNICO);
        assertEquals(2, negocio.consultarTodos().size());
    }
}
