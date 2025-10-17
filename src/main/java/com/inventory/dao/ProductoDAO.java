package com.inventory.dao;

import com.inventory.config.DatabaseConfig;
import com.inventory.model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public boolean agregarProducto(Producto producto) {
        String query = "INSERT INTO productos (nombre, descripcion, id_categoria, precio) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setInt(3, producto.getIdCategoria());
            pstmt.setBigDecimal(4, producto.getPrecio());
            
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Producto> obtenerTodosProductos() {
        List<Producto> productos = new ArrayList<>();
        String query = "SELECT p.id_producto, p.nombre, p.descripcion, p.id_categoria, p.precio, c.nombre as categoria_nombre " +
                       "FROM productos p LEFT JOIN categorias c ON p.id_categoria = c.id_categoria";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getInt("id_categoria"),
                    rs.getBigDecimal("precio"),
                    rs.getString("categoria_nombre")
                );
                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return productos;
    }

    public List<Producto> obtenerProductosPorCategoria(int idCategoria) {
        List<Producto> productos = new ArrayList<>();
        String query = "SELECT p.id_producto, p.nombre, p.descripcion, p.id_categoria, p.precio, c.nombre as categoria_nombre " +
                       "FROM productos p LEFT JOIN categorias c ON p.id_categoria = c.id_categoria " +
                       "WHERE p.id_categoria = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idCategoria);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getInt("id_categoria"),
                    rs.getBigDecimal("precio"),
                    rs.getString("categoria_nombre")
                );
                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return productos;
    }

    public boolean actualizarProducto(Producto producto) {
        String query = "UPDATE productos SET nombre = ?, descripcion = ?, id_categoria = ?, precio = ? WHERE id_producto = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setInt(3, producto.getIdCategoria());
            pstmt.setBigDecimal(4, producto.getPrecio());
            pstmt.setInt(5, producto.getIdProducto());
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarProducto(int idProducto) {
        String query = "DELETE FROM productos WHERE id_producto = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idProducto);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Producto obtenerProductoPorId(int idProducto) {
        String query = "SELECT p.id_producto, p.nombre, p.descripcion, p.id_categoria, p.precio, c.nombre as categoria_nombre " +
                       "FROM productos p LEFT JOIN categorias c ON p.id_categoria = c.id_categoria " +
                       "WHERE p.id_producto = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Producto(
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getInt("id_categoria"),
                    rs.getBigDecimal("precio"),
                    rs.getString("categoria_nombre")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
