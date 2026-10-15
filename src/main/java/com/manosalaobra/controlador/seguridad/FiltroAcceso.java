package com.manosalaobra.controlador.seguridad;

import com.manosalaobra.dominio.RolDominio;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;

/**
 * Protege las zonas /ciudadano, /tecnico y /admin: sin sesión manda al login; con otro rol manda a la
 * página de inicio del rol que sí tiene.
 */
@Component
public class FiltroAcceso extends OncePerRequestFilter {

    private static final String PAGINA_LOGIN = "/login.html";

    @Override
    protected void doFilterInternal(HttpServletRequest peticion, HttpServletResponse respuesta, FilterChain cadena)
            throws ServletException, IOException {
        String rolRequerido = rolRequerido(normalizarRuta(peticion));
        if (rolRequerido == null) {
            cadena.doFilter(peticion, respuesta);
            return;
        }
        String rolActual = SesionUsuario.rolActual(peticion);
        if (rolActual == null) {
            respuesta.sendRedirect(peticion.getContextPath() + PAGINA_LOGIN);
        } else if (!rolRequerido.equals(rolActual)) {
            respuesta.sendRedirect(peticion.getContextPath() + SesionUsuario.paginaDeInicio(rolActual));
        } else {
            cadena.doFilter(peticion, respuesta);
        }
    }

    private String normalizarRuta(HttpServletRequest peticion) {
        String ruta = peticion.getRequestURI().substring(peticion.getContextPath().length())
                .replaceAll("/{2,}", "/");
        try {
            ruta = URI.create(ruta).normalize().getPath();
        } catch (IllegalArgumentException excepcion) {
            ruta = "/";
        }
        ruta = ruta == null ? "/" : ruta.toLowerCase(Locale.ROOT);
        return ruta.endsWith("/") ? ruta : ruta + "/";
    }

    private String rolRequerido(String ruta) {
        if (ruta.startsWith("/ciudadano/")) {
            return RolDominio.CIUDADANO;
        }
        if (ruta.startsWith("/tecnico/")) {
            return RolDominio.TECNICO;
        }
        if (ruta.startsWith("/admin/")) {
            return RolDominio.ADMIN;
        }
        return null;
    }
}
