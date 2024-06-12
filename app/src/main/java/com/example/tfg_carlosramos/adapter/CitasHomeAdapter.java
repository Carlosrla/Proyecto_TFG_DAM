package com.example.tfg_carlosramos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tfg_carlosramos.Model.DateAdapter;
import com.example.tfg_carlosramos.Model.Cita;
import com.example.tfg_carlosramos.Model.Cliente;
import com.example.tfg_carlosramos.R;
import java.util.List;
import java.util.Map;

/**
 * Adaptador para mostrar una lista de citas en la pantalla de inicio.
 */
public class CitasHomeAdapter extends RecyclerView.Adapter<CitasHomeAdapter.CitaViewHolder> {

    private List<Cita> citasList;
    private Map<Integer, Cliente> clienteCache;

    /**
     * Constructor del adaptador.
     *
     * @param citasList    Lista de citas a mostrar.
     * @param clienteCache Caché de clientes para obtener sus datos.
     */
    public CitasHomeAdapter(List<Cita> citasList, Map<Integer, Cliente> clienteCache) {
        this.citasList = citasList;
        this.clienteCache = clienteCache;
    }

    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cita_home, parent, false);
        return new CitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
        Cita cita = citasList.get(position);
        holder.bind(cita, clienteCache);
    }

    @Override
    public int getItemCount() {
        return citasList.size();
    }

    /**
     * Actualiza la lista de citas en el adaptador.
     *
     * @param citas Nueva lista de citas.
     */
    public void updateCitas(List<Cita> citas) {
        this.citasList = citas;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder para la vista de cada cita en el RecyclerView.
     */
    static class CitaViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewFecha;
        private TextView textViewHora;
        private TextView textViewCliente;
        private TextView textViewDescripcion;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView La vista de cada elemento.
         */
        public CitaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
            textViewHora = itemView.findViewById(R.id.textViewHora);
            textViewCliente = itemView.findViewById(R.id.textViewCliente);
            textViewDescripcion = itemView.findViewById(R.id.textViewDescripcion);
        }

        /**
         * Vincula los datos de una cita a las vistas.
         *
         * @param cita         La cita cuyos datos se van a mostrar.
         * @param clienteCache Caché de clientes para obtener sus datos.
         */
        public void bind(Cita cita, Map<Integer, Cliente> clienteCache) {
            textViewFecha.setText("Fecha: " + DateAdapter.format(cita.getFecha()));
            textViewHora.setText("Hora: " + cita.getHora());
            Cliente cliente = clienteCache.get(cita.getClienteId());
            if (cliente != null) {
                textViewCliente.setText("Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
            } else {
                textViewCliente.setText("Cliente ID: " + cita.getClienteId());
            }
            textViewDescripcion.setText("Descripción: " + cita.getDescripcion());
        }
    }
}
