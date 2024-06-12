package com.example.tfg_carlosramos.Model;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.adapter.NotasAdapter;
import com.example.tfg_carlosramos.network.ApiService;
import com.example.tfg_carlosramos.network.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento que muestra las notas de un cliente específico.
 */
public class ClienteNotasTabFragment extends Fragment implements DialogoAgregarNota.NotaAgregadaListener {

    private static final String ARG_CLIENTE_ID = "cliente_id";
    private static final String ARG_CLIENTE_NOMBRE = "cliente_nombre";

    private int clienteId;
    private String clienteNombre;
    private RecyclerView recyclerView;
    private NotasAdapter notasAdapter;
    private ApiService apiService;
    private FloatingActionButton fabAddNotaCliente;

    /**
     * Crea una nueva instancia de ClienteNotasTabFragment con los argumentos proporcionados.
     * @param clienteId ID del cliente.
     * @param clienteNombre Nombre del cliente.
     * @return Una nueva instancia de ClienteNotasTabFragment.
     */
    public static ClienteNotasTabFragment newInstance(int clienteId, String clienteNombre) {
        ClienteNotasTabFragment fragment = new ClienteNotasTabFragment();
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
        View view = inflater.inflate(R.layout.fragment_cliente_notas_tab, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewNotas);
        fabAddNotaCliente = view.findViewById(R.id.fabAddNotaCliente);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notasAdapter = new NotasAdapter(new ArrayList<>(), apiService, getContext());
        recyclerView.setAdapter(notasAdapter);

        cargarNotasDelCliente();

        fabAddNotaCliente.setOnClickListener(v -> {
            DialogoAgregarNota dialogoAgregarNota = new DialogoAgregarNota(clienteId, this);
            dialogoAgregarNota.show(getParentFragmentManager(), "DialogoAgregarNota");
        });

        return view;
    }

    /**
     * Carga las notas del cliente desde el servidor.
     */
    private void cargarNotasDelCliente() {
        apiService.getNotasDelCliente(clienteId).enqueue(new Callback<List<Nota>>() {
            @Override
            public void onResponse(Call<List<Nota>> call, Response<List<Nota>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    notasAdapter.updateNotas(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Nota>> call, Throwable t) {
                // Manejo de errores
            }
        });
    }

    @Override
    public void onNotaAgregada() {
        cargarNotasDelCliente();
    }
}
