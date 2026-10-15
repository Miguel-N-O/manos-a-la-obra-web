package com.manosalaobra.negocio.negocio.ensamblador.impl;

import com.manosalaobra.dominio.UsuarioDominio;
import com.manosalaobra.entidad.UsuarioEntidad;
import com.manosalaobra.negocio.negocio.ensamblador.EnsambladorEntidad;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;

public final class UsuarioEnsambladorEntidad implements EnsambladorEntidad<UsuarioDominio, UsuarioEntidad> {

    private static final EnsambladorEntidad<UsuarioDominio, UsuarioEntidad> INSTANCIA =
            new UsuarioEnsambladorEntidad();

    private UsuarioEnsambladorEntidad() {
    }

    public static EnsambladorEntidad<UsuarioDominio, UsuarioEntidad> getInstancia() {
        return INSTANCIA;
    }

    @Override
    public UsuarioEntidad convertirAEntidad(UsuarioDominio dominio) {
        UsuarioDominio dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                dominio, new UsuarioDominio.Builder().build());
        return new UsuarioEntidad(
                dominioSeguro.getId(),
                RolEnsambladorEntidad.getInstancia().convertirAEntidad(dominioSeguro.getRol()),
                EstadoUsuarioEnsambladorEntidad.getInstancia().convertirAEntidad(dominioSeguro.getEstadoUsuario()),
                dominioSeguro.getNombre(),
                dominioSeguro.getCorreo(),
                dominioSeguro.getClaveHash(),
                IndicativoPaisEnsambladorEntidad.getInstancia().convertirAEntidad(dominioSeguro.getIndicativoPais()),
                dominioSeguro.getNumeroTelefono());
    }

    @Override
    public UsuarioDominio convertirADominio(UsuarioEntidad entidad) {
        UsuarioEntidad entidadSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                entidad, new UsuarioEntidad());
        return new UsuarioDominio.Builder()
                .id(entidadSegura.getId())
                .rol(RolEnsambladorEntidad.getInstancia().convertirADominio(entidadSegura.getRol()))
                .estadoUsuario(EstadoUsuarioEnsambladorEntidad.getInstancia()
                        .convertirADominio(entidadSegura.getEstadoUsuario()))
                .nombre(entidadSegura.getNombre())
                .correo(entidadSegura.getCorreo())
                .claveHash(entidadSegura.getClaveHash())
                .indicativoPais(IndicativoPaisEnsambladorEntidad.getInstancia()
                        .convertirADominio(entidadSegura.getIndicativoPais()))
                .numeroTelefono(entidadSegura.getNumeroTelefono())
                .build();
    }
}
