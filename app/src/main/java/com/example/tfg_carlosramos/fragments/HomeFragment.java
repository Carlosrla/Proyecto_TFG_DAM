package com.example.tfg_carlosramos.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_carlosramos.Model.Transaccion;
import com.example.tfg_carlosramos.Model.Cita;
import com.example.tfg_carlosramos.Model.Cliente;
import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.adapter.TransaccionesHomeAdapter;
import com.example.tfg_carlosramos.adapter.CitasHomeAdapter;
import com.example.tfg_carlosramos.network.ApiService;
import com.example.tfg_carlosramos.network.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento principal que muestra las transacciones pendientes y las próximas citas.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewTransaccionesPendientes;
    private RecyclerView recyclerViewCitas;
    private TextView noTransaccionesMessage;
    private TextView noCitasMessage;
    private TransaccionesHomeAdapter transaccionesHomeAdapter;
    private CitasHomeAdapter citasHomeAdapter;
    private ApiService apiService;
    private HashMap<Integer, Cliente> clienteCache = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewTransaccionesPendientes = view.findViewById(R.id.recyclerViewTransaccionesPendientes);
        recyclerViewCitas = view.findViewById(R.id.recyclerViewCitas);
        noTransaccionesMessage = view.findViewById(R.id.noTransaccionesMessage);
        noCitasMessage = view.findViewById(R.id.noCitasMessage);

        apiService = RetrofitClient.getApiService();

        recyclerViewTransaccionesPendientes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCitas.setLayoutManager(new LinearLayoutManager(getContext()));

        transaccionesHomeAdapter = new TransaccionesHomeAdapter(new ArrayList<>(), clienteCache);
        citasHomeAdapter = new CitasHomeAdapter(new ArrayList<>(), clienteCache);

        recyclerViewTransaccionesPendientes.setAdapter(transaccionesHomeAdapter);
        recyclerViewCitas.setAdapter(citasHomeAdapter);

        loadClientes();
    }

    /**
     * Carga la lista de clientes desde el servidor.
     */
    private void loadClientes() {
        apiService.getClientes().enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Cliente cliente : response.body()) {
                        clienteCache.put(cliente.getId(), cliente);
                    }
                    loadTransaccionesPendientes();
                    loadCitas();
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                // Manejar error
            }
        });
    }

    /**
     * Carga la lista de transacciones pendientes desde el servidor.
     */
    private void loadTransaccionesPendientes() {
        apiService.getTransacciones().enqueue(new Callback<List<Transaccion>>() {
            @Override
            public void onResponse(Call<List<Transaccion>> call, Response<List<Transaccion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Transaccion> transaccionesPendientes = new ArrayList<>();
                    for (Transaccion transaccion : response.body()) {
                        if ("Pendiente".equals(transaccion.getEstado())) {
                            transaccionesPendientes.add(transaccion);
                        }
                    }
                    transaccionesHomeAdapter.updateTransacciones(transaccionesPendientes);
                    noTransaccionesMessage.setVisibility(transaccionesPendientes.isEmpty() ? View.VISIBLE : View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Transaccion>> call, Throwable t) {
                // Manejar error
            }
        });
    }

    /**
     * Carga la lista de citas desde el servidor.
     */
    private void loadCitas() {
        apiService.getCitas().enqueue(new Callback<List<Cita>>() {
            @Override
            public void onResponse(Call<List<Cita>> call, Response<List<Cita>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Cita> citas = response.body();
                    citasHomeAdapter.updateCitas(citas);
                    noCitasMessage.setVisibility(citas.isEmpty() ? View.VISIBLE : View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Cita>> call, Throwable t) {
                // Manejar error
            }
        });
    }
}
