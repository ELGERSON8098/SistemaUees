package com.inventory.model;

public class Inventario {
    private int idInventario;
    private int idProducto;
    private int stockActual;
    private String nombreProducto;

    public Inventario() {
    }

    public Inventario(int idProducto, int stockActual) {
        this.idProducto = idProducto;
        this.stockActual = stockActual;
    }

    public Inventario(int idInventario, int idProducto, int stockActual, String nombreProducto) {
        this.idInventario = idInventario;
        this.idProducto = idProducto;
        this.stockActual = stockActual;
        this.nombreProducto = nombreProducto;
    }

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
}
