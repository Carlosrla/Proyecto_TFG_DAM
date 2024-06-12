package com.example.tfg_carlosramos.Model;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.network.ApiService;
import com.example.tfg_carlosramos.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogoAgregarNota extends DialogFragment {

    private static final String ARG_CLIENTE_ID = "cliente_id";

    private int clienteId;
    private EditText editTextContenido;
    private ApiService apiService;
    private NotaAgregadaListener listener;

    public DialogoAgregarNota(int clienteId, NotaAgregadaListener listener) {
        this.clienteId = clienteId;
        this.listener = listener;
        this.apiService = RetrofitClient.getApiService();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_agregar_nota, null);

        editTextContenido = view.findViewById(R.id.editTextContenido);

        builder.setView(view)
                .setTitle("Agregar Nota")
                .setPositiveButton("Agregar", (dialog, id) -> agregarNota())
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());

        return builder.create();
    }

    /**
     * Envía una solicitud a la API para agregar una nueva nota.
     */
    private void agregarNota() {
        String contenido = editTextContenido.getText().toString();

        // Crear una nueva Nota con el constructor correcto
        Nota nuevaNota = new Nota(0, clienteId, contenido, "", "");

        apiService.agregarNota(nuevaNota).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.onNotaAgregada();
                    Toast.makeText(getActivity(), "Nota agregada con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error al agregar nota", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface NotaAgregadaListener {
        void onNotaAgregada();
    }
}
