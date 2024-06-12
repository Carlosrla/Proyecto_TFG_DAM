package com.example.tfg_carlosramos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.Model.Cita;
import com.example.tfg_carlosramos.Model.DialogoModificarCita;
import com.example.tfg_carlosramos.network.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Adaptador para mostrar una lista de objetos Cita en un RecyclerView.
 */
public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.CitaViewHolder> {

    private List<Cita> listaCitas;
    private ApiService apiService;
    private Context context;

    /**
     * Constructor del adaptador CitasAdapter.
     *
     * @param listaCitas Lista de objetos Cita a mostrar.
     * @param apiService Instancia de ApiService para realizar llamadas de red.
     * @param context    Contexto de la actividad o fragmento.
     */
    public CitasAdapter(List<Cita> listaCitas, ApiService apiService, Context context) {
        this.listaCitas = listaCitas;
        this.apiService = apiService;
        this.context = context;
    }

    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cita_item, parent, false);
        return new CitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
        Cita cita = listaCitas.get(position);
        holder.fechaTextView.setText(cita.getFechaParaMySQL());
        holder.horaTextView.setText(cita.getHora());
        holder.descripcionTextView.setText(cita.getDescripcion());

        // Configuración del botón para eliminar una cita
        holder.btnEliminarCita.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Estás seguro de que quieres eliminar esta cita?")
                        .setPositiveButton("Sí", (dialog, which) -> eliminarCita(cita, adapterPosition))
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        // Configuración del botón para modificar una cita
        holder.btnModificarCita.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                DialogoModificarCita dialogoModificarCita = new DialogoModificarCita(cita, () -> {
                    // Actualizar la lista de citas después de modificar una cita
                    notifyDataSetChanged();
                });
                dialogoModificarCita.show(((FragmentActivity) context).getSupportFragmentManager(), "DialogoModificarCita");
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCitas.size();
    }

    /**
     * Actualiza la lista de citas en el adaptador.
     *
     * @param nuevasCitas Nueva lista de citas a mostrar.
     */
    public void updateCitas(List<Cita> nuevasCitas) {
        listaCitas.clear();
        listaCitas.addAll(nuevasCitas);
        notifyDataSetChanged();
    }

    /**
     * Elimina una cita de la lista y actualiza la vista.
     *
     * @param cita            La cita a eliminar.
     * @param adapterPosition La posición de la cita en el adaptador.
     */
    private void eliminarCita(Cita cita, int adapterPosition) {
        apiService.eliminarCita(cita.getIdCita()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listaCitas.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    notifyItemRangeChanged(adapterPosition, listaCitas.size());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Manejo de errores
            }
        });
    }

    /**
     * ViewHolder para la vista de cada cita en el RecyclerView.
     */
    static class CitaViewHolder extends RecyclerView.ViewHolder {
        TextView fechaTextView;
        TextView horaTextView;
        TextView descripcionTextView;
        Button btnModificarCita;
        Button btnEliminarCita;
        CardView cardView;

        /**
         * Constructor del ViewHolder para las citas.
         *
         * @param itemView La vista de cada elemento.
         */
        public CitaViewHolder(View itemView) {
            super(itemView);
            fechaTextView = itemView.findViewById(R.id.textFechaCita);
            horaTextView = itemView.findViewById(R.id.textHoraCita);
            descripcionTextView = itemView.findViewById(R.id.textDescripcionCita);
            btnModificarCita = itemView.findViewById(R.id.btnModificarCita);
            btnEliminarCita = itemView.findViewById(R.id.btnEliminarCita);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}

