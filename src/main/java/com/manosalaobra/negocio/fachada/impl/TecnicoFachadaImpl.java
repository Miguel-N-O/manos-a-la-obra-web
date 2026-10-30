package com.manosalaobra.negocio.fachada.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.dto.TecnicoDTO;
import com.manosalaobra.negocio.fachada.TecnicoFachada;
import com.manosalaobra.negocio.fachada.ensamblador.impl.TecnicoEnsambladorDTO;
import com.manosalaobra.negocio.negocio.TecnicoNegocio;
import com.manosalaobra.negocio.negocio.impl.TecnicoNegocioImpl;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.excepciones.ManosALaObraFachadaExcepcion;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TecnicoFachadaImpl implements TecnicoFachada {

    private static final Logger REGISTRO = LoggerFactory.getLogger(TecnicoFachadaImpl.class);

    private final DAOFactory daoFactory;
    private final TecnicoNegocio tecnicoNegocio;

    public TecnicoFachadaImpl() {
        this.daoFactory = DAOFactory.obtenerFactoria();
        this.tecnicoNegocio = new TecnicoNegocioImpl(daoFactory);
    }

    @Override
    public List<TecnicoDTO> consultarPorFiltro(TecnicoDTO filtro) {
        try {
            TecnicoDominio filtroDominio = TecnicoEnsambladorDTO.getInstancia().convertirADominio(filtro);
            return TecnicoEnsambladorDTO.getInstancia().convertirADTO(tecnicoNegocio.consultarPorFiltro(filtroDominio));
        } catch (ManosALaObraExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            REGISTRO.error("Problema inesperado consultando los técnicos", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.TecnicoFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_TECNICOS,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public TecnicoDTO consultarPorId(UUID id) {
        try {
            return TecnicoEnsambladorDTO.getInstancia().convertirADTO(tecnicoNegocio.consultarPorId(id));
        } catch (ManosALaObraExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            REGISTRO.error("Problema inesperado consultando el perfil del técnico", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.TecnicoFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_PERFIL,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public TecnicoDTO consultarPerfilPropio(UUID usuarioId) {
        try {
            return TecnicoEnsambladorDTO.getInstancia().convertirADTO(tecnicoNegocio.consultarPorUsuario(usuarioId));
        } catch (ManosALaObraExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            REGISTRO.error("Problema inesperado consultando el perfil propio del técnico", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.TecnicoFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_PERFIL_PROPIO,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public TecnicoDTO guardarPerfilPropio(UUID usuarioId, TecnicoDTO perfil) {
        boolean transaccionIniciada = false;
        try {
            daoFactory.iniciarTransaccion();
            transaccionIniciada = true;

            TecnicoDominio perfilDominio = TecnicoEnsambladorDTO.getInstancia().convertirADominio(perfil);
            TecnicoDominio perfilGuardado = tecnicoNegocio.guardarPerfil(usuarioId, perfilDominio);

            daoFactory.confirmarTransaccion();
            transaccionIniciada = false;
            return TecnicoEnsambladorDTO.getInstancia().convertirADTO(perfilGuardado);
        } catch (ManosALaObraExcepcion excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            throw excepcion;
        } catch (Exception excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            REGISTRO.error("Problema inesperado guardando el perfil del técnico", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.TecnicoFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_GUARDANDO_PERFIL,
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
