package com.manosalaobra.dao;

import com.manosalaobra.modelo.Rol;
import com.manosalaobra.modelo.Usuario;
import com.manosalaobra.util.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/** Acceso a datos de usuarios. Siempre con PreparedStatement (evita inyección SQL). */
public class UsuarioDAO {

    public boolean existeCorreo(String correo) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE correo = ?";
        try (Connection c = Conexion.obtener(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Inserta el usuario y devuelve el id generado. */
    public int crear(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, correo, clave_hash, telefono, rol) VALUES (?,?,?,?,?)";
        try (Connection c = Conexion.obtener();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getClaveHash());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getRol().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    public Optional<Usuario> buscarPorCorreo(String correo) throws SQLException {
        String sql = "SELECT id, nombre, correo, clave_hash, telefono, rol FROM usuarios "
                   + "WHERE correo = ? AND estado = 'ACTIVO'";
        try (Connection c = Conexion.obtener(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setClaveHash(rs.getString("clave_hash"));
                u.setTelefono(rs.getString("telefono"));
                u.setRol(Rol.valueOf(rs.getString("rol")));
                return Optional.of(u);
            }
        }
    }
}
