package com.manosalaobra.negocio.fachada.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dto.ServicioDTO;
import com.manosalaobra.negocio.fachada.ServicioFachada;
import com.manosalaobra.negocio.fachada.ensamblador.impl.ServicioEnsambladorDTO;
import com.manosalaobra.negocio.negocio.ServicioNegocio;
import com.manosalaobra.negocio.negocio.impl.ServicioNegocioImpl;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.excepciones.ManosALaObraFachadaExcepcion;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ServicioFachadaImpl implements ServicioFachada {

    private static final Logger REGISTRO = LoggerFactory.getLogger(ServicioFachadaImpl.class);

    private final DAOFactory daoFactory;
    private final ServicioNegocio servicioNegocio;

    public ServicioFachadaImpl() {
        this.daoFactory = DAOFactory.obtenerFactoria();
        this.servicioNegocio = new ServicioNegocioImpl(daoFactory);
    }

    @Override
    public List<ServicioDTO> consultarServicios() {
        try {
            return ServicioEnsambladorDTO.getInstancia().convertirADTO(servicioNegocio.consultarServicios());
        } catch (ManosALaObraExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            REGISTRO.error("Problema inesperado consultando los servicios", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.ServicioFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_SERVICIOS,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
