package com.example.tfg_carlosramos.Model;

import com.google.gson.annotations.SerializedName;

public class Propiedad {
    @SerializedName("id_propiedad")
    private int idPropiedad;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("ubicacion")
    private String ubicacion;

    @SerializedName("precio")
    private double precio;

    @SerializedName("estado")
    private String estado;

    public Propiedad(String titulo, String descripcion, String ubicacion, double precio, String estado) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.precio = precio;
        this.estado = estado;
    }

    public int getIdPropiedad() {
        return idPropiedad;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public double getPrecio() {
        return precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setIdPropiedad(int idPropiedad) {
        this.idPropiedad = idPropiedad;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
