package com.example.tfg_carlosramos.Model;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String estado = position == 0 ? "Pendiente" : "Completada";
        return TransaccionesListFragment.newInstance(estado);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
