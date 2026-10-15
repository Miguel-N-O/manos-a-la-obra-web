package com.manosalaobra.negocio.negocio.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.EstadoUsuarioDominio;
import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.entidad.TecnicoEntidad;
import com.manosalaobra.negocio.negocio.TecnicoNegocio;
import com.manosalaobra.negocio.negocio.ensamblador.impl.TecnicoEnsambladorEntidad;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class TecnicoNegocioImpl implements TecnicoNegocio {

    private static final Comparator<TecnicoDominio> POR_PROMEDIO_Y_NOMBRE = Comparator
            .comparing(TecnicoDominio::getPromedio, Comparator.reverseOrder())
            .thenComparing(tecnico -> tecnico.getUsuario().getNombre(), String.CASE_INSENSITIVE_ORDER);

    private final DAOFactory daoFactory;

    public TecnicoNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<TecnicoDominio> consultarPorFiltro(TecnicoDominio filtro) {
        TecnicoEntidad filtroEntidad = TecnicoEnsambladorEntidad.getInstancia().convertirAEntidad(filtro);
        filtroEntidad.getUsuario().getEstadoUsuario().setNombre(EstadoUsuarioDominio.ACTIVO);

        List<TecnicoDominio> tecnicos = new ArrayList<>();
        for (TecnicoEntidad entidad : daoFactory.obtenerTecnicoDAO().consultarPorFiltro(filtroEntidad)) {
            tecnicos.add(TecnicoEnsambladorEntidad.getInstancia().convertirADominio(entidad));
        }
        tecnicos.sort(POR_PROMEDIO_Y_NOMBRE);
        return tecnicos;
    }
}
