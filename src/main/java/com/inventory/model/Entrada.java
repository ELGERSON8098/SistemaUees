package com.inventory.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Entrada {
    private int idEntrada;
    private int idProducto;
    private int idProveedor;
    private Date fecha;
    private int cantidad;
    private BigDecimal costo;
    private String nombreProducto;
    private String nombreProveedor;

    public Entrada() {
    }

    public Entrada(int idProducto, int idProveedor, Date fecha, int cantidad, BigDecimal costo) {
        this.idProducto = idProducto;
        this.idProveedor = idProveedor;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.costo = costo;
    }

    public Entrada(int idEntrada, int idProducto, int idProveedor, Date fecha, int cantidad, BigDecimal costo, String nombreProducto, String nombreProveedor) {
        this.idEntrada = idEntrada;
        this.idProducto = idProducto;
        this.idProveedor = idProveedor;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.costo = costo;
        this.nombreProducto = nombreProducto;
        this.nombreProveedor = nombreProveedor;
    }

    public int getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(int idEntrada) {
        this.idEntrada = idEntrada;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }
}
