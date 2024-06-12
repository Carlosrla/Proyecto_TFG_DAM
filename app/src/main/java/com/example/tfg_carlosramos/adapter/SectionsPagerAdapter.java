package com.example.tfg_carlosramos.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tfg_carlosramos.Model.ClienteNotasTabFragment;
import com.example.tfg_carlosramos.fragments.ClienteCitasFragment;

/**
 * Adaptador para manejar los fragmentos de las pestañas de notas y citas de un cliente.
 */
public class SectionsPagerAdapter extends FragmentStateAdapter {

    private final int clienteId;
    private final String clienteNombre;

    /**
     * Constructor del adaptador.
     *
     * @param fragment     Fragmento padre.
     * @param clienteId    ID del cliente.
     * @param clienteNombre Nombre del cliente.
     */
    public SectionsPagerAdapter(@NonNull Fragment fragment, int clienteId, String clienteNombre) {
        super(fragment);
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
    }

    /**
     * Crea el fragmento correspondiente para cada posición.
     *
     * @param position Posición del fragmento.
     * @return El fragmento correspondiente.
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return ClienteNotasTabFragment.newInstance(clienteId, clienteNombre);
        } else {
            return ClienteCitasFragment.newInstance(clienteId, clienteNombre);
        }
    }

    /**
     * Devuelve el número total de fragmentos.
     *
     * @return Número total de fragmentos.
     */
    @Override
    public int getItemCount() {
        return 2;
    }
}
