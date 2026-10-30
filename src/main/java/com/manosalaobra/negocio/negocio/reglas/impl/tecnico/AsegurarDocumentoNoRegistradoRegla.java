package com.manosalaobra.negocio.negocio.reglas.impl.tecnico;

import com.manosalaobra.dao.factoria.DAOFactory;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;

public final class AsegurarDocumentoNoRegistradoRegla implements Regla<Object> {

    private static final Regla<Object> INSTANCIA = new AsegurarDocumentoNoRegistradoRegla();

    private AsegurarDocumentoNoRegistradoRegla() {
    }

    public static Regla<Object> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        String documento = (String) datos[0];
        DAOFactory daoFactory = (DAOFactory) datos[1];

        if (daoFactory.obtenerTecnicoDAO().existeDocumento(documento)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.UsuarioNegocioImpl.USUARIO_ERROR_DOCUMENTO_YA_REGISTRADO);
        }
    }
}
