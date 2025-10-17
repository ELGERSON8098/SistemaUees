package com.sistemaues.app.service;

import com.sistemaues.app.dao.UserDao;
import com.sistemaues.app.model.User;

import java.sql.SQLException;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User register(String nombre, String usuario, String contrasena, String rol) throws SQLException {
        if (nombre == null || nombre.isBlank() || usuario == null || usuario.isBlank() || contrasena == null || contrasena.isBlank()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
        String rolNormalizado = rol == null || rol.isBlank() ? "empleado" : rol;
        return userDao.register(nombre.trim(), usuario.trim(), contrasena, rolNormalizado);
    }

    public Optional<User> authenticate(String usuario, String contrasena) throws SQLException {
        if (usuario == null || usuario.isBlank() || contrasena == null || contrasena.isBlank()) {
            return Optional.empty();
        }
        return userDao.login(usuario.trim(), contrasena);
    }
}
