package com.example.tfg_carlosramos.Model;

import com.google.gson.annotations.SerializedName;

public class Transaccion {

    @SerializedName("id_transaccion")
    private int idTransaccion;

    @SerializedName("id_cliente")
    private int idCliente;

    @SerializedName("id_propiedad")
    private int idPropiedad;

    @SerializedName("precio")
    private double precio;

    @SerializedName("forma_pago")
    private String formaPago;

    @SerializedName("estado")
    private String estado;

    @SerializedName("notas")
    private String notas;

    public Transaccion(int idCliente, int idPropiedad, double precio, String formaPago, String estado, String notas) {
        this.idCliente = idCliente;
        this.idPropiedad = idPropiedad;
        this.precio = precio;
        this.formaPago = formaPago;
        this.estado = estado;
        this.notas = notas;
    }

    public int getIdTransaccion() {
        return idTransaccion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdPropiedad() {
        return idPropiedad;
    }

    public double getPrecio() {
        return precio;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public String getEstado() {
        return estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdPropiedad(int idPropiedad) {
        this.idPropiedad = idPropiedad;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    @Override
    public String toString() {
        return "Transaccion{" +
                "idTransaccion=" + idTransaccion +
                ", idCliente=" + idCliente +
                ", idPropiedad=" + idPropiedad +
                ", precio=" + precio +
                ", formaPago='" + formaPago + '\'' +
                ", estado='" + estado + '\'' +
                ", notas='" + notas + '\'' +
                '}';
    }
}