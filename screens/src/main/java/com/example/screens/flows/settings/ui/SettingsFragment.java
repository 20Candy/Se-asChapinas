package com.example.screens.flows.settings.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.components.buttons.DebounceClickListener;
import com.example.screens.R;
import com.example.screens.base.BaseFragment;
import com.example.screens.databinding.FragmentSettingsBinding;
import com.example.screens.databinding.FragmentVideoBinding;
import com.example.screens.flows.home.vm.HomeViewModel;
import com.example.screens.utils.SharedPreferencesManager;


public class SettingsFragment extends BaseFragment {

    // Binding --------------------------------------------------------------------------------------

    FragmentSettingsBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    private SharedPreferencesManager sharedPreferencesManager;


    // Metodos de ciclo de vida --------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        setInitValues();
        setListeners();
    }

    // Metodos privados de la clase ----------------------------------------------------------------
    private void setListeners(){
        binding.imgBack.setOnClickListener(new DebounceClickListener(v->{
            onBackPressed(() -> {
                navigateTo(binding.getRoot(), R.id.profile_nav, null);
            });

        }));

        binding.llcontra.setOnClickListener(new DebounceClickListener(v->{
            navigateTo(binding.getRoot(), R.id.action_settingsFragment_to_changePasswordFragment, null);

        }));


        binding.llcerrar.setOnClickListener(new DebounceClickListener(v->{
            showCustomDialogMessage(
                    "¿Estás seguro que quieres cerrar sesión?",
                    "Estas cerrando sesión",
                    "Confirmar",
                    "Cerrar",
                    () -> {
                        // Cofirmar
                        goToLandingFragment(binding.getRoot());

                    }
            );

        }));

        binding.lleliminar.setOnClickListener(new DebounceClickListener(v->{
            showCustomDialogMessage(
                    "Tus videos y traducciones seran eliminados permanentemente",
                    "¿Estás seguro que quieres eliminar tu cuenta?",
                    "Confirmar",
                    "Cerrar",
                    () -> {
                        // Cofirmar
                        servicioEliminarCuenta();

                    }
            );

        }));

        binding.llsobre.setOnClickListener(new DebounceClickListener(v->{
            navigateTo(binding.getRoot(), R.id.action_settingsFragment_to_aboutApp, null);

        }));

        binding.llreto.setOnClickListener(new DebounceClickListener(v->{
            binding.switchReto.setChecked(!binding.switchReto.isChecked());
            sharedPreferencesManager.setChallengeShow(binding.switchReto.isChecked());

        }));

        binding.llcamara.setOnClickListener(new DebounceClickListener(v->{
            binding.switchCamara.setChecked(!binding.switchCamara.isChecked());
            sharedPreferencesManager.setOpenCamera(binding.switchCamara.isChecked());

        }));

    }

    private void setInitValues(){
        binding.switchReto.setChecked(sharedPreferencesManager.isChallengeShow());
        binding.switchCamara.setChecked(sharedPreferencesManager.isOpenCamera());
    }



    // Servicio ------------------------------------------------------------------------------------
    private void servicioEliminarCuenta(){

        // TODO
        // ON SUCESS
        goToLandingFragment(binding.getRoot());

    }

}