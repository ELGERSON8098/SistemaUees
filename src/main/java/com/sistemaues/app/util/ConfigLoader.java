package com.sistemaues.app.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigLoader {
    private static final String CONFIG_FILE = "database.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new IllegalStateException("No se encontró el archivo de configuración " + CONFIG_FILE);
            }
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Error al cargar las configuraciones de base de datos", e);
        }
    }

    private ConfigLoader() {
    }

    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("La propiedad requerida " + key + " no está configurada");
        }
        return value.trim();
    }
}
