package com.manosalaobra.negocio.negocio.reglas.impl.usuario;

import com.manosalaobra.dominio.EstadoUsuarioDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import java.util.Set;

// HU-16: el administrador solo puede dejar una cuenta ACTIVA, SUSPENDIDA o BLOQUEADA; para suspender o bloquear
// debe indicar el motivo (datos[0] = estado, datos[1] = motivo)
public final class ValidarEstadoUsuarioRegla implements Regla<String> {

    private static final Regla<String> INSTANCIA = new ValidarEstadoUsuarioRegla();

    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            EstadoUsuarioDominio.ACTIVO, EstadoUsuarioDominio.SUSPENDIDO, EstadoUsuarioDominio.BLOQUEADO);

    private static final int MOTIVO_LONGITUD_MAXIMA = 200;

    private ValidarEstadoUsuarioRegla() {
    }

    public static Regla<String> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    @SafeVarargs
    public final void ejecutar(String... datos) {
        String estado = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(datos.length > 0 ? datos[0] : null);
        if (!ESTADOS_VALIDOS.contains(estado)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_ESTADO_NO_VALIDO);
        }
        if (EstadoUsuarioDominio.ACTIVO.equals(estado)) {
            return;
        }
        String motivo = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(datos.length > 1 ? datos[1] : null);
        if (motivo.isEmpty()) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_MOTIVO_OBLIGATORIO);
        }
        if (motivo.length() > MOTIVO_LONGITUD_MAXIMA) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_MOTIVO_MUY_LARGO);
        }
    }
}
