package com.example.tfg_carlosramos.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tfg_carlosramos.Model.Cliente;
import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.adapter.ClientesAdapter;
import com.example.tfg_carlosramos.network.ApiService;
import com.example.tfg_carlosramos.network.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClienteFragment extends Fragment implements ModificarClienteListener {

    private RecyclerView recyclerView;
    private ClientesAdapter clientesAdapter;
    private FloatingActionButton fabAddCliente;
    private ApiService apiService;
    private List<Cliente> listaClientes = new ArrayList<>();
    private SearchView searchViewClientes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cliente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewClientes);
        fabAddCliente = view.findViewById(R.id.fabAddCliente);
        searchViewClientes = view.findViewById(R.id.searchViewClientes);

        // Obtener la instancia de ApiService utilizando RetrofitClient
        apiService = RetrofitClient.getApiService();

        // Inicializar RecyclerView
        initializeRecyclerView();

        // Cargar clientes desde el servidor
        loadClientes();

        // Configuración del FAB para agregar nuevos clientes
        fabAddCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogAgregarCliente();
            }
        });

        // Configuración del SearchView para filtrar clientes
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
     * Inicializa el RecyclerView con un LayoutManager y un adaptador vacío.
     */
    private void initializeRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        clientesAdapter = new ClientesAdapter(new ArrayList<>(), apiService, this, getContext());
        recyclerView.setAdapter(clientesAdapter);
    }

    /**
     * Carga la lista de clientes desde la API y la actualiza en el adaptador.
     */
    private void loadClientes() {
        Log.d("ClienteFragment", "Cargando clientes desde la API");
        apiService.getClientes().enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ClienteFragment", "Clientes recibidos: " + response.body().size());
                    listaClientes = response.body();
                    clientesAdapter.updateClientes(listaClientes);
                } else {
                    Log.e("ClienteFragment", "Error en la respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Log.e("ClienteFragment", "Fallo en la llamada: " + t.getMessage());
            }
        });
    }

    /**
     * Muestra un diálogo para agregar un nuevo cliente y maneja la lógica de validación y creación del cliente.
     */
    private void mostrarDialogAgregarCliente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_agregar_cliente, null);
        builder.setView(dialogView)
                .setTitle("Agregar Cliente")
                .setPositiveButton("Agregar", (dialog, id) -> {
                    Log.d("ClienteFragment", "Botón Agregar clickeado");

                    // Obtener los datos del nuevo cliente del dialog
                    EditText editTextNombre = dialogView.findViewById(R.id.editTextNombre);
                    EditText editTextApellido = dialogView.findViewById(R.id.editTextApellido);
                    EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);
                    EditText editTextTelefono = dialogView.findViewById(R.id.editTextTelefono);

                    String nombre = editTextNombre.getText().toString();
                    String apellido = editTextApellido.getText().toString();
                    String email = editTextEmail.getText().toString();
                    String telefono = editTextTelefono.getText().toString();

                    // Validar que los campos no estén vacíos
                    if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty()) {
                        Log.e("ClienteFragment", "Uno o más campos están vacíos");
                        Toast.makeText(getActivity(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Crear un nuevo cliente con los datos ingresados
                    Cliente nuevoCliente = new Cliente(nombre, apellido, email, telefono);
                    Log.d("ClienteFragment", "Datos del nuevo cliente: " + nombre + ", " + apellido + ", " + email + ", " + telefono);
                    agregarCliente(nuevoCliente);
                })
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Muestra un diálogo para modificar un cliente existente y maneja la lógica de validación y actualización del cliente.
     *
     * @param cliente El cliente a modificar.
     */
    private void mostrarDialogModificarCliente(Cliente cliente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_agregar_cliente, null);
        builder.setView(dialogView)
                .setTitle("Modificar Cliente")
                .setPositiveButton("Modificar", (dialog, id) -> {
                    Log.d("ClienteFragment", "Botón Modificar clickeado");

                    // Obtener los datos del cliente del dialog
                    EditText editTextNombre = dialogView.findViewById(R.id.editTextNombre);
                    EditText editTextApellido = dialogView.findViewById(R.id.editTextApellido);
                    EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);
                    EditText editTextTelefono = dialogView.findViewById(R.id.editTextTelefono);

                    cliente.setNombre(editTextNombre.getText().toString());
                    cliente.setApellido(editTextApellido.getText().toString());
                    cliente.setEmail(editTextEmail.getText().toString());
                    cliente.setTelefono(editTextTelefono.getText().toString());

                    // Validar que los campos no estén vacíos
                    if (cliente.getNombre().isEmpty() || cliente.getApellido().isEmpty() || cliente.getEmail().isEmpty() || cliente.getTelefono().isEmpty()) {
                        Log.e("ClienteFragment", "Uno o más campos están vacíos");
                        Toast.makeText(getActivity(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Modificar el cliente con los nuevos datos ingresados
                    modificarCliente(cliente);
                })
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

        // Rellenar los campos con los datos actuales del cliente
        EditText editTextNombre = dialogView.findViewById(R.id.editTextNombre);
        EditText editTextApellido = dialogView.findViewById(R.id.editTextApellido);
        EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);
        EditText editTextTelefono = dialogView.findViewById(R.id.editTextTelefono);

        editTextNombre.setText(cliente.getNombre());
        editTextApellido.setText(cliente.getApellido());
        editTextEmail.setText(cliente.getEmail());
        editTextTelefono.setText(cliente.getTelefono());
    }

    /**
     * Envía una solicitud a la API para agregar un nuevo cliente.
     *
     * @param cliente El cliente a agregar.
     */
    private void agregarCliente(Cliente cliente) {
        Log.d("ClienteFragment", "Agregando cliente: " + cliente.getNombre());
        apiService.agregarCliente(cliente).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("ClienteFragment", "Cliente agregado exitosamente");
                    // Cliente agregado exitosamente, recargar la lista de clientes
                    loadClientes();
                    Toast.makeText(getActivity(), "Cliente agregado", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("ClienteFragment", "Error al agregar cliente: " + response.code() + " - " + response.message());
                    Toast.makeText(getActivity(), "Error al agregar cliente: " + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ClienteFragment", "Fallo en la llamada: " + t.getMessage());
                Toast.makeText(getActivity(), "Fallo en la llamada: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Envía una solicitud a la API para modificar un cliente existente.
     *
     * @param cliente El cliente a modificar.
     */
    private void modificarCliente(Cliente cliente) {
        Log.d("ClienteFragment", "Modificando cliente: " + cliente.getNombre() + ", ID: " + cliente.getId());
        apiService.actualizarCliente(cliente.getId(), cliente).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("ClienteFragment", "Cliente modificado exitosamente");
                    // Cliente modificado exitosamente, recargar la lista de clientes
                    loadClientes();
                    Toast.makeText(getActivity(), "Cliente modificado", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("ClienteFragment", "Error al modificar cliente: " + response.code() + " - " + response.message());
                    Toast.makeText(getActivity(), "Error al modificar cliente: " + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ClienteFragment", "Fallo en la llamada: " + t.getMessage());
                Toast.makeText(getActivity(), "Fallo en la llamada: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    cliente.getEmail().toLowerCase().contains(texto.toLowerCase())) {
                clientesFiltrados.add(cliente);
            }
        }
        clientesAdapter.updateClientes(clientesFiltrados);
    }

    @Override
    public void onModificarCliente(Cliente cliente) {
        mostrarDialogModificarCliente(cliente);
    }

    @Override
    public void onEliminarCliente(Cliente cliente) {
        // Implementar si es necesario
    }
}
