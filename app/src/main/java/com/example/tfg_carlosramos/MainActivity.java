package com.example.tfg_carlosramos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tfg_carlosramos.fragments.ClienteFragment;
import com.example.tfg_carlosramos.fragments.HomeFragment;
import com.example.tfg_carlosramos.fragments.NotasFragment;
import com.example.tfg_carlosramos.fragments.PropiedadesFragment;
import com.example.tfg_carlosramos.fragments.TransaccionesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.navigation_clientes) {
                selectedFragment = new ClienteFragment();
            } else if (itemId == R.id.navigation_propiedades) {
                selectedFragment = new PropiedadesFragment();
            } else if (itemId == R.id.navigation_notas) {
                selectedFragment = new NotasFragment();
            }else if (itemId == R.id.navigation_transacciones) {
                selectedFragment = new TransaccionesFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Establece el fragmento inicial para que aparezca el home como principal
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.navigation_home);
        }
    }
}
