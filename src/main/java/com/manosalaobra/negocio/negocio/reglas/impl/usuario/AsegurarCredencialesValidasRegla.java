package com.manosalaobra.negocio.negocio.reglas.impl.usuario;

import com.manosalaobra.dominio.EstadoUsuarioDominio;
import com.manosalaobra.entidad.UsuarioEntidad;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.Claves;

/**
 * Recibe: [0] UsuarioEntidad encontrado por correo (o una entidad vacía si no existe) y [1] la clave en texto plano.
 * Siempre compara contra un hash, exista o no el usuario, para que el tiempo de respuesta no delate si el correo existe.
 */
public final class AsegurarCredencialesValidasRegla implements Regla<Object> {

    private static final Regla<Object> INSTANCIA = new AsegurarCredencialesValidasRegla();
    private static final String HASH_FICTICIO = Claves.cifrar("clave-ficticia-sin-usuario");

    private AsegurarCredencialesValidasRegla() {
    }

    public static Regla<Object> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        UsuarioEntidad usuario = (UsuarioEntidad) datos[0];
        String clave = (String) datos[1];

        boolean existe = !usuario.getClaveHash().isEmpty();
        boolean claveCorrecta = Claves.coincide(clave, existe ? usuario.getClaveHash() : HASH_FICTICIO);
        boolean activo = EstadoUsuarioDominio.ACTIVO.equals(usuario.getEstadoUsuario().getNombre());

        if (!existe || !claveCorrecta || !activo) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CREDENCIALES_INCORRECTAS);
        }
    }
}
