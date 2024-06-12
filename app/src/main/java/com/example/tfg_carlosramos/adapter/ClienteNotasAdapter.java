package com.example.tfg_carlosramos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;
import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.Model.Cliente;
import com.example.tfg_carlosramos.fragments.ClienteNotasFragment;
import java.util.List;

/**
 * Adaptador para mostrar una lista de clientes en la pantalla de notas.
 */
public class ClienteNotasAdapter extends RecyclerView.Adapter<ClienteNotasAdapter.ClienteViewHolder> {
    private List<Cliente> listaClientes;
    private Context context;

    /**
     * Constructor del adaptador.
     *
     * @param listaClientes Lista de clientes a mostrar.
     * @param context       Contexto de la actividad o fragmento.
     */
    public ClienteNotasAdapter(List<Cliente> listaClientes, Context context) {
        this.listaClientes = listaClientes;
        this.context = context;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cliente_item_simple, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = listaClientes.get(position);
        holder.nombreApellidoTextView.setText(cliente.getNombre() + " " + cliente.getApellido());

        holder.cardView.setOnClickListener(v -> {
            ClienteNotasFragment fragment = ClienteNotasFragment.newInstance(cliente.getId(), cliente.getNombre() + " " + cliente.getApellido());
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    /**
     * Actualiza la lista de clientes en el adaptador.
     *
     * @param nuevosClientes Nueva lista de clientes.
     */
    public void updateClientes(List<Cliente> nuevosClientes) {
        listaClientes.clear();
        listaClientes.addAll(nuevosClientes);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder para la vista de cada cliente en el RecyclerView.
     */
    static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView nombreApellidoTextView;
        CardView cardView;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView La vista de cada elemento.
         */
        public ClienteViewHolder(View itemView) {
            super(itemView);
            nombreApellidoTextView = itemView.findViewById(R.id.textNombreApellidoCliente);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}

