package com.sistemaues.app.db;

import com.sistemaues.app.util.ConfigLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
    private static final String URL = ConfigLoader.get("db.url");
    private static final String USERNAME = ConfigLoader.get("db.username");
    private static final String PASSWORD = ConfigLoader.get("db.password");

    private ConnectionManager() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
