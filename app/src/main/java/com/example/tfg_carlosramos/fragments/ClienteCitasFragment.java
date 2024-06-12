package com.example.tfg_carlosramos.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tfg_carlosramos.Model.Cita;
import com.example.tfg_carlosramos.Model.DialogoAgregarCita;
import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.adapter.CitasAdapter;
import com.example.tfg_carlosramos.network.ApiService;
import com.example.tfg_carlosramos.network.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento para mostrar y gestionar las citas de un cliente específico.
 */
public class ClienteCitasFragment extends Fragment implements DialogoAgregarCita.CitaAgregadaListener {

    private static final String ARG_CLIENTE_ID = "cliente_id";
    private static final String ARG_CLIENTE_NOMBRE = "cliente_nombre";

    private int clienteId;
    private String clienteNombre;
    private RecyclerView recyclerView;
    private CitasAdapter citasAdapter;
    private ApiService apiService;
    private TextView nombreClienteTextView;
    private FloatingActionButton fabAddCitaCliente;

    /**
     * Crea una nueva instancia del fragmento con los parámetros proporcionados.
     *
     * @param clienteId    ID del cliente.
     * @param clienteNombre Nombre del cliente.
     * @return Una nueva instancia de ClienteCitasFragment.
     */
    public static ClienteCitasFragment newInstance(int clienteId, String clienteNombre) {
        ClienteCitasFragment fragment = new ClienteCitasFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CLIENTE_ID, clienteId);
        args.putString(ARG_CLIENTE_NOMBRE, clienteNombre);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clienteId = getArguments().getInt(ARG_CLIENTE_ID);
            clienteNombre = getArguments().getString(ARG_CLIENTE_NOMBRE);
        }
        apiService = RetrofitClient.getApiService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cliente_citas, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewCitas);
        nombreClienteTextView = view.findViewById(R.id.nombreClienteCitas);
        fabAddCitaCliente = view.findViewById(R.id.fabAddCitaCliente);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        citasAdapter = new CitasAdapter(new ArrayList<>(), apiService, getContext());
        recyclerView.setAdapter(citasAdapter);

        nombreClienteTextView.setText("Citas de " + clienteNombre);  // Establecer el nombre del cliente

        cargarCitasDelCliente();

        fabAddCitaCliente.setOnClickListener(v -> {
            Log.d("ClienteCitasFragment", "Creando DialogoAgregarCita con clienteId = " + clienteId);
            DialogoAgregarCita dialogoAgregarCita = new DialogoAgregarCita(clienteId, this);
            dialogoAgregarCita.show(getParentFragmentManager(), "DialogoAgregarCita");
        });

        return view;
    }

    /**
     * Carga las citas del cliente desde el servidor.
     */
    private void cargarCitasDelCliente() {
        apiService.getCitasDelCliente(clienteId).enqueue(new Callback<List<Cita>>() {
            @Override
            public void onResponse(Call<List<Cita>> call, Response<List<Cita>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    citasAdapter.updateCitas(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Cita>> call, Throwable t) {
                // Manejo de errores
                Log.e("ClienteCitasFragment", "Error al cargar las citas del cliente: " + t.getMessage());
            }
        });
    }

    @Override
    public void onCitaAgregada() {
        cargarCitasDelCliente();
    }
}
