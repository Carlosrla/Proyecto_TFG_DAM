package com.example.tfg_carlosramos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.Model.Nota;
import com.example.tfg_carlosramos.network.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Adaptador para manejar la visualización y las acciones en la lista de notas.
 */
public class NotasAdapter extends RecyclerView.Adapter<NotasAdapter.NotaViewHolder> {
    private List<Nota> listaNotas;
    private ApiService apiService;
    private Context context;

    /**
     * Constructor del adaptador.
     *
     * @param listaNotas Lista de notas.
     * @param apiService Servicio de API para interactuar con el backend.
     * @param context    Contexto de la actividad o fragmento.
     */
    public NotasAdapter(List<Nota> listaNotas, ApiService apiService, Context context) {
        this.listaNotas = listaNotas;
        this.apiService = apiService;
        this.context = context;
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nota_item, parent, false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        Nota nota = listaNotas.get(position);
        holder.contenidoTextView.setText(nota.getContenido());
        holder.fechaCreacionTextView.setText(nota.getFechaCreacion());
        holder.fechaModificacionTextView.setText(nota.getFechaModificacion());

        // Maneja la eliminación de la nota
        holder.btnEliminarNota.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Estás seguro de que quieres eliminar esta nota?")
                        .setPositiveButton("Sí", (dialog, which) -> eliminarNota(nota, adapterPosition))
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        // Maneja la modificación de la nota
        holder.btnModificarNota.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                mostrarDialogModificarNota(nota);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    /**
     * Actualiza la lista de notas.
     *
     * @param nuevasNotas Nueva lista de notas.
     */
    public void updateNotas(List<Nota> nuevasNotas) {
        listaNotas.clear();
        listaNotas.addAll(nuevasNotas);
        notifyDataSetChanged();
    }

    /**
     * Elimina una nota de la lista y del backend.
     *
     * @param nota            Nota a eliminar.
     * @param adapterPosition Posición de la nota en el adaptador.
     */
    private void eliminarNota(Nota nota, int adapterPosition) {
        apiService.eliminarNota(nota.getIdNota()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listaNotas.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    notifyItemRangeChanged(adapterPosition, listaNotas.size());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Manejo de errores
            }
        });
    }

    /**
     * Muestra un diálogo para modificar una nota.
     *
     * @param nota Nota a modificar.
     */
    private void mostrarDialogModificarNota(Nota nota) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_modificar_nota, null);
        builder.setView(dialogView)
                .setTitle("Modificar Nota")
                .setPositiveButton("Modificar", (dialog, id) -> {
                    TextView editTextContenido = dialogView.findViewById(R.id.editTextContenido);
                    nota.setContenido(editTextContenido.getText().toString());
                    actualizarNota(nota);
                })
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

        // Rellenar el campo con los datos actuales de la nota
        TextView editTextContenido = dialogView.findViewById(R.id.editTextContenido);
        editTextContenido.setText(nota.getContenido());
    }

    /**
     * Actualiza una nota en el backend.
     *
     * @param nota Nota a actualizar.
     */
    private void actualizarNota(Nota nota) {
        apiService.actualizarNota(nota.getIdNota(), nota).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Manejo de errores
            }
        });
    }

    /**
     * ViewHolder para la vista de cada nota en el RecyclerView.
     */
    static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView contenidoTextView;
        TextView fechaCreacionTextView;
        TextView fechaModificacionTextView;
        Button btnModificarNota;
        Button btnEliminarNota;
        CardView cardView;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView La vista de cada elemento.
         */
        public NotaViewHolder(View itemView) {
            super(itemView);
            contenidoTextView = itemView.findViewById(R.id.textContenidoNota);
            fechaCreacionTextView = itemView.findViewById(R.id.textFechaCreacionNota);
            fechaModificacionTextView = itemView.findViewById(R.id.textFechaModificacionNota);
            btnModificarNota = itemView.findViewById(R.id.btnModificarNota);
            btnEliminarNota = itemView.findViewById(R.id.btnEliminarNota);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}

