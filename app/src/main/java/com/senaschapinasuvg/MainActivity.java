package com.senaschapinasuvg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.components.navMenu.BottomNavMenu;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.screens.base.BaseViewModel;
import com.screens.utils.SharedPreferencesManager;
import com.senaschapinasuvg.R;

public class MainActivity extends AppCompatActivity {

    SharedPreferencesManager sharedPreferencesManager;
    public BottomNavMenu bottomNavMenu;
    private BaseViewModel baseViewModel;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        // Variables
        sharedPreferencesManager = new SharedPreferencesManager(this);
        bottomNavMenu = findViewById(R.id.bottom_nav_menu);
        baseViewModel = new ViewModelProvider(this).get(BaseViewModel.class);


        // Observers
        baseViewModel.getIsBottomNavVisible().observe(this, isVisible -> {
            if (bottomNavMenu != null) {
                bottomNavMenu.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            }
        });

        baseViewModel.getSelectedTab().observe(this, newTab -> {
            if (newTab != null) {
                bottomNavMenu.setActiveTab(newTab);
            }
        });

        // Navegacion
        bottomNavMenu.setOnTabSelectedListener(new BottomNavMenu.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId) {
                    case BottomNavMenu.TAB_HOME:
                        navController.setGraph(com.screens.R.navigation.video_nav);
                        break;
                    case BottomNavMenu.TAB_TRANSLATE:
                        navController.setGraph(com.screens.R.navigation.translate_nav);
                        break;
                    case BottomNavMenu.TAB_DICTIONARY:
                        navController.setGraph(com.screens.R.navigation.dictionary_nav);
                        break;
                    case BottomNavMenu.TAB_PROFILE:
                        navController.setGraph(com.screens.R.navigation.profile_nav);
                        break;
                }
            }
        });

        // Verificar sesión activa
        if (!sharedPreferencesManager.getIdUsuario().isEmpty()) {
            navController.setGraph(com.screens.R.navigation.video_nav);
            bottomNavMenu.setActiveTab(BottomNavMenu.TAB_HOME);
        } else {
            navController.setGraph(com.screens.R.navigation.main_nav);
        }

        // Deeplink
        handleDeepLink(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        handleDeepLink(intent);
    }


    // Método para manejar el deep link
    private void handleDeepLink(Intent intent) {
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri data = intent.getData();
            if (data != null) {
                String path = data.getPath();
                if ("/cambio-contra".equals(path)) {

                    String email = data.getQueryParameter("email");
                    Bundle bundle = new Bundle();
                    bundle.putString("email", email);

                    navController.navigate(com.screens.R.id.changePasswordFragment2, bundle);
                }
            }
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
