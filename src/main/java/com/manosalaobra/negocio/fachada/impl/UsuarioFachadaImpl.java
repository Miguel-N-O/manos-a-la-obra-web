package com.manosalaobra.negocio.fachada.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.dto.TecnicoDTO;
import com.manosalaobra.dto.UsuarioDTO;
import com.manosalaobra.negocio.fachada.UsuarioFachada;
import com.manosalaobra.negocio.fachada.ensamblador.impl.TecnicoEnsambladorDTO;
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
            transaccionIniciada = false;
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

    @Override
    public UsuarioDTO registrarTecnico(TecnicoDTO tecnico) {
        boolean transaccionIniciada = false;
        try {
            daoFactory.iniciarTransaccion();
            transaccionIniciada = true;

            TecnicoDominio tecnicoDominio = TecnicoEnsambladorDTO.getInstancia().convertirADominio(tecnico);
            TecnicoDominio tecnicoRegistrado = usuarioNegocio.registrarTecnico(tecnicoDominio);

            daoFactory.confirmarTransaccion();
            transaccionIniciada = false;
            return UsuarioEnsambladorDTO.getInstancia().convertirADTO(tecnicoRegistrado.getUsuario());
        } catch (ManosALaObraExcepcion excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            throw excepcion;
        } catch (Exception excepcion) {
            cancelarSiHaceFalta(transaccionIniciada);
            REGISTRO.error("Problema inesperado registrando un técnico", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.UsuarioFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_REGISTRANDO_TECNICO,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }

    @Override
    public UsuarioDTO iniciarSesion(UsuarioDTO credenciales) {
        try {
            UsuarioDominio credencialesDominio = UsuarioEnsambladorDTO.getInstancia().convertirADominio(credenciales);
            UsuarioDominio usuarioAutenticado = usuarioNegocio.iniciarSesion(credencialesDominio);
            return UsuarioEnsambladorDTO.getInstancia().convertirADTO(usuarioAutenticado);
        } catch (ManosALaObraExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            REGISTRO.error("Problema inesperado iniciando sesión", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.UsuarioFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_INICIANDO_SESION,
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
