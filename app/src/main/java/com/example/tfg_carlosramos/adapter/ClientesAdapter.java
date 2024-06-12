package com.example.tfg_carlosramos.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_carlosramos.Model.Cliente;
import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.fragments.ModificarClienteListener;
import com.example.tfg_carlosramos.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Adaptador para manejar la visualización y las acciones en la lista de clientes.
 */
public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ClienteViewHolder> {
    private List<Cliente> listaClientes;
    private ApiService apiService;
    private ModificarClienteListener listener;
    private Context context;

    /**
     * Constructor del adaptador.
     *
     * @param listaClientes Lista de clientes.
     * @param apiService    Servicio de API para interactuar con el backend.
     * @param listener      Listener para manejar la modificación del cliente.
     * @param context       Contexto de la actividad o fragmento.
     */
    public ClientesAdapter(List<Cliente> listaClientes, ApiService apiService, ModificarClienteListener listener, Context context) {
        this.listaClientes = listaClientes;
        this.apiService = apiService;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cliente_item, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = listaClientes.get(position);
        holder.idTextView.setText(String.valueOf(cliente.getId()));
        holder.nombreTextView.setText(cliente.getNombre() + " " + cliente.getApellido());
        holder.emailTextView.setText(cliente.getEmail());
        holder.telefonoTextView.setText(cliente.getTelefono());

        // Maneja la eliminación del cliente
        holder.btnEliminarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    new AlertDialog.Builder(context)
                            .setTitle("Confirmar eliminación")
                            .setMessage("¿Estás seguro de que quieres eliminar este cliente?")
                            .setPositiveButton("Sí", (dialog, which) -> eliminarCliente(cliente, adapterPosition))
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });

        // Maneja la modificación del cliente
        holder.btnModificarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onModificarCliente(cliente);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    /**
     * Actualiza la lista de clientes.
     *
     * @param nuevosClientes Nueva lista de clientes.
     */
    public void updateClientes(List<Cliente> nuevosClientes) {
        listaClientes.clear();
        listaClientes.addAll(nuevosClientes);
        notifyDataSetChanged();
    }

    /**
     * Elimina un cliente de la lista y del backend.
     *
     * @param cliente          Cliente a eliminar.
     * @param adapterPosition Posición del cliente en el adaptador.
     */
    private void eliminarCliente(Cliente cliente, int adapterPosition) {
        Log.d("ClientesAdapter", "Eliminando cliente ID: " + cliente.getId());
        apiService.eliminarCliente(cliente.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("ClientesAdapter", "Cliente eliminado: " + cliente.getId());
                    listaClientes.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    notifyItemRangeChanged(adapterPosition, listaClientes.size());
                    Toast.makeText(context, "Cliente eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("ClientesAdapter", "Error al eliminar cliente: " + response.code() + " - " + response.message());
                    Toast.makeText(context, "Error al eliminar cliente: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ClientesAdapter", "Fallo en la llamada: " + t.getMessage());
                Toast.makeText(context, "Fallo en la llamada: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * ViewHolder para la vista de cada cliente en el RecyclerView.
     */
    static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView;
        TextView nombreTextView;
        TextView emailTextView;
        TextView telefonoTextView;
        Button btnModificarCliente;
        Button btnEliminarCliente;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView La vista de cada elemento.
         */
        public ClienteViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.textIdCliente);
            nombreTextView = itemView.findViewById(R.id.textNombreCliente);
            emailTextView = itemView.findViewById(R.id.textEmailCliente);
            telefonoTextView = itemView.findViewById(R.id.textTelefonoCliente);
            btnModificarCliente = itemView.findViewById(R.id.btnModificarCliente);
            btnEliminarCliente = itemView.findViewById(R.id.btnEliminarCliente);
        }
    }
}
