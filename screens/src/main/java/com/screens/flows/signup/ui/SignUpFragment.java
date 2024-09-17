package com.screens.flows.signup.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.components.buttons.DebounceClickListener;
import com.components.navMenu.BottomNavMenu;
import com.screens.R;
import com.screens.base.BaseFragment;
import com.screens.databinding.FragmentSignUpBinding;
import com.screens.flows.home.vm.HomeViewModel;
import com.screens.flows.login.vm.LoginViewModel;
import com.screens.flows.signup.vm.SignUpViewModel;
import com.screens.utils.SharedPreferencesManager;
import com.senaschapinas.flows.SignUp.SignUpRequest;

import java.util.Random;

public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    //Binding --------------------------------------------------------------------------------------
    FragmentSignUpBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    SharedPreferencesManager sharedPreferencesManager;
    SignUpViewModel signUpViewModel;


    // Metodos de ciclo de vida --------------------------------------------------------------------


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpViewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);

    }

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

    public String randomFileSelector(){
         String[] files = {"q_azul", "q_verde", "q_aqua", "q_amarillo"};
         Random random = new Random();
         int index = random.nextInt(files.length);
         return files[index];

    }


    // On Click ------------------------------------------------------------------------------------
    @Override
    public void onClick(View view) {
        if (view.getId() == com.components.R.id.btn_main) {
            if (binding.mainButton.isEnabled()) {
                signUpService();

            }

        } else if (view.getId() == com.components.R.id.btn_transparent) {
            navigateTo(binding.getRoot(), R.id.action_signUpFragment_to_logInFragment, null);
        }

    }

    // Servicios -----------------------------------------------------------------------------------
    private void signUpService(){
        showCustomDialogProgress(requireContext());

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setMail(binding.emailInput.getEmailInput());
        signUpRequest.setPassword(binding.passWordInput.getPassword());
        signUpRequest.setQuetzalito(randomFileSelector());

        signUpViewModel.doSignUpRequest(signUpRequest);
        signUpViewModel.getSignUpResult().observe(getViewLifecycleOwner(), login -> {
            switch (login.status) {
                case SUCCESS:
                    hideCustomDialogProgress();

                    sharedPreferencesManager = new SharedPreferencesManager(requireContext());
                    sharedPreferencesManager.setIdUsuario(login.data.getId_user().toString());
                    sharedPreferencesManager.setFirstLogin(true);

                    HomeViewModel.selectTab(BottomNavMenu.TAB_HOME);

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