package com.manosalaobra.controlador;

import com.manosalaobra.dao.UsuarioDAO;
import com.manosalaobra.modelo.Rol;
import com.manosalaobra.modelo.Usuario;
import com.manosalaobra.util.Claves;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/** HU-01: Registro de ciudadano. */
@WebServlet("/registro")
public class RegistroCiudadanoServlet extends HttpServlet {
    private final UsuarioDAO usuarios = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/registro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String nombre = valor(req, "nombre");
        String correo = valor(req, "correo");
        String clave = valor(req, "clave");
        String telefono = valor(req, "telefono");

        String error = validar(nombre, correo, clave);
        try {
            if (error == null && usuarios.existeCorreo(correo)) {
                error = "Ese correo ya está registrado.";
            }
            if (error == null) {
                usuarios.crear(new Usuario(nombre, correo, Claves.cifrar(clave), telefono, Rol.CIUDADANO));
                resp.sendRedirect(req.getContextPath() + "/index.jsp?registro=ok");
                return;
            }
        } catch (SQLException e) {
            error = "No pudimos guardar tus datos. Intenta de nuevo.";
            log("Error registrando ciudadano", e);
        }
        req.setAttribute("error", error);
        req.getRequestDispatcher("/registro.jsp").forward(req, resp);
    }

    private static String valor(HttpServletRequest req, String campo) {
        String v = req.getParameter(campo);
        return v == null ? "" : v.trim();
    }

    private static String validar(String nombre, String correo, String clave) {
        if (nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()) return "Completa todos los campos obligatorios.";
        if (!correo.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) return "El correo no es válido.";
        if (clave.length() < 8) return "Tu contraseña es muy corta (mínimo 8 caracteres).";
        return null;
    }
}
