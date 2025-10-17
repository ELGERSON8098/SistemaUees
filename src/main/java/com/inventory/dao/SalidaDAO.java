package com.inventory.dao;

import com.inventory.config.DatabaseConfig;
import com.inventory.model.Salida;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalidaDAO {

    public boolean agregarSalida(Salida salida) {
        String query = "INSERT INTO salidas (id_producto, fecha, cantidad, destino) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, salida.getIdProducto());
            pstmt.setDate(2, salida.getFecha());
            pstmt.setInt(3, salida.getCantidad());
            pstmt.setString(4, salida.getDestino());
            
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Salida> obtenerTodasSalidas() {
        List<Salida> salidas = new ArrayList<>();
        String query = "SELECT s.id_salida, s.id_producto, s.fecha, s.cantidad, s.destino, p.nombre as producto_nombre " +
                       "FROM salidas s " +
                       "LEFT JOIN productos p ON s.id_producto = p.id_producto";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Salida salida = new Salida(
                    rs.getInt("id_salida"),
                    rs.getInt("id_producto"),
                    rs.getDate("fecha"),
                    rs.getInt("cantidad"),
                    rs.getString("destino"),
                    rs.getString("producto_nombre")
                );
                salidas.add(salida);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return salidas;
    }

    public boolean eliminarSalida(int idSalida) {
        String query = "DELETE FROM salidas WHERE id_salida = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idSalida);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
