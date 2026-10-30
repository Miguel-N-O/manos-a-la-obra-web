package com.manosalaobra.negocio.fachada.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.SolicitudDominio;
import com.manosalaobra.dto.SolicitudDTO;
import com.manosalaobra.negocio.fachada.SolicitudFachada;
import com.manosalaobra.negocio.fachada.ensamblador.impl.SolicitudEnsambladorDTO;
import com.manosalaobra.negocio.negocio.SolicitudNegocio;
import com.manosalaobra.negocio.negocio.impl.SolicitudNegocioImpl;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.excepciones.ManosALaObraFachadaExcepcion;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SolicitudFachadaImpl implements SolicitudFachada {

    private static final Logger REGISTRO = LoggerFactory.getLogger(SolicitudFachadaImpl.class);

    private final DAOFactory daoFactory;
    private final SolicitudNegocio solicitudNegocio;

    public SolicitudFachadaImpl() {
        this.daoFactory = DAOFactory.obtenerFactoria();
        this.solicitudNegocio = new SolicitudNegocioImpl(daoFactory);
    }

    @Override
    public SolicitudDTO crear(UUID ciudadanoId, SolicitudDTO solicitud) {
        boolean transaccionIniciada = false;
        try {
            daoFactory.iniciarTransaccion();
            transaccionIniciada = true;

            SolicitudDominio solicitudDominio = SolicitudEnsambladorDTO.getInstancia().convertirADominio(solicitud);
            SolicitudDominio solicitudCreada = solicitudNegocio.crear(ciudadanoId, solicitudDominio);

            daoFactory.confirmarTransaccion();
            transaccionIniciada = false;
            return SolicitudEnsambladorDTO.getInstancia().convertirADTO(solicitudCreada);
        } catch (ManosALaObraExcepcion excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            throw excepcion;
        } catch (Exception excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            REGISTRO.error("Problema inesperado creando una solicitud", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.SolicitudFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CREANDO_SOLICITUD,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public List<SolicitudDTO> consultarDelCiudadano(UUID ciudadanoId) {
        try {
            return SolicitudEnsambladorDTO.getInstancia().convertirADTO(
                    solicitudNegocio.consultarDelCiudadano(ciudadanoId));
        } catch (ManosALaObraExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            REGISTRO.error("Problema inesperado consultando las solicitudes del ciudadano", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.SolicitudFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_SOLICITUDES,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public SolicitudDTO consultarUnaDelCiudadano(UUID ciudadanoId, UUID solicitudId) {
        try {
            return SolicitudEnsambladorDTO.getInstancia().convertirADTO(
                    solicitudNegocio.consultarUnaDelCiudadano(ciudadanoId, solicitudId));
        } catch (ManosALaObraExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            REGISTRO.error("Problema inesperado consultando una solicitud del ciudadano", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.SolicitudFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_SOLICITUDES,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public List<SolicitudDTO> consultarRecibidas(UUID usuarioTecnicoId) {
        try {
            return SolicitudEnsambladorDTO.getInstancia().convertirADTO(
                    solicitudNegocio.consultarRecibidas(usuarioTecnicoId));
        } catch (ManosALaObraExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            REGISTRO.error("Problema inesperado consultando las solicitudes recibidas", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.SolicitudFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_SOLICITUDES,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public SolicitudDTO consultarUnaRecibida(UUID usuarioTecnicoId, UUID solicitudId) {
        try {
            return SolicitudEnsambladorDTO.getInstancia().convertirADTO(
                    solicitudNegocio.consultarUnaRecibida(usuarioTecnicoId, solicitudId));
        } catch (ManosALaObraExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            REGISTRO.error("Problema inesperado consultando una solicitud recibida", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.SolicitudFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_SOLICITUDES,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public void marcarRecibidasComoNotificadas(UUID usuarioTecnicoId) {
        boolean transaccionIniciada = false;
        try {
            daoFactory.iniciarTransaccion();
            transaccionIniciada = true;

            solicitudNegocio.marcarRecibidasComoNotificadas(usuarioTecnicoId);

            daoFactory.confirmarTransaccion();
            transaccionIniciada = false;
        } catch (ManosALaObraExcepcion excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            throw excepcion;
        } catch (Exception excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            REGISTRO.error("Problema inesperado marcando las solicitudes como notificadas", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.SolicitudFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_MARCANDO_NOTIFICADAS,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public SolicitudDTO aceptar(UUID usuarioTecnicoId, UUID solicitudId) {
        boolean transaccionIniciada = false;
        try {
            daoFactory.iniciarTransaccion();
            transaccionIniciada = true;

            SolicitudDominio aceptada = solicitudNegocio.aceptar(usuarioTecnicoId, solicitudId);

            daoFactory.confirmarTransaccion();
            transaccionIniciada = false;
            return SolicitudEnsambladorDTO.getInstancia().convertirADTO(aceptada);
        } catch (ManosALaObraExcepcion excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            throw excepcion;
        } catch (Exception excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            REGISTRO.error("Problema inesperado aceptando una solicitud", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.SolicitudFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_ACEPTANDO_SOLICITUD,
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
