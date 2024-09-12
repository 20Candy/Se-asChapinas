package com.screens.flows.changePassword.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.components.buttons.DebounceClickListener;
import com.components.navMenu.BottomNavMenu;
import com.screens.base.BaseFragment;
import com.screens.databinding.FragmentChangePasswordBinding;
import com.screens.flows.home.vm.HomeViewModel;
import com.screens.utils.SharedPreferencesManager;

public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {

    //Binding --------------------------------------------------------------------------------------
    FragmentChangePasswordBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    SharedPreferencesManager sharedPreferencesManager;


    // Metodos de ciclo de vida --------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startListeners();
        getProfileData();
    }

    // Metodos privados de la clase ----------------------------------------------------------------
    private void startListeners(){
        binding.mainButton.setOnClickListener(new DebounceClickListener(this));
        binding.emailInput.setValidationListener(isValid -> checkAllInputs());
        binding.passWordInput.setValidationListener(isValid -> checkAllInputs());
    }

    private void checkAllInputs() {
        boolean allValid = binding.emailInput.isInputValid() && binding.passWordInput.isInputValid();
        binding.mainButton.setEnabled(allValid);
    }


    // On Click ------------------------------------------------------------------------------------
    @Override
    public void onClick(View view) {
        if (view.getId() == com.components.R.id.btn_main) {
                changePasswordService();

        }

    }

    // Servicios -----------------------------------------------------------------------------------
    private void changePasswordService(){
        // TODO LLAMAR SERVICIO
        // Sucess

        showCustomDialogMessage(
                "Tu contraseña ha sido aztualizada existosamente.",
                "¡Contraseña actualizada!",
                "Cerrar",
                "",
                () -> {
                    // Cofirmar
                    HomeViewModel.selectTab(BottomNavMenu.TAB_HOME);

                },
                ContextCompat.getColor(getContext(), com.components.R.color.base_blue)
        );


    }


    private void getProfileData(){
        // TODO LLAMAR SERVICIO

        //SUCESS
        String correo = "ejemplo@gmail.com";
        binding.emailInput.setText(correo);
        binding.emailInput.disableInput();


    }
}