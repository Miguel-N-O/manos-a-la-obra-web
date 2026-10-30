package com.manosalaobra.negocio.negocio.reglas.impl.solicitud;

import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import java.util.Set;

// HU-12 / CU12: el motivo del rechazo es opcional, pero si llega debe ser uno de la lista cerrada
public final class ValidarMotivoRechazoRegla implements Regla<String> {

    private static final Regla<String> INSTANCIA = new ValidarMotivoRechazoRegla();

    private static final Set<String> MOTIVOS_VALIDOS =
            Set.of("SIN_DISPONIBILIDAD", "FUERA_DE_ESPECIALIDAD", "FUERA_DE_ZONA", "OTRO");

    private ValidarMotivoRechazoRegla() {
    }

    public static Regla<String> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    @SafeVarargs
    public final void ejecutar(String... datos) {
        UtilTexto utilTexto = UtilTexto.getUtilTexto();
        String motivo = utilTexto.quitarEspacioEnBlanco(datos.length > 0 ? datos[0] : null);
        if (!utilTexto.esVacia(motivo) && !MOTIVOS_VALIDOS.contains(motivo)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_MOTIVO_RECHAZO_NO_VALIDO);
        }
    }
}
