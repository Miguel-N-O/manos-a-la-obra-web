package com.manosalaobra.negocio.negocio.reglas.impl.tecnico;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.dominio.EstadoUsuarioDominio;
import com.manosalaobra.entidad.TecnicoEntidad;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import java.util.UUID;

// CU09 precondición: el técnico debe tener la cuenta ACTIVO y el perfil registrado (CU04).
// La consulta por filtro del DAO ya deja por fuera a los técnicos sin perfil.
public final class AsegurarTecnicoDisponibleRegla implements Regla<Object> {

    private static final Regla<Object> INSTANCIA = new AsegurarTecnicoDisponibleRegla();

    private AsegurarTecnicoDisponibleRegla() {
    }

    public static Regla<Object> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        UUID idTecnico = (UUID) datos[0];
        DAOFactory daoFactory = (DAOFactory) datos[1];

        TecnicoEntidad filtro = new TecnicoEntidad();
        filtro.setId(idTecnico);
        filtro.getUsuario().getEstadoUsuario().setNombre(EstadoUsuarioDominio.ACTIVO);
        if (daoFactory.obtenerTecnicoDAO().consultarPorFiltro(filtro).isEmpty()) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.SolicitudNegocioImpl.USUARIO_ERROR_TECNICO_NO_DISPONIBLE);
        }
    }
}
