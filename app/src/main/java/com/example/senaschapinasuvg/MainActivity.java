package com.example.senaschapinasuvg;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.screens.utils.SharedPreferencesManager;

public class MainActivity extends AppCompatActivity {

    SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        sharedPreferencesManager = new SharedPreferencesManager(this);

        // Verificar sesion activa
        if (sharedPreferencesManager.isLogged()) {
            navController.setGraph(com.example.screens.R.navigation.video_nav);
        } else {
            navController.setGraph(com.example.screens.R.navigation.main_nav);
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = ((NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment))
                .getNavController();
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
