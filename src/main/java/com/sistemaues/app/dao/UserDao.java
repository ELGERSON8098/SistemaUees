package com.sistemaues.app.dao;

import com.sistemaues.app.db.ConnectionManager;
import com.sistemaues.app.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserDao {
    private static final String INSERT_USER = "INSERT INTO usuarios (nombre, usuario, contrasena, rol) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_USERNAME = "SELECT id_usuario, nombre, usuario, rol, fecha_registro, contrasena FROM usuarios WHERE usuario = ?";

    public boolean isUsernameTaken(String username) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM usuarios WHERE usuario = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public User register(String nombre, String username, String password, String rol) throws SQLException {
        if (isUsernameTaken(username)) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        byte[] hashedPassword = hashPassword(password);
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, nombre);
            statement.setString(2, username);
            statement.setBytes(3, hashedPassword);
            statement.setString(4, rol);
            int affected = statement.executeUpdate();
            if (affected == 0) {
                throw new SQLException("No se pudo registrar el usuario");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return new User(id, nombre, username, rol, LocalDateTime.now());
                }
            }
        }
        throw new SQLException("No se pudo obtener el ID del usuario registrado");
    }

    public Optional<User> login(String username, String password) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_USERNAME)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                byte[] storedHash = resultSet.getBytes("contrasena");
                byte[] inputHash = hashPassword(password);
                if (!MessageDigest.isEqual(storedHash, inputHash)) {
                    return Optional.empty();
                }
                int id = resultSet.getInt("id_usuario");
                String nombre = resultSet.getString("nombre");
                String rol = resultSet.getString("rol");
                Timestamp timestamp = resultSet.getTimestamp("fecha_registro");
                LocalDateTime fechaRegistro = timestamp == null ? null : timestamp.toLocalDateTime();
                return Optional.of(new User(id, nombre, username, rol, fechaRegistro));
            }
        }
    }

    private byte[] hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("El algoritmo de hash SHA-256 no está disponible", e);
        }
    }
}
