package com.example.tfg_carlosramos.Model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.adapter.TransaccionesAdapter;
import com.example.tfg_carlosramos.network.ApiService;
import com.example.tfg_carlosramos.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransaccionesListFragment extends Fragment {

    private static final String ARG_ESTADO = "estado";

    private String estado;
    private RecyclerView recyclerViewTransacciones;
    private TransaccionesAdapter transaccionAdapter;
    private ApiService apiService;

    /**
     * Crea una nueva instancia de TransaccionesListFragment con un estado específico.
     *
     * @param estado El estado de las transacciones a mostrar (e.g., "Pendiente", "Completada").
     * @return Una nueva instancia de TransaccionesListFragment.
     */
    public static TransaccionesListFragment newInstance(String estado) {
        TransaccionesListFragment fragment = new TransaccionesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ESTADO, estado);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transacciones_list, container, false);

        if (getArguments() != null) {
            estado = getArguments().getString(ARG_ESTADO);
        }

        recyclerViewTransacciones = view.findViewById(R.id.recyclerViewTransacciones);
        recyclerViewTransacciones.setLayoutManager(new LinearLayoutManager(getActivity()));

        apiService = RetrofitClient.getApiService();
        transaccionAdapter = new TransaccionesAdapter(new ArrayList<>(), apiService, getContext());
        recyclerViewTransacciones.setAdapter(transaccionAdapter);

        cargarTransacciones();

        return view;
    }

    /**
     * Carga las transacciones desde la API y las filtra por estado.
     */
    private void cargarTransacciones() {
        apiService.getTransacciones().enqueue(new Callback<List<Transaccion>>() {
            @Override
            public void onResponse(Call<List<Transaccion>> call, Response<List<Transaccion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Transaccion> transaccionesFiltradas = new ArrayList<>();
                    for (Transaccion transaccion : response.body()) {
                        if (transaccion.getEstado().equals(estado)) {
                            transaccionesFiltradas.add(transaccion);
                        }
                    }
                    transaccionAdapter.updateTransacciones(transaccionesFiltradas);
                } else {
                    Toast.makeText(getActivity(), "Error al cargar transacciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Transaccion>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error al cargar transacciones", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
