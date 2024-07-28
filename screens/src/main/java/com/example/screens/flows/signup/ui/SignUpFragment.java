package com.example.screens.flows.signup.ui;

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
import com.example.screens.databinding.FragmentLandingBinding;
import com.example.screens.databinding.FragmentSignUpBinding;
import com.example.screens.utils.SharedPreferencesManager;

public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    //Binding --------------------------------------------------------------------------------------
    FragmentSignUpBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    SharedPreferencesManager sharedPreferencesManager;


    // Metodos de ciclo de vida --------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startListeners();
    }

    // Metodos privados de la clase ----------------------------------------------------------------
    private void startListeners(){
        binding.mainButton.setOnClickListener(new DebounceClickListener(this));
        binding.secondButton.setOnClickListener(new DebounceClickListener(this));
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
        if (view.getId() == com.example.components.R.id.btn_main) {
            if (binding.mainButton.isEnabled()) {
                signUpService();

            }

        } else if (view.getId() == com.example.components.R.id.btn_transparent) {
            navigateTo(binding.getRoot(), R.id.action_signUpFragment_to_logInFragment, null);
        }

    }

    // Servicios -----------------------------------------------------------------------------------
    private void signUpService(){
        // TODO LLAMAR SERVICIO

        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        sharedPreferencesManager.setLogged(true);

        navigateTo(binding.getRoot(), R.id.action_signUpFragment_to_video_nav, null);

    }
}