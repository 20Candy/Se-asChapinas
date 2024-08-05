package com.example.senaschapinasuvg;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.example.components.navMenu.BottomNavMenu;
import com.example.screens.base.BaseViewModel;
import com.example.screens.utils.SharedPreferencesManager;

public class MainActivity extends AppCompatActivity {

    SharedPreferencesManager sharedPreferencesManager;
    private BottomNavMenu bottomNavMenu;
    private BaseViewModel baseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        sharedPreferencesManager = new SharedPreferencesManager(this);

        bottomNavMenu = findViewById(R.id.bottom_nav_menu);
        baseViewModel = new ViewModelProvider(this).get(BaseViewModel.class);

        baseViewModel.getIsBottomNavVisible().observe(this, isVisible -> {
            if (bottomNavMenu != null) {
                bottomNavMenu.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            }
        });

        bottomNavMenu.setOnTabSelectedListener(new BottomNavMenu.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId) {
                    case BottomNavMenu.TAB_HOME:
                        navController.setGraph(com.example.screens.R.navigation.video_nav);
                        break;
                    case BottomNavMenu.TAB_TRANSLATE:
                        navController.setGraph(com.example.screens.R.navigation.translate_nav);
                        break;
                    case BottomNavMenu.TAB_DICTIONARY:
                        navController.setGraph(com.example.screens.R.navigation.dictionary_nav);
                        break;
                    case BottomNavMenu.TAB_PROFILE:
                        navController.setGraph(com.example.screens.R.navigation.profile_nav);
                        break;
                }
            }
        });

        // Verificar sesión activa
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
