package com.example.tfg_carlosramos.Model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.network.ApiService;
import com.example.tfg_carlosramos.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarTransaccionDialog extends DialogFragment {

    private Spinner spinnerCliente;
    private Spinner spinnerPropiedad;
    private EditText editTextPrecio;
    private Spinner spinnerFormaPago;
    private Spinner spinnerEstado;
    private EditText editTextNotas;
    private ApiService apiService;
    private List<Cliente> listaClientes;
    private List<Propiedad> listaPropiedades;

    public static RegistrarTransaccionDialog newInstance() {
        return new RegistrarTransaccionDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_registrar_transaccion, null);

        spinnerCliente = view.findViewById(R.id.spinnerCliente);
        spinnerPropiedad = view.findViewById(R.id.spinnerPropiedad);
        editTextPrecio = view.findViewById(R.id.editTextPrecio);
        spinnerFormaPago = view.findViewById(R.id.spinnerFormaPago);
        spinnerEstado = view.findViewById(R.id.spinnerEstado);
        editTextNotas = view.findViewById(R.id.editTextNotas);

        apiService = RetrofitClient.getApiService();

        cargarClientes();
        cargarPropiedadesDisponibles(); // Cambia esta línea para cargar solo propiedades disponibles
        cargarFormasDePago();
        cargarEstados();

        return new AlertDialog.Builder(requireActivity())
                .setView(view)
                .setTitle("Registrar Transacción")
                .setPositiveButton("Registrar", (dialog, which) -> registrarTransaccion())
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .create();
    }

    /**
     * Carga la lista de clientes desde la API y la asigna al Spinner correspondiente.
     */
    private void cargarClientes() {
        apiService.getClientes().enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaClientes = response.body();
                    List<String> nombresClientes = new ArrayList<>();
                    nombresClientes.add("Seleccionar Cliente");
                    for (Cliente cliente : listaClientes) {
                        nombresClientes.add(cliente.getNombre() + " " + cliente.getApellido());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresClientes);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCliente.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), "Error al cargar clientes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error al cargar clientes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Carga la lista de propiedades disponibles desde la API y la asigna al Spinner correspondiente.
     */
    private void cargarPropiedadesDisponibles() { // Método modificado
        apiService.getPropiedadesDisponibles().enqueue(new Callback<List<Propiedad>>() {
            @Override
            public void onResponse(Call<List<Propiedad>> call, Response<List<Propiedad>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPropiedades = response.body();
                    List<String> nombresPropiedades = new ArrayList<>();
                    nombresPropiedades.add("Seleccionar Propiedad");
                    for (Propiedad propiedad : listaPropiedades) {
                        nombresPropiedades.add(propiedad.getTitulo());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresPropiedades);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPropiedad.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), "Error al cargar propiedades", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Propiedad>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error al cargar propiedades", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Carga las opciones de forma de pago en el Spinner correspondiente.
     */
    private void cargarFormasDePago() {
        List<String> formasDePago = new ArrayList<>();
        formasDePago.add("Seleccionar Forma de Pago");
        formasDePago.add("Efectivo");
        formasDePago.add("Transferencia Bancaria");
        formasDePago.add("Tarjeta de Crédito");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, formasDePago);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFormaPago.setAdapter(adapter);
    }

    /**
     * Carga las opciones de estado en el Spinner correspondiente.
     */
    private void cargarEstados() {
        List<String> estados = new ArrayList<>();
        estados.add("Seleccionar Estado");
        estados.add("Pendiente");
        estados.add("Completada");
        estados.add("Cancelada");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);
    }

    /**
     * Registra una nueva transacción con los datos ingresados por el usuario.
     */
    private void registrarTransaccion() {
        if (spinnerCliente.getSelectedItemPosition() == 0 || spinnerPropiedad.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Seleccione un cliente y una propiedad", Toast.LENGTH_SHORT).show();
            return;
        }

        Cliente clienteSeleccionado = listaClientes.get(spinnerCliente.getSelectedItemPosition() - 1);
        Propiedad propiedadSeleccionada = listaPropiedades.get(spinnerPropiedad.getSelectedItemPosition() - 1);
        int idCliente = clienteSeleccionado.getId();
        int idPropiedad = propiedadSeleccionada.getIdPropiedad();
        double precio = Double.parseDouble(editTextPrecio.getText().toString());
        String formaPago = spinnerFormaPago.getSelectedItem().toString();
        String estado = spinnerEstado.getSelectedItem().toString();
        String notas = editTextNotas.getText().toString();

        Transaccion nuevaTransaccion = new Transaccion(idCliente, idPropiedad, precio, formaPago, estado, notas);

        apiService.agregarTransaccion(nuevaTransaccion).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Transacción guardada exitosamente", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Error al guardar la transacción", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Fallo en la llamada: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
