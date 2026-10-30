package com.manosalaobra.controlador.seguridad;

import com.manosalaobra.dominio.RolDominio;
import com.manosalaobra.dto.UsuarioDTO;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.UUID;

public final class SesionUsuario {

    public static final String ATRIBUTO_ID = "usuarioId";
    public static final String ATRIBUTO_NOMBRE = "usuarioNombre";
    public static final String ATRIBUTO_ROL = "usuarioRol";

    private SesionUsuario() {
    }

    public static void iniciar(HttpServletRequest peticion, UsuarioDTO usuario) {
        HttpSession anterior = peticion.getSession(false);
        if (anterior != null) {
            anterior.invalidate();
        }
        HttpSession sesion = peticion.getSession(true);
        sesion.setAttribute(ATRIBUTO_ID, usuario.getId().toString());
        sesion.setAttribute(ATRIBUTO_NOMBRE, usuario.getNombre());
        sesion.setAttribute(ATRIBUTO_ROL, usuario.getRol().getNombre());
    }

    public static void cerrar(HttpServletRequest peticion) {
        HttpSession sesion = peticion.getSession(false);
        if (sesion != null) {
            sesion.invalidate();
        }
    }

    public static String rolActual(HttpServletRequest peticion) {
        HttpSession sesion = peticion.getSession(false);
        if (sesion == null) {
            return null;
        }
        return (String) sesion.getAttribute(ATRIBUTO_ROL);
    }

    public static UUID idActual(HttpServletRequest peticion) {
        HttpSession sesion = peticion.getSession(false);
        String id = sesion == null ? null : (String) sesion.getAttribute(ATRIBUTO_ID);
        return UtilUUID.convertirAUUID(UtilUUID.obtenerValorDefectoComoTexto(id));
    }

    public static String nombreActual(HttpServletRequest peticion) {
        HttpSession sesion = peticion.getSession(false);
        return sesion == null ? null : (String) sesion.getAttribute(ATRIBUTO_NOMBRE);
    }

    public static String paginaDeInicio(String rol) {
        if (RolDominio.TECNICO.equals(rol)) {
            return "/tecnico/inicio.html";
        }
        if (RolDominio.ADMIN.equals(rol)) {
            return "/admin/inicio.html";
        }
        return "/ciudadano/inicio.html";
    }
}
