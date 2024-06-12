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

public class DialogoModificarCita extends DialogFragment {

    private static final String TAG = "DialogoModificarCita";

    private int clienteId;
    private Button btnSeleccionarFecha;
    private Button btnSeleccionarHora;
    private EditText editTextDescripcion;
    private ApiService apiService;
    private Cita cita;
    private CitaModificadaListener listener;

    private int year, month, day, hour, minute;
    private String fechaSeleccionada, horaSeleccionada;

    public DialogoModificarCita(Cita cita, CitaModificadaListener listener) {
        this.cita = cita;
        this.listener = listener;
        this.apiService = RetrofitClient.getApiService();
        this.clienteId = cita.getClienteId();
        Log.d(TAG, "Constructor: clienteId = " + clienteId);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_modificar_cita, null);

        btnSeleccionarFecha = view.findViewById(R.id.btnSeleccionarFecha);
        btnSeleccionarHora = view.findViewById(R.id.btnSeleccionarHora);
        editTextDescripcion = view.findViewById(R.id.editTextDescripcion);

        btnSeleccionarFecha.setText("Fecha: " + getShortDate(cita.getFecha()));
        btnSeleccionarHora.setText("Hora: " + cita.getHora());
        editTextDescripcion.setText(cita.getDescripcion());

        fechaSeleccionada = getShortDate(cita.getFecha());
        horaSeleccionada = cita.getHora();

        btnSeleccionarFecha.setOnClickListener(v -> mostrarDatePickerDialog());
        btnSeleccionarHora.setOnClickListener(v -> mostrarTimePickerDialog());

        builder.setView(view)
                .setTitle("Modificar Cita")
                .setPositiveButton("Modificar", (dialog, id) -> {
                    if (fechaSeleccionada == null || horaSeleccionada == null) {
                        Toast.makeText(getActivity(), "Por favor, selecciona fecha y hora", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    modificarCita();
                })
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());

        Log.d(TAG, "onCreateDialog: Dialog created with clienteId = " + clienteId);
        return builder.create();
    }

    /**
     * Muestra un DatePickerDialog para seleccionar una fecha.
     */
    private void mostrarDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            fechaSeleccionada = selectedDate;
            btnSeleccionarFecha.setText("Fecha: " + fechaSeleccionada);
            Log.d(TAG, "Fecha seleccionada: " + fechaSeleccionada);
        }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * Muestra un TimePickerDialog para seleccionar una hora.
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
     * Envía una solicitud a la API para modificar una cita existente.
     */
    private void modificarCita() {
        String descripcion = editTextDescripcion.getText().toString();
        cita.setFecha(fechaSeleccionada);
        cita.setHora(horaSeleccionada);
        cita.setDescripcion(descripcion);

        Log.d(TAG, "Modificando Cita: " + cita.toString());

        apiService.actualizarCita(cita.getIdCita(), cita).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.onCitaModificada();
                    Toast.makeText(getActivity(), "Cita modificada con éxito", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Cita modificada con éxito");
                } else {
                    Toast.makeText(getActivity(), "Error al modificar cita", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Error al modificar cita: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Error de red", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error de red al modificar cita", t);
            }
        });
    }

    /**
     * Obtiene una representación corta de una fecha en formato "yyyy-MM-dd".
     *
     * @param fullDate La fecha completa en formato "yyyy-MM-dd'T'HH:mm:ss".
     * @return La fecha en formato "yyyy-MM-dd".
     */
    private String getShortDate(String fullDate) {
        if (fullDate != null && fullDate.length() >= 10) {
            return fullDate.substring(0, 10);
        }
        return fullDate;
    }

    public interface CitaModificadaListener {
        void onCitaModificada();
    }
}
