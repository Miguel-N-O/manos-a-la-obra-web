package com.manosalaobra.negocio.fachada.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.dto.UsuarioDTO;
import com.manosalaobra.negocio.fachada.UsuarioFachada;
import com.manosalaobra.negocio.fachada.ensamblador.impl.UsuarioEnsambladorDTO;
import com.manosalaobra.negocio.negocio.UsuarioNegocio;
import com.manosalaobra.negocio.negocio.impl.UsuarioNegocioImpl;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.excepciones.ManosALaObraFachadaExcepcion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UsuarioFachadaImpl implements UsuarioFachada {

    private static final Logger REGISTRO = LoggerFactory.getLogger(UsuarioFachadaImpl.class);

    private final DAOFactory daoFactory;
    private final UsuarioNegocio usuarioNegocio;

    public UsuarioFachadaImpl() {
        this.daoFactory = DAOFactory.obtenerFactoria();
        this.usuarioNegocio = new UsuarioNegocioImpl(daoFactory);
    }

    @Override
    public UsuarioDTO registrarCiudadano(UsuarioDTO usuario) {
        boolean transaccionIniciada = false;
        try {
            daoFactory.iniciarTransaccion();
            transaccionIniciada = true;

            UsuarioDominio usuarioDominio = UsuarioEnsambladorDTO.getInstancia().convertirADominio(usuario);
            UsuarioDominio ciudadanoRegistrado = usuarioNegocio.registrarCiudadano(usuarioDominio);

            daoFactory.confirmarTransaccion();
            return UsuarioEnsambladorDTO.getInstancia().convertirADTO(ciudadanoRegistrado);
        } catch (ManosALaObraExcepcion excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            throw excepcion;
        } catch (Exception excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            REGISTRO.error("Problema inesperado registrando un ciudadano", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.UsuarioFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_REGISTRANDO_CIUDADANO,
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
