package com.inventory.util;

import com.inventory.model.Producto;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PdfExporter {
    
    public static boolean exportarProductos(List<Producto> productos, String rutaArchivo) throws Exception {
        if (!rutaArchivo.endsWith(".csv")) {
            rutaArchivo += ".csv";
        }

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(rutaArchivo), StandardCharsets.UTF_8)) {
            writer.write("\uFEFF");
            writer.write("ID;Nombre;Descripción;Categoría;Proveedor;Precio;Stock\r\n");
            
            for (Producto p : productos) {
                StringBuilder linea = new StringBuilder();
                linea.append(p.getIdProducto()).append(";");
                linea.append(escaparCSV(p.getNombre())).append(";");
                linea.append(escaparCSV(p.getDescripcion())).append(";");
                linea.append(escaparCSV(p.getNombreCategoria())).append(";");
                linea.append(escaparCSV(p.getNombreProveedor())).append(";");
                linea.append(p.getPrecio().toString()).append(";");
                linea.append(p.getStock());
                writer.write(linea.toString());
                writer.write("\r\n");
            }
        }

        return true;
    }
    
    private static String escaparCSV(String valor) {
        if (valor == null) {
            return "";
        }
        if (valor.contains(";") || valor.contains("\"") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
