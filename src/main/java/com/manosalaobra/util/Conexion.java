package com.manosalaobra.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Entrega conexiones JDBC usando db.properties. */
public final class Conexion {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = Conexion.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) throw new IllegalStateException("Falta db.properties");
            PROPS.load(in);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Conexion() { }

    public static Connection obtener() throws SQLException {
        return DriverManager.getConnection(
                PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"),
                PROPS.getProperty("db.password"));
    }
}
