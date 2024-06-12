package com.example.tfg_carlosramos.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.tfg_carlosramos.R;
import com.example.tfg_carlosramos.adapter.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * Fragmento que muestra las notas y citas de un cliente específico usando tabs.
 */
public class ClienteNotasFragment extends Fragment {

    private static final String ARG_CLIENTE_ID = "cliente_id";
    private static final String ARG_CLIENTE_NOMBRE = "cliente_nombre";

    private int clienteId;
    private String clienteNombre;

    /**
     * Crea una nueva instancia del fragmento con los parámetros proporcionados.
     *
     * @param clienteId    ID del cliente.
     * @param clienteNombre Nombre del cliente.
     * @return Una nueva instancia de ClienteNotasFragment.
     */
    public static ClienteNotasFragment newInstance(int clienteId, String clienteNombre) {
        ClienteNotasFragment fragment = new ClienteNotasFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CLIENTE_ID, clienteId);
        args.putString(ARG_CLIENTE_NOMBRE, clienteNombre);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cliente_notas, container, false);

        if (getArguments() != null) {
            clienteId = getArguments().getInt(ARG_CLIENTE_ID);
            clienteNombre = getArguments().getString(ARG_CLIENTE_NOMBRE);
        }

        TextView nombreClienteTextView = view.findViewById(R.id.nombreCliente);
        nombreClienteTextView.setText("Notas y Citas de " + clienteNombre);

        Button btnAtras = view.findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(v -> {
            // Aquí se implementa la navegación para ir a la pestaña de selección de cliente
            requireActivity().onBackPressed(); // Esto simula el botón "Atrás"
        });

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, clienteId, clienteNombre);
        viewPager.setAdapter(sectionsPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Notas");
            } else {
                tab.setText("Citas");
            }
        }).attach();

        return view;
    }
}
