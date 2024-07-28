package com.example.screens.flows.home.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.components.buttons.DebounceClickListener;
import com.example.screens.R;
import com.example.screens.base.BaseFragment;
import com.example.screens.databinding.FragmentHomeBinding;
import com.example.screens.flows.home.vm.HomeViewModel;
import com.example.screens.utils.SharedPreferencesManager;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    //Binding --------------------------------------------------------------------------------------
    FragmentHomeBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------

    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private HomeViewModel homeViewModel;
    private SharedPreferencesManager sharedPreferencesManager;

    // Metodos de ciclo de vida --------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBackPressed(() -> {});
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        verifyAndHandlePermissions();
        setupListeners();
    }

    // Metodos privados de la clase ----------------------------------------------------------------
    private void setupListeners() {
        binding.btnEmpezar.setOnClickListener(new DebounceClickListener(this));
    }

    private void verifyAndHandlePermissions() {
        // Es primer login
        if (sharedPreferencesManager.isFirstLogin()) {
            sharedPreferencesManager.setFirstLogin(false);  // Actualiza first login status
            requestPermissionsIfNeeded();

        // No es primer login
        } else {
            //Usuario permite encender camara al abrir la app
            if (homeViewModel.getEncenderCamara()) {
                // Tiene permisos abrir camara
                if (hasAllPermissionsGranted()) {
                    encenderCamara();
                // No tiene permisos muestra pantalla gris
                } else {
                    showGreyScreen(true);
                }
            // Usuario no permite encender camara al abrir la app
            } else {
                showGreyScreen(false);
            }
        }
    }


    private boolean hasAllPermissionsGranted() {
        return ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionsIfNeeded() {
        requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO}, REQUEST_CAMERA_PERMISSION);
    }

    private void encenderCamara() {
        binding.llEmpty.setVisibility(View.GONE);
    }

    private void showGreyScreen(boolean permissionsNeeded) {
        binding.llEmpty.setVisibility(View.VISIBLE);
        binding.tvEmtpy.setText(permissionsNeeded ? R.string.permissions_alert : R.string.camera_alert); // Cambia mensaje
    }

    // On Click ------------------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        if (view.getId() == com.example.components.R.id.btn_main) {
            // Si tiene permisos enciende camara
            if (hasAllPermissionsGranted()) {
                encenderCamara();
            // Si no tiene permisos pide
            } else {
                requestPermissionsIfNeeded();
            }

        }
    }

    // Permisos ------------------------------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Da permisos
                encenderCamara();
            } else {
                // Muestra pantalla gris nuevamente
                showGreyScreen(true);
            }
        }
    }
}
