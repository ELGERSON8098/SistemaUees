package com.inventory.dao;

import com.inventory.config.DatabaseConfig;
import com.inventory.model.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    public boolean agregarProveedor(Proveedor proveedor) {
        String query = "INSERT INTO proveedores (nombre, telefono, direccion) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, proveedor.getNombre());
            pstmt.setString(2, proveedor.getTelefono());
            pstmt.setString(3, proveedor.getDireccion());
            
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Proveedor> obtenerTodosProveedores() {
        List<Proveedor> proveedores = new ArrayList<>();
        String query = "SELECT * FROM proveedores";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Proveedor proveedor = new Proveedor(
                    rs.getInt("id_proveedor"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    rs.getString("direccion")
                );
                proveedores.add(proveedor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return proveedores;
    }

    public boolean actualizarProveedor(Proveedor proveedor) {
        String query = "UPDATE proveedores SET nombre = ?, telefono = ?, direccion = ? WHERE id_proveedor = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, proveedor.getNombre());
            pstmt.setString(2, proveedor.getTelefono());
            pstmt.setString(3, proveedor.getDireccion());
            pstmt.setInt(4, proveedor.getIdProveedor());
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarProveedor(int idProveedor) {
        String query = "DELETE FROM proveedores WHERE id_proveedor = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idProveedor);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Proveedor obtenerProveedorPorId(int idProveedor) {
        String query = "SELECT * FROM proveedores WHERE id_proveedor = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idProveedor);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Proveedor(
                    rs.getInt("id_proveedor"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    rs.getString("direccion")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
