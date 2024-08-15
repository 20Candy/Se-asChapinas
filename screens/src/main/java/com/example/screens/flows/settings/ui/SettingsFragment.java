package com.example.screens.flows.settings.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.components.buttons.DebounceClickListener;
import com.example.screens.R;
import com.example.screens.base.BaseFragment;
import com.example.screens.databinding.FragmentSettingsBinding;
import com.example.screens.databinding.FragmentVideoBinding;


public class SettingsFragment extends BaseFragment {

    // Binding --------------------------------------------------------------------------------------

    FragmentSettingsBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------



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
                        navigateTo(binding.getRoot(), R.id.main_nav, null);

                    }
            );

        }));

        binding.lleliminar.setOnClickListener(new DebounceClickListener(v->{
            showCustomDialogMessage(
                    "¿Estás seguro que quieres eliminar tu cuenta?",
                    "Tus videos y traducciones seran eliminados permanentemente",
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

        }));

        binding.llcamara.setOnClickListener(new DebounceClickListener(v->{
            binding.switchCamara.setChecked(!binding.switchCamara.isChecked());

        }));


    }

    // Servicio ------------------------------------------------------------------------------------
    private void servicioEliminarCuenta(){

        // TODO
        // ON SUCESS
        navigateTo(binding.getRoot(), R.id.main_nav, null);

    }

}