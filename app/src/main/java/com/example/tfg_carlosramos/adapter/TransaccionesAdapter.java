package com.example.tfg_carlosramos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tfg_carlosramos.Model.Transaccion;
import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.network.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Adaptador para manejar las transacciones en un RecyclerView.
 */
public class TransaccionesAdapter extends RecyclerView.Adapter<TransaccionesAdapter.TransaccionViewHolder> {

    private List<Transaccion> listaTransacciones;
    private ApiService apiService;
    private Context context;
    private TransaccionCompletaListener listener;

    /**
     * Interface para manejar la finalización de una transacción.
     */
    public interface TransaccionCompletaListener {
        void onTransaccionCompleta();
    }

    /**
     * Constructor del adaptador.
     *
     * @param listaTransacciones Lista de transacciones.
     * @param apiService         Servicio API para realizar las operaciones de red.
     * @param context            Contexto de la aplicación.
     */
    public TransaccionesAdapter(List<Transaccion> listaTransacciones, ApiService apiService, Context context) {
        this.listaTransacciones = listaTransacciones;
        this.apiService = apiService;
        this.context = context;
    }

    @NonNull
    @Override
    public TransaccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transacciones_item, parent, false);
        return new TransaccionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaccionViewHolder holder, int position) {
        Transaccion transaccion = listaTransacciones.get(position);
        holder.textViewCliente.setText("Cliente: " + transaccion.getIdCliente());
        holder.textViewPropiedad.setText("Propiedad: " + transaccion.getIdPropiedad());
        holder.textViewPrecio.setText("Precio: " + transaccion.getPrecio());
        holder.textViewFormaPago.setText("Forma de Pago: " + transaccion.getFormaPago());
        holder.textViewEstado.setText("Estado: " + transaccion.getEstado());
        holder.textViewNotas.setText("Notas: " + transaccion.getNotas());

        holder.buttonCompletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completarTransaccion(transaccion, holder.getAdapterPosition());
            }
        });

        holder.buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarTransaccion(transaccion, holder.getAdapterPosition());
            }
        });

        if (!transaccion.getEstado().equals("Pendiente")) {
            holder.buttonCompletar.setVisibility(View.GONE);
            holder.buttonCancelar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listaTransacciones.size();
    }

    /**
     * Actualiza la lista de transacciones.
     *
     * @param nuevasTransacciones Nuevas transacciones a mostrar.
     */
    public void updateTransacciones(List<Transaccion> nuevasTransacciones) {
        listaTransacciones = nuevasTransacciones;
        notifyDataSetChanged();
    }

    /**
     * Marca una transacción como completada.
     *
     * @param transaccion Transacción a completar.
     * @param position    Posición de la transacción en la lista.
     */
    private void completarTransaccion(Transaccion transaccion, int position) {
        transaccion.setEstado("Completada");
        apiService.actualizarTransaccion(transaccion.getIdTransaccion(), transaccion).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    notifyItemChanged(position);
                    listener.onTransaccionCompleta();
                    Toast.makeText(context, "Transacción completada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al completar la transacción", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error al completar la transacción", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Marca una transacción como cancelada.
     *
     * @param transaccion Transacción a cancelar.
     * @param position    Posición de la transacción en la lista.
     */
    private void cancelarTransaccion(Transaccion transaccion, int position) {
        transaccion.setEstado("Cancelada");
        apiService.actualizarTransaccion(transaccion.getIdTransaccion(), transaccion).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    notifyItemChanged(position);
                    Toast.makeText(context, "Transacción cancelada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al cancelar la transacción", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error al cancelar la transacción", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Clase ViewHolder para las transacciones.
     */
    static class TransaccionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCliente;
        TextView textViewPropiedad;
        TextView textViewPrecio;
        TextView textViewFormaPago;
        TextView textViewEstado;
        TextView textViewNotas;
        Button buttonCompletar;
        Button buttonCancelar;

        public TransaccionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCliente = itemView.findViewById(R.id.textViewCliente);
            textViewPropiedad = itemView.findViewById(R.id.textViewPropiedad);
            textViewPrecio = itemView.findViewById(R.id.textViewPrecio);
            textViewFormaPago = itemView.findViewById(R.id.textViewFormaPago);
            textViewEstado = itemView.findViewById(R.id.textViewEstado);
            textViewNotas = itemView.findViewById(R.id.textViewNotas);
            buttonCompletar = itemView.findViewById(R.id.buttonCompletar);
            buttonCancelar = itemView.findViewById(R.id.buttonCancelar);
        }
    }
}
