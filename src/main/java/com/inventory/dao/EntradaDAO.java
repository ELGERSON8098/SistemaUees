package com.inventory.dao;

import com.inventory.config.DatabaseConfig;
import com.inventory.model.Entrada;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntradaDAO {

    public boolean agregarEntrada(Entrada entrada) {
        String query = "INSERT INTO entradas (id_producto, id_proveedor, fecha, cantidad, costo) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, entrada.getIdProducto());
            pstmt.setInt(2, entrada.getIdProveedor());
            pstmt.setDate(3, entrada.getFecha());
            pstmt.setInt(4, entrada.getCantidad());
            pstmt.setBigDecimal(5, entrada.getCosto());
            
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Entrada> obtenerTodasEntradas() {
        List<Entrada> entradas = new ArrayList<>();
        String query = "SELECT e.id_entrada, e.id_producto, e.id_proveedor, e.fecha, e.cantidad, e.costo, " +
                       "p.nombre as producto_nombre, pr.nombre as proveedor_nombre " +
                       "FROM entradas e " +
                       "LEFT JOIN productos p ON e.id_producto = p.id_producto " +
                       "LEFT JOIN proveedores pr ON e.id_proveedor = pr.id_proveedor";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Entrada entrada = new Entrada(
                    rs.getInt("id_entrada"),
                    rs.getInt("id_producto"),
                    rs.getInt("id_proveedor"),
                    rs.getDate("fecha"),
                    rs.getInt("cantidad"),
                    rs.getBigDecimal("costo"),
                    rs.getString("producto_nombre"),
                    rs.getString("proveedor_nombre")
                );
                entradas.add(entrada);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return entradas;
    }

    public boolean eliminarEntrada(int idEntrada) {
        String query = "DELETE FROM entradas WHERE id_entrada = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idEntrada);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
