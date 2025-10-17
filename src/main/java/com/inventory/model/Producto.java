package com.inventory.model;

import java.math.BigDecimal;

public class Producto {
    private int idProducto;
    private String nombre;
    private String descripcion;
    private int idCategoria;
    private BigDecimal precio;
    private String nombreCategoria;

    public Producto() {
    }

    public Producto(String nombre, String descripcion, int idCategoria, BigDecimal precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.precio = precio;
    }

    public Producto(int idProducto, String nombre, String descripcion, int idCategoria, BigDecimal precio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.precio = precio;
    }

    public Producto(int idProducto, String nombre, String descripcion, int idCategoria, BigDecimal precio, String nombreCategoria) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.precio = precio;
        this.nombreCategoria = nombreCategoria;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}
