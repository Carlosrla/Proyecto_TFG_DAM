package com.example.tfg_carlosramos.Model;

import com.google.gson.annotations.SerializedName;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Cita {
    @SerializedName("id_visita")
    private int idCita;  // Asegúrate de que este campo existe en tu JSON

    @SerializedName("id_cliente")
    private int clienteId;

    @SerializedName("Fecha_Visita")
    private String fecha;

    @SerializedName("Hora_Visita")
    private String hora;

    @SerializedName("comentarios")
    private String descripcion;

    public Cita(int clienteId, String fecha, String hora, String descripcion) {
        this.clienteId = clienteId;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descripcion;
    }

    // Getters y setters
    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Método para obtener la fecha en formato compatible con MySQL
    public String getFechaParaMySQL() {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = inputFormat.parse(fecha);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return fecha; // En caso de error, devolver la fecha sin formatear
        }
    }

    @Override
    public String toString() {
        return "Cita{" +
                "idCita=" + idCita +
                ", clienteId=" + clienteId +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
