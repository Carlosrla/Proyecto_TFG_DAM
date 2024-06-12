package com.example.tfg_carlosramos.Model;

import com.google.gson.annotations.SerializedName;

public class Nota {

    @SerializedName("id_nota")
    private int idNota;

    @SerializedName("id_cliente")
    private int idCliente;

    @SerializedName("contenido")
    private String contenido;

    @SerializedName("Fecha_Creacion")
    private String fechaCreacion;

    @SerializedName("Fecha_Modificacion")
    private String fechaModificacion;

    public Nota(int idNota, int idCliente, String contenido, String fechaCreacion, String fechaModificacion) {
        this.idNota = idNota;
        this.idCliente = idCliente;
        this.contenido = contenido;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public int getIdNota() {
        return idNota;
    }

    public void setIdNota(int idNota) {
        this.idNota = idNota;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
