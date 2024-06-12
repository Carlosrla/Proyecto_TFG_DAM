package com.example.tfg_carlosramos.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.Model.ViewPagerAdapter;
import com.example.tfg_carlosramos.Model.RegistrarTransaccionDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * Fragmento para gestionar las transacciones.
 * Utiliza un ViewPager2 y TabLayout para mostrar las transacciones pendientes y completadas en pestañas separadas.
 */
public class TransaccionesFragment extends Fragment {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    /**
     * Método que se llama para inflar el diseño del fragmento.
     * @param inflater Inflater para inflar el diseño del fragmento.
     * @param container Contenedor en el que se inserta el fragmento.
     * @param savedInstanceState Estado guardado del fragmento.
     * @return La vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaccion, container, false);
    }

    /**
     * Método que se llama una vez que la vista del fragmento ha sido creada.
     * Configura el ViewPager2 y el TabLayout para mostrar las transacciones.
     * @param view La vista creada.
     * @param savedInstanceState Estado guardado del fragmento.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa el TabLayout y el ViewPager2
        tabLayout = view.findViewById(R.id.tabLayoutTransacciones);
        viewPager = view.findViewById(R.id.viewPagerTransacciones);
        viewPager.setAdapter(new ViewPagerAdapter(this));

        // Conecta el TabLayout con el ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "Pendientes" : "Completadas")
        ).attach();

        // Configura el botón flotante para registrar una nueva transacción
        view.findViewById(R.id.fabAddTransaccion).setOnClickListener(v -> mostrarDialogoRegistrarTransaccion());
    }

    /**
     * Muestra el diálogo para registrar una nueva transacción.
     */
    private void mostrarDialogoRegistrarTransaccion() {
        RegistrarTransaccionDialog dialog = RegistrarTransaccionDialog.newInstance();
        dialog.show(getParentFragmentManager(), "RegistrarTransaccionDialog");
    }
}
