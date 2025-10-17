package com.sistemaues.app.model;

import java.time.LocalDateTime;

public class User {
    private final int id;
    private final String nombre;
    private final String usuario;
    private final String rol;
    private final LocalDateTime fechaRegistro;

    public User(int id, String nombre, String usuario, String rol, LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.rol = rol;
        this.fechaRegistro = fechaRegistro;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getRol() {
        return rol;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}
