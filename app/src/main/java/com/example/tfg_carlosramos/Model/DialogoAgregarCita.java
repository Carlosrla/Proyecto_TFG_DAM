package com.example.tfg_carlosramos.Model;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.network.ApiService;
import com.example.tfg_carlosramos.network.RetrofitClient;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * DialogFragment para agregar una nueva cita.
 */
public class DialogoAgregarCita extends DialogFragment {

    private static final String TAG = "DialogoAgregarCita";

    private int clienteId;
    private Button btnSeleccionarFecha;
    private Button btnSeleccionarHora;
    private EditText editTextDescripcion;
    private ApiService apiService;
    private CitaAgregadaListener listener;

    private int year, month, day, hour, minute;
    private String fechaSeleccionada, horaSeleccionada;

    /**
     * Constructor del diálogo para agregar una cita.
     *
     * @param clienteId ID del cliente.
     * @param listener Listener para notificar cuando una cita ha sido agregada.
     */
    public DialogoAgregarCita(int clienteId, CitaAgregadaListener listener) {
        this.clienteId = clienteId;
        this.listener = listener;
        this.apiService = RetrofitClient.getApiService();
        Log.d(TAG, "Constructor: clienteId = " + clienteId);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_agregar_cita, null);

        btnSeleccionarFecha = view.findViewById(R.id.btnSeleccionarFecha);
        btnSeleccionarHora = view.findViewById(R.id.btnSeleccionarHora);
        editTextDescripcion = view.findViewById(R.id.editTextDescripcion);

        btnSeleccionarFecha.setOnClickListener(v -> mostrarDatePickerDialog());
        btnSeleccionarHora.setOnClickListener(v -> mostrarTimePickerDialog());

        builder.setView(view)
                .setTitle("Agregar Cita")
                .setPositiveButton("Agregar", (dialog, id) -> agregarCita())
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());

        Log.d(TAG, "onCreateDialog: Dialog created with clienteId = " + clienteId);
        return builder.create();
    }

    /**
     * Muestra el diálogo para seleccionar la fecha.
     */
    private void mostrarDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> {
            fechaSeleccionada = year + "-" + (month + 1) + "-" + dayOfMonth;
            btnSeleccionarFecha.setText("Fecha: " + fechaSeleccionada);
            Log.d(TAG, "Fecha seleccionada: " + fechaSeleccionada);
        }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * Muestra el diálogo para seleccionar la hora.
     */
    private void mostrarTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (view, hourOfDay, minute) -> {
            horaSeleccionada = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute);
            btnSeleccionarHora.setText("Hora: " + horaSeleccionada);
            Log.d(TAG, "Hora seleccionada: " + horaSeleccionada);
        }, hour, minute, true);
        timePickerDialog.show();
    }

    /**
     * Agrega una nueva cita utilizando la API.
     */
    private void agregarCita() {
        String descripcion = editTextDescripcion.getText().toString();
        Log.d(TAG, "Agregar Cita: clienteId = " + clienteId);
        if (fechaSeleccionada == null || horaSeleccionada == null) {
            Toast.makeText(getActivity(), "Por favor, selecciona fecha y hora", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Fecha u hora no seleccionada");
            return;
        }

        Cita nuevaCita = new Cita(clienteId, fechaSeleccionada, horaSeleccionada, descripcion);
        Log.d(TAG, "Nueva Cita: " + nuevaCita.toString());

        apiService.agregarCita(nuevaCita).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.onCitaAgregada();
                    Toast.makeText(getActivity(), "Cita agregada con éxito", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Cita agregada con éxito");
                } else {
                    Toast.makeText(getActivity(), "Error al agregar cita", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Error al agregar cita: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Error de red", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error de red al agregar cita", t);
            }
        });
    }

    /**
     * Interfaz para notificar cuando una cita ha sido agregada.
     */
    public interface CitaAgregadaListener {
        void onCitaAgregada();
    }
}

