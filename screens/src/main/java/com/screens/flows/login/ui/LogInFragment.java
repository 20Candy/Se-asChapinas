package com.screens.flows.login.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.components.buttons.DebounceClickListener;
import com.screens.R;
import com.screens.base.BaseFragment;
import com.screens.databinding.FragmentLogInBinding;
import com.screens.flows.login.vm.LoginViewModel;
import com.screens.utils.SharedPreferencesManager;
import com.senaschapinas.flows.ForgotPassword.ForgotPasswordRequest;
import com.senaschapinas.flows.LogIn.LoginRequest;

import java.util.Random;


public class LogInFragment extends BaseFragment implements View.OnClickListener {

    //Binding --------------------------------------------------------------------------------------
    FragmentLogInBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    SharedPreferencesManager sharedPreferencesManager;

    LoginViewModel loginViewModel;


    // Metodos de ciclo de vida --------------------------------------------------------------------


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

    }

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
        binding.tvChangePass.setOnClickListener(new DebounceClickListener(this));
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
        } else if (view.getId() == R.id.tvChangePass) {
            navigateTo(binding.getRoot(), R.id.action_logInFragment_to_forgotPasswordFragment, null);
        }

    }

    // Services ------------------------------------------------------------------------------------
    private void loginService() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(binding.emailInput.getEmailInput());
        loginRequest.setPassword(binding.passWordInput.getPassword());

        loginViewModel.doLoginRequest(loginRequest);
        loginViewModel.getLogin().observe(getViewLifecycleOwner(), login -> {
            switch (login.status) {
                case SUCCESS:
                    showCustomDialogProgress(requireActivity());
                    sharedPreferencesManager = new SharedPreferencesManager(requireContext());
                    sharedPreferencesManager.setLogged(true);

                    navigateTo(binding.getRoot(), R.id.action_logInFragment_to_video_nav, null);
                    break;
                case ERROR:
                    hideCustomDialogProgress();
                    showCustomDialogMessage(
                            login.message,
                            "Oops algo salió mal",
                            "",
                            "Cerrar",
                            null,
                            ContextCompat.getColor(getContext(), com.components.R.color.base_red)
                    );
                    break;
            }
        });

    }






}