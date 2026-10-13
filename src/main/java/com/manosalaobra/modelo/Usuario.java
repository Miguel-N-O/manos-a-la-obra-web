package com.manosalaobra.modelo;

public class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String claveHash;
    private String telefono;
    private Rol rol;

    public Usuario() { }

    public Usuario(String nombre, String correo, String claveHash, String telefono, Rol rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.claveHash = claveHash;
        this.telefono = telefono;
        this.rol = rol;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getClaveHash() { return claveHash; }
    public void setClaveHash(String claveHash) { this.claveHash = claveHash; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}
