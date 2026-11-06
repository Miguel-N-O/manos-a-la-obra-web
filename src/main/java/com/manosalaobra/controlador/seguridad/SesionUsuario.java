package com.manosalaobra.controlador.seguridad;

import com.manosalaobra.dominio.RolDominio;
import com.manosalaobra.dto.UsuarioDTO;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SesionUsuario {

    public static final String ATRIBUTO_ID = "usuarioId";
    public static final String ATRIBUTO_NOMBRE = "usuarioNombre";
    public static final String ATRIBUTO_ROL = "usuarioRol";

    // HU-16: cuentas suspendidas o bloqueadas mientras la aplicación está en marcha; su sesión deja de valer
    private static final Set<String> CUENTAS_RESTRINGIDAS = ConcurrentHashMap.newKeySet();

    private SesionUsuario() {
    }

    public static void restringir(UUID usuarioId) {
        CUENTAS_RESTRINGIDAS.add(usuarioId.toString());
    }

    public static void liberar(UUID usuarioId) {
        CUENTAS_RESTRINGIDAS.remove(usuarioId.toString());
    }

    // La sesión de una cuenta restringida se cierra y se trata como si no hubiera sesión
    private static HttpSession sesionVigente(HttpServletRequest peticion) {
        HttpSession sesion = peticion.getSession(false);
        if (sesion == null) {
            return null;
        }
        Object id = sesion.getAttribute(ATRIBUTO_ID);
        if (id != null && CUENTAS_RESTRINGIDAS.contains(id.toString())) {
            sesion.invalidate();
            return null;
        }
        return sesion;
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
        HttpSession sesion = sesionVigente(peticion);
        if (sesion == null) {
            return null;
        }
        return (String) sesion.getAttribute(ATRIBUTO_ROL);
    }

    public static UUID idActual(HttpServletRequest peticion) {
        HttpSession sesion = sesionVigente(peticion);
        String id = sesion == null ? null : (String) sesion.getAttribute(ATRIBUTO_ID);
        return UtilUUID.convertirAUUID(UtilUUID.obtenerValorDefectoComoTexto(id));
    }

    public static String nombreActual(HttpServletRequest peticion) {
        HttpSession sesion = sesionVigente(peticion);
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
