package com.manosalaobra.negocio.negocio.reglas.impl.usuario;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.entidad.UsuarioEntidad;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;

public final class AsegurarCorreoNoRegistradoRegla implements Regla<Object> {

    private static final Regla<Object> INSTANCIA = new AsegurarCorreoNoRegistradoRegla();

    private AsegurarCorreoNoRegistradoRegla() {
    }

    public static Regla<Object> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        String correo = (String) datos[0];
        DAOFactory daoFactory = (DAOFactory) datos[1];

        UsuarioEntidad filtro = new UsuarioEntidad();
        filtro.setCorreo(correo);
        if (!daoFactory.obtenerUsuarioDAO().consultarPorFiltro(filtro).isEmpty()) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_CORREO_YA_REGISTRADO);
        }
    }
}
