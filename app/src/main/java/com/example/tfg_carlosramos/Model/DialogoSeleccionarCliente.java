package com.example.tfg_carlosramos.Model;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.Model.Cliente;
import java.util.List;

/**
 * Dialogo para seleccionar un cliente de una lista.
 */
public class DialogoSeleccionarCliente extends DialogFragment {

    private List<Cliente> clientesSinNotas;
    private ClienteSeleccionadoListener clienteSeleccionadoListener;

    /**
     * Constructor del diálogo para seleccionar un cliente.
     *
     * @param clientesSinNotas Lista de clientes sin notas.
     * @param clienteSeleccionadoListener Listener para manejar el cliente seleccionado.
     */
    public DialogoSeleccionarCliente(List<Cliente> clientesSinNotas, ClienteSeleccionadoListener clienteSeleccionadoListener) {
        this.clientesSinNotas = clientesSinNotas;
        this.clienteSeleccionadoListener = clienteSeleccionadoListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_seleccionar_cliente, null);

        ListView listViewClientes = dialogView.findViewById(R.id.listViewClientesSinNotas);
        ArrayAdapter<Cliente> adapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_list_item_1, clientesSinNotas);
        listViewClientes.setAdapter(adapter);

        listViewClientes.setOnItemClickListener((parent, view, position, id) -> {
            Cliente clienteSeleccionado = clientesSinNotas.get(position);
            clienteSeleccionadoListener.onClienteSeleccionado(clienteSeleccionado);
            dismiss();
        });

        builder.setView(dialogView);
        return builder.create();
    }

    /**
     * Interfaz para manejar el cliente seleccionado.
     */
    public interface ClienteSeleccionadoListener {
        void onClienteSeleccionado(Cliente cliente);
    }
}
