package com.inventory.model;

import java.sql.Date;

public class Salida {
    private int idSalida;
    private int idProducto;
    private Date fecha;
    private int cantidad;
    private String destino;
    private String nombreProducto;

    public Salida() {
    }

    public Salida(int idProducto, Date fecha, int cantidad, String destino) {
        this.idProducto = idProducto;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.destino = destino;
    }

    public Salida(int idSalida, int idProducto, Date fecha, int cantidad, String destino, String nombreProducto) {
        this.idSalida = idSalida;
        this.idProducto = idProducto;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.destino = destino;
        this.nombreProducto = nombreProducto;
    }

    public int getIdSalida() {
        return idSalida;
    }

    public void setIdSalida(int idSalida) {
        this.idSalida = idSalida;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
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

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
}
