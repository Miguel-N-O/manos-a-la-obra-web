package com.manosalaobra.negocio.fachada.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dto.IndicativoPaisDTO;
import com.manosalaobra.negocio.fachada.IndicativoPaisFachada;
import com.manosalaobra.negocio.fachada.ensamblador.impl.IndicativoPaisEnsambladorDTO;
import com.manosalaobra.negocio.negocio.IndicativoPaisNegocio;
import com.manosalaobra.negocio.negocio.impl.IndicativoPaisNegocioImpl;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraExcepcion;
import com.manosalaobra.transversal.excepciones.ManosALaObraFachadaExcepcion;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IndicativoPaisFachadaImpl implements IndicativoPaisFachada {

    private static final Logger REGISTRO = LoggerFactory.getLogger(IndicativoPaisFachadaImpl.class);

    private final DAOFactory daoFactory;
    private final IndicativoPaisNegocio indicativoPaisNegocio;

    public IndicativoPaisFachadaImpl() {
        this.daoFactory = DAOFactory.obtenerFactoria();
        this.indicativoPaisNegocio = new IndicativoPaisNegocioImpl(daoFactory);
    }

    @Override
    public List<IndicativoPaisDTO> consultarIndicativosPais() {
        try {
            return IndicativoPaisEnsambladorDTO.getInstancia()
                    .convertirADTO(indicativoPaisNegocio.consultarIndicativosPais());
        } catch (ManosALaObraExcepcion excepcion) {
            throw excepcion;
        } catch (Exception excepcion) {
            REGISTRO.error("Problema inesperado consultando los indicativos de país", excepcion);
            throw ManosALaObraFachadaExcepcion.crear(
                    CatalogoMensajes.IndicativoPaisFachadaImpl.USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_INDICATIVOS_PAIS,
                    excepcion.getMessage(), excepcion);
        } finally {
            daoFactory.cerrarConexion();
        }
    }
}
