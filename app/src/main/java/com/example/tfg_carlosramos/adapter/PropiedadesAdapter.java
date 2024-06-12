package com.example.tfg_carlosramos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.Model.Propiedad;
import com.example.tfg_carlosramos.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Adaptador para manejar la visualización y las acciones en la lista de propiedades.
 */
public class PropiedadesAdapter extends RecyclerView.Adapter<PropiedadesAdapter.PropiedadViewHolder> {
    private List<Propiedad> listaPropiedades;
    private ApiService apiService;
    private Context context;

    /**
     * Constructor del adaptador.
     *
     * @param listaPropiedades Lista de propiedades.
     * @param apiService       Servicio de API para interactuar con el backend.
     * @param context          Contexto de la actividad o fragmento.
     */
    public PropiedadesAdapter(List<Propiedad> listaPropiedades, ApiService apiService, Context context) {
        this.listaPropiedades = listaPropiedades;
        this.apiService = apiService;
        this.context = context;
    }

    @NonNull
    @Override
    public PropiedadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.propiedad_item, parent, false);
        return new PropiedadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropiedadViewHolder holder, int position) {
        Propiedad propiedad = listaPropiedades.get(position);
        holder.tituloTextView.setText(propiedad.getTitulo());
        holder.descripcionTextView.setText(propiedad.getDescripcion());
        holder.ubicacionTextView.setText(propiedad.getUbicacion());
        holder.precioTextView.setText(String.valueOf(propiedad.getPrecio()));
        holder.estadoTextView.setText(propiedad.getEstado());

        // Cambia el color de la tarjeta según el estado de la propiedad
        switch (propiedad.getEstado()) {
            case "Disponible":
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
                break;
            case "Reservada":
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));
                break;
            case "Vendida":
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));
                break;
        }

        // Maneja la eliminación de la propiedad
        holder.buttonEliminar.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Estás seguro de que quieres eliminar esta propiedad?")
                        .setPositiveButton("Sí", (dialog, which) -> eliminarPropiedad(propiedad.getIdPropiedad(), adapterPosition))
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        // Maneja la modificación de la propiedad
        holder.buttonModificar.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                mostrarDialogModificarPropiedad(propiedad);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPropiedades.size();
    }

    /**
     * Actualiza la lista de propiedades.
     *
     * @param nuevasPropiedades Nueva lista de propiedades.
     */
    public void updatePropiedades(List<Propiedad> nuevasPropiedades) {
        listaPropiedades.clear();
        listaPropiedades.addAll(nuevasPropiedades);
        notifyDataSetChanged();
    }

    /**
     * Elimina una propiedad de la lista y del backend.
     *
     * @param idPropiedad ID de la propiedad a eliminar.
     * @param position    Posición de la propiedad en el adaptador.
     */
    private void eliminarPropiedad(int idPropiedad, int position) {
        apiService.eliminarPropiedad(idPropiedad).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listaPropiedades.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, listaPropiedades.size());
                    Toast.makeText(context, "Propiedad eliminada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al eliminar la propiedad", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Fallo en la eliminación de la propiedad", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Muestra un diálogo para modificar una propiedad.
     *
     * @param propiedad Propiedad a modificar.
     */
    private void mostrarDialogModificarPropiedad(Propiedad propiedad) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_agregar_propiedad, null);
        builder.setView(dialogView)
                .setTitle("Modificar Propiedad")
                .setPositiveButton("Modificar", (dialog, id) -> {
                    EditText editTextTitulo = dialogView.findViewById(R.id.editTextTitulo);
                    EditText editTextDescripcion = dialogView.findViewById(R.id.editTextDescripcion);
                    EditText editTextUbicacion = dialogView.findViewById(R.id.editTextUbicacion);
                    EditText editTextPrecio = dialogView.findViewById(R.id.editTextPrecio);
                    Spinner spinnerEstado = dialogView.findViewById(R.id.spinnerEstado);

                    String titulo = editTextTitulo.getText().toString();
                    String descripcion = editTextDescripcion.getText().toString();
                    String ubicacion = editTextUbicacion.getText().toString();
                    String precio = editTextPrecio.getText().toString();
                    String estado = spinnerEstado.getSelectedItem().toString();

                    // Validar que los campos no estén vacíos
                    if (titulo.isEmpty() || descripcion.isEmpty() || ubicacion.isEmpty() || precio.isEmpty()) {
                        Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        double precioDouble = Double.parseDouble(precio);
                        propiedad.setPrecio(precioDouble);
                        propiedad.setEstado(estado);
                        propiedad.setTitulo(titulo);
                        propiedad.setDescripcion(descripcion);
                        propiedad.setUbicacion(ubicacion);

                        modificarPropiedad(propiedad);
                    } catch (NumberFormatException e) {
                        Toast.makeText(context, "Por favor, ingresa un precio válido", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        // Rellenar los campos con los datos actuales de la propiedad
        EditText editTextTitulo = dialogView.findViewById(R.id.editTextTitulo);
        EditText editTextDescripcion = dialogView.findViewById(R.id.editTextDescripcion);
        EditText editTextUbicacion = dialogView.findViewById(R.id.editTextUbicacion);
        EditText editTextPrecio = dialogView.findViewById(R.id.editTextPrecio);
        Spinner spinnerEstado = dialogView.findViewById(R.id.spinnerEstado);

        editTextTitulo.setText(propiedad.getTitulo());
        editTextDescripcion.setText(propiedad.getDescripcion());
        editTextUbicacion.setText(propiedad.getUbicacion());
        editTextPrecio.setText(String.valueOf(propiedad.getPrecio()));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.estados_propiedad, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);
        if (propiedad.getEstado() != null) {
            int spinnerPosition = adapter.getPosition(propiedad.getEstado());
            spinnerEstado.setSelection(spinnerPosition);
        }
    }
    /**
     * Actualiza una propiedad en el backend.
     *
     * @param propiedad Propiedad a actualizar.
     */
    private void modificarPropiedad(Propiedad propiedad) {
        apiService.actualizarPropiedad(propiedad.getIdPropiedad(), propiedad).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Actualizar la lista de propiedades
                    int index = listaPropiedades.indexOf(propiedad);
                    listaPropiedades.set(index, propiedad);
                    notifyItemChanged(index);
                    Toast.makeText(context, "Propiedad modificada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al modificar la propiedad", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Fallo en la modificación de la propiedad", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class PropiedadViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTextView;
        TextView descripcionTextView;
        TextView ubicacionTextView;
        TextView precioTextView;
        TextView estadoTextView;
        Button buttonModificar;
        Button buttonEliminar;
        CardView cardView;

        public PropiedadViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewPropiedad);
            tituloTextView = itemView.findViewById(R.id.textViewTitulo);
            descripcionTextView = itemView.findViewById(R.id.textViewDescripcion);
            ubicacionTextView = itemView.findViewById(R.id.textViewUbicacion);
            precioTextView = itemView.findViewById(R.id.textViewPrecio);
            estadoTextView = itemView.findViewById(R.id.textViewEstado);
            buttonModificar = itemView.findViewById(R.id.buttonModificar);
            buttonEliminar = itemView.findViewById(R.id.buttonEliminar);
        }
    }
}
