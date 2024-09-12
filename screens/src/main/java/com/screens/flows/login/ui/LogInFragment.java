package com.screens.flows.login.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.components.buttons.DebounceClickListener;
import com.screens.R;
import com.screens.base.BaseFragment;
import com.screens.databinding.FragmentLogInBinding;
import com.screens.utils.SharedPreferencesManager;


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
        boolean allValid = !binding.emailInput.isEmpty() && !binding.passWordInput.isEmpty();
        binding.secondButton.setEnabled(allValid);
    }

    // On Click ------------------------------------------------------------------------------------
    @Override
    public void onClick(View view) {
    if (view.getId() == com.components.R.id.btn_transparent) {
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