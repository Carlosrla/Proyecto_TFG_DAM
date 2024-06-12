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
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import com.example.tfg_carlosramos.Model.DialogoAgregarNota;
import com.example.tfg_carlosramos.Model.DialogoSeleccionarCliente;
import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.adapter.ClienteNotasAdapter;
import com.example.tfg_carlosramos.Model.Cliente;
import com.example.tfg_carlosramos.network.ApiService;
import com.example.tfg_carlosramos.network.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotasFragment extends Fragment implements DialogoAgregarNota.NotaAgregadaListener {

    private static final String TAG = "NotasFragment";

    private RecyclerView recyclerView;
    private ClienteNotasAdapter clientesNotasAdapter;
    private FloatingActionButton fabAddNota;
    private ApiService apiService;
    private List<Cliente> listaClientes = new ArrayList<>();
    private SearchView searchViewClientes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notas, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewClientesConNotas);
        fabAddNota = view.findViewById(R.id.fabAddNota);
        searchViewClientes = view.findViewById(R.id.searchViewClientes);

        apiService = RetrofitClient.getApiService();

        initializeRecyclerView();
        loadClientesConNotas();

        fabAddNota.setOnClickListener(v -> mostrarDialogSeleccionarClienteSinNotas());

        setSearchFilter();

        return view;
    }

    /**
     * Inicializa el RecyclerView con un LayoutManager y un adaptador vacío.
     */
    private void initializeRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        clientesNotasAdapter = new ClienteNotasAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(clientesNotasAdapter);
    }

    /**
     * Carga la lista de clientes con notas desde la API y la actualiza en el adaptador.
     */
    private void loadClientesConNotas() {
        Log.d(TAG, "Cargando clientes con notas desde la API");
        apiService.getClientesConNotas().enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(@NonNull Call<List<Cliente>> call, @NonNull Response<List<Cliente>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Clientes con notas recibidos: " + response.body().size());
                    listaClientes = response.body();
                    clientesNotasAdapter.updateClientes(listaClientes);
                } else {
                    Log.e(TAG, "Error en la respuesta de clientes con notas: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Cliente>> call, @NonNull Throwable t) {
                Log.e(TAG, "Fallo en la llamada para clientes con notas: " + t.getMessage());
            }
        });
    }

    /**
     * Muestra un diálogo para seleccionar un cliente sin notas y luego agregar una nota a ese cliente.
     */
    private void mostrarDialogSeleccionarClienteSinNotas() {
        Log.d(TAG, "Cargando clientes sin notas desde la API");
        apiService.getClientesSinNotas().enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(@NonNull Call<List<Cliente>> call, @NonNull Response<List<Cliente>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Clientes sin notas recibidos: " + response.body().size());
                    List<Cliente> clientesSinNotas = response.body();
                    if (clientesSinNotas.isEmpty()) {
                        Toast.makeText(getContext(), "Todos los clientes tienen notas", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DialogoSeleccionarCliente dialogoSeleccionarCliente = new DialogoSeleccionarCliente(clientesSinNotas, cliente -> {
                        DialogoAgregarNota dialogoAgregarNota = new DialogoAgregarNota(cliente.getId(), NotasFragment.this);
                        dialogoAgregarNota.show(getParentFragmentManager(), "DialogoAgregarNota");
                    });
                    dialogoSeleccionarCliente.show(getParentFragmentManager(), "DialogoSeleccionarCliente");
                } else {
                    Log.e(TAG, "Error en la respuesta de clientes sin notas: " + response.code());
                    Toast.makeText(getContext(), "Error al obtener la lista de clientes sin notas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Cliente>> call, @NonNull Throwable t) {
                Log.e(TAG, "Fallo en la llamada para clientes sin notas: " + t.getMessage());
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Configura el filtro de búsqueda para el SearchView.
     */
    private void setSearchFilter() {
        searchViewClientes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarClientes(newText);
                return true;
            }
        });
    }

    /**
     * Filtra la lista de clientes basada en el texto ingresado y actualiza el adaptador.
     *
     * @param texto El texto para filtrar la lista de clientes.
     */
    private void filtrarClientes(String texto) {
        List<Cliente> clientesFiltrados = new ArrayList<>();
        for (Cliente cliente : listaClientes) {
            if (cliente.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                    cliente.getApellido().toLowerCase().contains(texto.toLowerCase())) {
                clientesFiltrados.add(cliente);
            }
        }
        clientesNotasAdapter.updateClientes(clientesFiltrados);
    }

    /**
     * Método llamado cuando se agrega una nota, recarga la lista de clientes con notas.
     */
    @Override
    public void onNotaAgregada() {
        // Recargar la lista de clientes con notas
        loadClientesConNotas();
    }
}
