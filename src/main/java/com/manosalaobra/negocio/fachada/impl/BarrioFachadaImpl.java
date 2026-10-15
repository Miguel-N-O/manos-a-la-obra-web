package com.manosalaobra.negocio.fachada.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dto.BarrioDTO;
import com.manosalaobra.negocio.fachada.BarrioFachada;
import com.manosalaobra.negocio.fachada.ensamblador.impl.BarrioEnsambladorDTO;
import com.manosalaobra.negocio.negocio.BarrioNegocio;
import com.manosalaobra.negocio.negocio.impl.BarrioNegocioImpl;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.excepciones.ManosALaObraFachadaExcepcion;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BarrioFachadaImpl implements BarrioFachada {

    private static final Logger REGISTRO = LoggerFactory.getLogger(BarrioFachadaImpl.class);

    private final DAOFactory daoFactory;
    private final BarrioNegocio barrioNegocio;

    public BarrioFachadaImpl() {
        this.daoFactory = DAOFactory.obtenerFactoria();
        this.barrioNegocio = new BarrioNegocioImpl(daoFactory);
    }

    @Override
    public List<BarrioDTO> consultarBarrios() {
        try {
            return BarrioEnsambladorDTO.getInstancia().convertirADTO(barrioNegocio.consultarBarrios());
        } catch (ManosALaObraExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            REGISTRO.error("Problema inesperado consultando los barrios", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.BarrioFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_BARRIOS,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
