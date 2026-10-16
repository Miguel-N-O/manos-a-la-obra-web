package com.manosalaobra.negocio.negocio.impl;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.EstadoUsuarioDominio;
import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.entidad.TecnicoEntidad;
import com.manosalaobra.negocio.negocio.TecnicoNegocio;
import com.manosalaobra.negocio.negocio.ensamblador.impl.TecnicoEnsambladorEntidad;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

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

    @Override
    public TecnicoDominio consultarPorId(UUID id) {
        if (UtilUUID.esValorDefecto(id)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_TECNICO_NO_ENCONTRADO);
        }
        TecnicoEntidad filtroEntidad = new TecnicoEntidad();
        filtroEntidad.setId(id);
        filtroEntidad.getUsuario().getEstadoUsuario().setNombre(EstadoUsuarioDominio.ACTIVO);

        List<TecnicoEntidad> encontrados = daoFactory.obtenerTecnicoDAO().consultarPorFiltro(filtroEntidad);
        if (encontrados.isEmpty()) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_TECNICO_NO_ENCONTRADO);
        }
        return TecnicoEnsambladorEntidad.getInstancia().convertirADominio(encontrados.get(0));
    }
}
