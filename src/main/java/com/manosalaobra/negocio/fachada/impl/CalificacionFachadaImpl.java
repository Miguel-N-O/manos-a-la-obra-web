package com.manosalaobra.negocio.fachada.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.CalificacionDominio;
import com.manosalaobra.dto.CalificacionDTO;
import com.manosalaobra.negocio.fachada.CalificacionFachada;
import com.manosalaobra.negocio.fachada.ensamblador.impl.CalificacionEnsambladorDTO;
import com.manosalaobra.negocio.negocio.CalificacionNegocio;
import com.manosalaobra.negocio.negocio.impl.CalificacionNegocioImpl;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.excepciones.ManosALaObraFachadaExcepcion;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CalificacionFachadaImpl implements CalificacionFachada {

    private static final Logger REGISTRO = LoggerFactory.getLogger(CalificacionFachadaImpl.class);

    private final DAOFactory daoFactory;
    private final CalificacionNegocio calificacionNegocio;

    public CalificacionFachadaImpl() {
        this.daoFactory = DAOFactory.obtenerFactoria();
        this.calificacionNegocio = new CalificacionNegocioImpl(daoFactory);
    }

    @Override
    public CalificacionDTO calificar(UUID ciudadanoId, CalificacionDTO calificacion) {
        boolean transaccionIniciada = false;
        try {
            daoFactory.iniciarTransaccion();
            transaccionIniciada = true;

            CalificacionDominio calificacionDominio =
                    CalificacionEnsambladorDTO.getInstancia().convertirADominio(calificacion);
            CalificacionDominio registrada = calificacionNegocio.calificar(ciudadanoId, calificacionDominio);

            daoFactory.confirmarTransaccion();
            transaccionIniciada = false;
            return CalificacionEnsambladorDTO.getInstancia().convertirADTO(registrada);
        } catch (ManosALaObraExcepcion excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            throw excepcion;
        } catch (Exception excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            REGISTRO.error("Problema inesperado registrando una calificación", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.CalificacionFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CALIFICANDO,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public List<CalificacionDTO> consultarDelTecnico(UUID tecnicoId) {
        try {
            return CalificacionEnsambladorDTO.getInstancia().convertirADTO(
                    calificacionNegocio.consultarDelTecnico(tecnicoId));
        } catch (ManosALaObraExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            REGISTRO.error("Problema inesperado consultando las calificaciones del técnico", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.CalificacionFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_CALIFICACIONES,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public List<CalificacionDTO> consultarTodasParaModeracion() {
        try {
            return CalificacionEnsambladorDTO.getInstancia().convertirADTO(
                    calificacionNegocio.consultarTodasParaModeracion());
        } catch (ManosALaObraExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            REGISTRO.error("Problema inesperado consultando las calificaciones para moderar", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.CalificacionFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_CALIFICACIONES,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public CalificacionDTO ocultarComentario(UUID calificacionId, String motivo) {
        return cambiarVisibilidad(calificacionId, false, motivo);
    }

    @Override
    public CalificacionDTO mostrarComentario(UUID calificacionId) {
        return cambiarVisibilidad(calificacionId, true, "");
    }

    private CalificacionDTO cambiarVisibilidad(UUID calificacionId, boolean visible, String motivo) {
        boolean transaccionIniciada = false;
        try {
            daoFactory.iniciarTransaccion();
            transaccionIniciada = true;

            CalificacionDominio cambiada = visible
                    ? calificacionNegocio.mostrarComentario(calificacionId)
                    : calificacionNegocio.ocultarComentario(calificacionId, motivo);

            daoFactory.confirmarTransaccion();
            transaccionIniciada = false;
            return CalificacionEnsambladorDTO.getInstancia().convertirADTO(cambiada);
        } catch (ManosALaObraExcepcion excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            throw excepcion;
        } catch (Exception excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            REGISTRO.error("Problema inesperado moderando un comentario", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.CalificacionFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_MODERANDO,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    private void cancelarSiHaceFalta(boolean transaccionIniciada) {
        if (transaccionIniciada) {
            daoFactory.cancelarTransaccion();
        }
    }
}
