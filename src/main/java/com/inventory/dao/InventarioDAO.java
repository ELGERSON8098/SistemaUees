package com.inventory.dao;

import com.inventory.config.DatabaseConfig;
import com.inventory.model.Inventario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventarioDAO {

    public boolean agregarInventario(Inventario inventario) {
        String query = "INSERT INTO inventario (id_producto, stock_actual) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, inventario.getIdProducto());
            pstmt.setInt(2, inventario.getStockActual());
            
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Inventario> obtenerTodoInventario() {
        List<Inventario> inventarios = new ArrayList<>();
        String query = "SELECT i.id_inventario, i.id_producto, i.stock_actual, p.nombre as producto_nombre " +
                       "FROM inventario i " +
                       "LEFT JOIN productos p ON i.id_producto = p.id_producto";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Inventario inventario = new Inventario(
                    rs.getInt("id_inventario"),
                    rs.getInt("id_producto"),
                    rs.getInt("stock_actual"),
                    rs.getString("producto_nombre")
                );
                inventarios.add(inventario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return inventarios;
    }

    public boolean actualizarStock(int idProducto, int nuevoStock) {
        String query = "UPDATE inventario SET stock_actual = ? WHERE id_producto = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, nuevoStock);
            pstmt.setInt(2, idProducto);
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Inventario obtenerInventarioPorProducto(int idProducto) {
        String query = "SELECT i.id_inventario, i.id_producto, i.stock_actual, p.nombre as producto_nombre " +
                       "FROM inventario i " +
                       "LEFT JOIN productos p ON i.id_producto = p.id_producto " +
                       "WHERE i.id_producto = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Inventario(
                    rs.getInt("id_inventario"),
                    rs.getInt("id_producto"),
                    rs.getInt("stock_actual"),
                    rs.getString("producto_nombre")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
