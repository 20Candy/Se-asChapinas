package com.example.screens.flows.login.ui;

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
import com.example.screens.databinding.FragmentLogInBinding;
import com.example.screens.databinding.FragmentSignUpBinding;
import com.example.screens.utils.SharedPreferencesManager;


public class LogInFragment extends BaseFragment implements View.OnClickListener {

    //Binding --------------------------------------------------------------------------------------
    FragmentLogInBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    SharedPreferencesManager sharedPreferencesManager;


    // Metodos de ciclo de vida --------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startListeners();
    }

    // Metodos privados de la clase ----------------------------------------------------------------

    private void startListeners(){
        binding.secondButton.setOnClickListener(new DebounceClickListener(this));
        binding.emailInput.setValidationListener(isValid -> checkAllInputs());
        binding.passWordInput.setValidationListener(isValid -> checkAllInputs());
    }

    private void checkAllInputs() {
        boolean allValid = !binding.emailInput.isInputValid() && !binding.passWordInput.isEmpty();
        binding.secondButton.setEnabled(allValid);
    }

    // On Click ------------------------------------------------------------------------------------
    @Override
    public void onClick(View view) {
    if (view.getId() == com.example.components.R.id.btn_transparent) {
            loginService();
        }

    }

    // Services ------------------------------------------------------------------------------------
    private void loginService(){
        //TODO IMPLEMENTAR SERVICIO

        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        sharedPreferencesManager.setLogged(true);

        navigateTo(binding.getRoot(), R.id.action_logInFragment_to_video_nav, null);

    }




}