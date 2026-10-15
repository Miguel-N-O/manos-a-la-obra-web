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
}
