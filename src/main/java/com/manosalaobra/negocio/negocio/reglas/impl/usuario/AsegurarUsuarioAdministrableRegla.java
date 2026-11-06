package com.manosalaobra.negocio.negocio.reglas.impl.usuario;

import com.manosalaobra.dominio.RolDominio;
import com.manosalaobra.entidad.UsuarioEntidad;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.util.UUID;

/**
 * HU-16. Recibe: [0] UsuarioEntidad al que se le cambiará el estado (vacía si no existe) y [1] id del
 * administrador que hace el cambio. No se puede tocar la propia cuenta ni la de otro administrador.
 */
public final class AsegurarUsuarioAdministrableRegla implements Regla<Object> {

    private static final Regla<Object> INSTANCIA = new AsegurarUsuarioAdministrableRegla();

    private AsegurarUsuarioAdministrableRegla() {
    }

    public static Regla<Object> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        UsuarioEntidad usuario = (UsuarioEntidad) datos[0];
        UUID administradorId = (UUID) datos[1];

        if (UtilUUID.esValorDefecto(usuario.getId())) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_USUARIO_NO_ENCONTRADO);
        }
        if (usuario.getId().equals(administradorId)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_NO_PUEDES_CAMBIAR_TU_CUENTA);
        }
        if (RolDominio.ADMIN.equals(usuario.getRol().getNombre())) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CUENTA_ADMIN_NO_MODIFICABLE);
        }
    }
}
