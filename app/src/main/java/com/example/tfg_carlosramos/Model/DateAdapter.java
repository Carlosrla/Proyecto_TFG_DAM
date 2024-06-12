package com.example.tfg_carlosramos.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Clase utilitaria para adaptar y formatear fechas.
 */
public class DateAdapter {

    // Formato de entrada utilizado para analizar la fecha de la base de datos
    private static final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

    // Formato de salida utilizado para mostrar la fecha en la aplicación
    private static final SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    /**
     * Formatea una fecha desde el formato de entrada al formato de salida.
     *
     * @param dateString la fecha en formato de entrada como cadena.
     * @return la fecha formateada como cadena, o la cadena original en caso de error.
     */
    public static String format(String dateString) {
        try {
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString; // Devuelve la cadena original en caso de error
        }
    }
}
