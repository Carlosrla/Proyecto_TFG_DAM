package com.example.tfg_carlosramos.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tfg_carlosramos.Model.Propiedad;
import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.adapter.PropiedadesAdapter;
import com.example.tfg_carlosramos.network.ApiService;
import com.example.tfg_carlosramos.network.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropiedadesFragment extends Fragment {

    private RecyclerView recyclerView;
    private PropiedadesAdapter propiedadesAdapter;
    private FloatingActionButton fabAddPropiedad;
    private FloatingActionButton fabFilter;
    private ApiService apiService;
    private List<Propiedad> listaPropiedades = new ArrayList<>();
    private SearchView searchViewPropiedades;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_propiedad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewPropiedades);
        fabAddPropiedad = view.findViewById(R.id.fabAddPropiedad);
        fabFilter = view.findViewById(R.id.fabFilter);
        searchViewPropiedades = view.findViewById(R.id.searchViewPropiedades);

        // Obtener la instancia de ApiService utilizando RetrofitClient
        apiService = RetrofitClient.getApiService();

        // Inicializar RecyclerView
        initializeRecyclerView();

        // Cargar propiedades desde el servidor
        loadPropiedades();

        // Configuración del FAB para agregar nuevas propiedades
        fabAddPropiedad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogAgregarPropiedad();
            }
        });

        // Configuración del FAB para filtrar propiedades
        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarBottomSheetFiltros();
            }
        });

        // Configuración del SearchView para filtrar propiedades
        searchViewPropiedades.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarPropiedades(newText);
                return true;
            }
        });
    }

    /**
     * Inicializa el RecyclerView con un LayoutManager y un adaptador vacío.
     */
    private void initializeRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        propiedadesAdapter = new PropiedadesAdapter(new ArrayList<>(), apiService, getContext());
        recyclerView.setAdapter(propiedadesAdapter);
    }

    /**
     * Carga la lista de propiedades desde la API y la actualiza en el adaptador.
     */
    private void loadPropiedades() {
        Log.d("PropiedadFragment", "Cargando propiedades desde la API");
        apiService.getPropiedades().enqueue(new Callback<List<Propiedad>>() {
            @Override
            public void onResponse(Call<List<Propiedad>> call, Response<List<Propiedad>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("PropiedadFragment", "Propiedades recibidas: " + response.body().size());
                    listaPropiedades = response.body();
                    propiedadesAdapter.updatePropiedades(listaPropiedades);
                } else {
                    Log.e("PropiedadFragment", "Error en la respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Propiedad>> call, Throwable t) {
                Log.e("PropiedadFragment", "Fallo en la llamada: " + t.getMessage());
            }
        });
    }

    /**
     * Muestra un diálogo para agregar una nueva propiedad y maneja la lógica de validación y creación de la propiedad.
     */
    private void mostrarDialogAgregarPropiedad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_agregar_propiedad, null);
        builder.setView(dialogView)
                .setTitle("Agregar Propiedad")
                .setPositiveButton("Agregar", (dialog, id) -> {
                    Log.d("PropiedadFragment", "Botón Agregar clickeado");

                    // Obtener los datos de la nueva propiedad del dialog
                    EditText editTextTitulo = dialogView.findViewById(R.id.editTextTitulo);
                    EditText editTextDescripcion = dialogView.findViewById(R.id.editTextDescripcion);
                    EditText editTextUbicacion = dialogView.findViewById(R.id.editTextUbicacion);
                    EditText editTextPrecio = dialogView.findViewById(R.id.editTextPrecio);

                    String titulo = editTextTitulo.getText().toString();
                    String descripcion = editTextDescripcion.getText().toString();
                    String ubicacion = editTextUbicacion.getText().toString();
                    String precio = editTextPrecio.getText().toString();

                    // Validar que los campos no estén vacíos
                    if (titulo.isEmpty() || descripcion.isEmpty() || ubicacion.isEmpty() || precio.isEmpty()) {
                        Log.e("PropiedadFragment", "Uno o más campos están vacíos");
                        Toast.makeText(getActivity(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Crear una nueva propiedad con los datos ingresados
                    Propiedad nuevaPropiedad = new Propiedad(titulo, descripcion, ubicacion, Double.parseDouble(precio), "Disponible");
                    Log.d("PropiedadFragment", "Datos de la nueva propiedad: " + titulo + ", " + descripcion + ", " + ubicacion + ", " + precio + ", Disponible");
                    agregarPropiedad(nuevaPropiedad);
                })
                .setNegativeButton("Cancelar", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Envía una solicitud a la API para agregar una nueva propiedad.
     *
     * @param propiedad La propiedad a agregar.
     */
    private void agregarPropiedad(Propiedad propiedad) {
        Log.d("PropiedadFragment", "Agregando propiedad: " + propiedad.getTitulo());
        apiService.agregarPropiedad(propiedad).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("PropiedadFragment", "Propiedad agregada exitosamente");
                    // Propiedad agregada exitosamente, recargar la lista de propiedades
                    loadPropiedades();
                    Toast.makeText(getActivity(), "Propiedad agregada", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("PropiedadFragment", "Error al agregar propiedad: " + response.code() + " - " + response.message());
                    Toast.makeText(getActivity(), "Error al agregar propiedad: " + response.code() + " - " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("PropiedadFragment", "Fallo en la llamada: " + t.getMessage());
                Toast.makeText(getActivity(), "Fallo en la llamada: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Muestra un bottom sheet para aplicar filtros a la lista de propiedades.
     */
    private void mostrarBottomSheetFiltros() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_filtros, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        EditText editTextPrecioMin = bottomSheetView.findViewById(R.id.editTextPrecioMin);
        EditText editTextPrecioMax = bottomSheetView.findViewById(R.id.editTextPrecioMax);
        EditText editTextUbicacion = bottomSheetView.findViewById(R.id.editTextUbicacion);
        Button buttonAplicarFiltros = bottomSheetView.findViewById(R.id.buttonAplicarFiltros);

        buttonAplicarFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aplicarFiltros(editTextPrecioMin, editTextPrecioMax, editTextUbicacion);
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    /**
     * Aplica filtros a la lista de propiedades basados en los valores ingresados.
     *
     * @param editTextPrecioMin   Campo de texto para el precio mínimo.
     * @param editTextPrecioMax   Campo de texto para el precio máximo.
     * @param editTextUbicacion   Campo de texto para la ubicación.
     */
    private void aplicarFiltros(EditText editTextPrecioMin, EditText editTextPrecioMax, EditText editTextUbicacion) {
        String precioMinStr = editTextPrecioMin.getText().toString();
        String precioMaxStr = editTextPrecioMax.getText().toString();
        String ubicacion = editTextUbicacion.getText().toString();

        double precioMin = precioMinStr.isEmpty() ? Double.MIN_VALUE : Double.parseDouble(precioMinStr);
        double precioMax = precioMaxStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(precioMaxStr);

        if (!ubicacion.isEmpty()) {
            apiService.getPropiedadesPorUbicacion(ubicacion).enqueue(new Callback<List<Propiedad>>() {
                @Override
                public void onResponse(Call<List<Propiedad>> call, Response<List<Propiedad>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Propiedad> propiedadesFiltradas = response.body();
                        propiedadesFiltradas = filtrarPorPrecio(propiedadesFiltradas, precioMin, precioMax);
                        propiedadesAdapter.updatePropiedades(propiedadesFiltradas);
                    } else {
                        Toast.makeText(getActivity(), "Error al obtener propiedades", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Propiedad>> call, Throwable t) {
                    Toast.makeText(getActivity(), "Fallo en la llamada: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            List<Propiedad> propiedadesFiltradas = filtrarPorPrecio(listaPropiedades, precioMin, precioMax);
            propiedadesAdapter.updatePropiedades(propiedadesFiltradas);
        }
    }

    /**
     * Filtra una lista de propiedades basada en un rango de precios.
     *
     * @param propiedades La lista de propiedades a filtrar.
     * @param precioMin   El precio mínimo.
     * @param precioMax   El precio máximo.
     * @return Una lista de propiedades que se encuentran dentro del rango de precios.
     */
    private List<Propiedad> filtrarPorPrecio(List<Propiedad> propiedades, double precioMin, double precioMax) {
        List<Propiedad> propiedadesFiltradas = new ArrayList<>();
        for (Propiedad propiedad : propiedades) {
            if (propiedad.getPrecio() >= precioMin && propiedad.getPrecio() <= precioMax) {
                propiedadesFiltradas.add(propiedad);
            }
        }
        return propiedadesFiltradas;
    }

    /**
     * Filtra la lista de propiedades basada en el texto ingresado y actualiza el adaptador.
     *
     * @param texto El texto para filtrar la lista de propiedades.
     */
    private void filtrarPropiedades(String texto) {
        List<Propiedad> propiedadesFiltradas = new ArrayList<>();
        for (Propiedad propiedad : listaPropiedades) {
            if (propiedad.getTitulo().toLowerCase().contains(texto.toLowerCase()) ||
                    propiedad.getDescripcion().toLowerCase().contains(texto.toLowerCase()) ||
                    propiedad.getUbicacion().toLowerCase().contains(texto.toLowerCase())) {
                propiedadesFiltradas.add(propiedad);
            }
        }
        propiedadesAdapter.updatePropiedades(propiedadesFiltradas);
    }
}
