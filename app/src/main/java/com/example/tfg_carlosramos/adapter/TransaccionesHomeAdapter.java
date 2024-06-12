package com.example.tfg_carlosramos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tfg_carlosramos.Model.Cliente;
import com.example.tfg_carlosramos.Model.Transaccion;
import com.example.tfg_carlosramos.R;
import java.util.HashMap;
import java.util.List;

/**
 * Adaptador para manejar las transacciones en la vista principal del home.
 */
public class TransaccionesHomeAdapter extends RecyclerView.Adapter<TransaccionesHomeAdapter.ViewHolder> {

    private List<Transaccion> transacciones;
    private HashMap<Integer, Cliente> clienteCache;

    /**
     * Constructor del adaptador.
     *
     * @param transacciones  Lista de transacciones.
     * @param clienteCache   Caché de clientes para obtener datos adicionales.
     */
    public TransaccionesHomeAdapter(List<Transaccion> transacciones, HashMap<Integer, Cliente> clienteCache) {
        this.transacciones = transacciones;
        this.clienteCache = clienteCache;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaccion_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaccion transaccion = transacciones.get(position);
        Cliente cliente = clienteCache.get(transaccion.getIdCliente());
        if (cliente != null) {
            holder.textViewCliente.setText("Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
            holder.textViewTelefono.setText("Teléfono: " + cliente.getTelefono());
        } else {
            holder.textViewCliente.setText("Cliente: ID Cliente " + transaccion.getIdCliente());
            holder.textViewTelefono.setText("Teléfono: no disponible");
        }
        holder.textViewEstado.setText("Estado: " + transaccion.getEstado());
    }

    @Override
    public int getItemCount() {
        return transacciones.size();
    }

    /**
     * Actualiza la lista de transacciones.
     *
     * @param nuevasTransacciones Nuevas transacciones a mostrar.
     */
    public void updateTransacciones(List<Transaccion> nuevasTransacciones) {
        this.transacciones = nuevasTransacciones;
        notifyDataSetChanged();
    }

    /**
     * Clase ViewHolder para las transacciones en el home.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCliente, textViewEstado, textViewTelefono;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCliente = itemView.findViewById(R.id.textViewCliente);
            textViewEstado = itemView.findViewById(R.id.textViewEstado);
            textViewTelefono = itemView.findViewById(R.id.textViewTelefono);
        }
    }
}



