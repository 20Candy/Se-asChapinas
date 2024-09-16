package com.screens.flows.forgotPassword.ui;

;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.components.buttons.DebounceClickListener;
import com.screens.R;
import com.screens.base.BaseFragment;
import com.screens.databinding.FragmentForgotPasswordBinding;
import com.screens.flows.forgotPassword.vm.ForgotPasswordViewModel;
import com.senaschapinas.flows.ForgotPassword.ForgotPasswordRequest;


public class ForgotPasswordFragment extends BaseFragment implements View.OnClickListener {


    //Binding --------------------------------------------------------------------------------------
    FragmentForgotPasswordBinding binding;


    // Atributos de la clase -----------------------------------------------------------------------
    ForgotPasswordViewModel forgotPasswordViewModel;


    // Metodos de ciclo de vida --------------------------------------------------------------------


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgotPasswordViewModel = new ViewModelProvider(requireActivity()).get(ForgotPasswordViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startListeners();
    }

    // Metodos privados de la clase ----------------------------------------------------------------

    private void startListeners(){
        binding.emailInput.setValidationListener(isValid -> checkAllInputs());
        binding.secondButton.setOnClickListener(new DebounceClickListener(this));
    }

    private void checkAllInputs() {
        boolean allValid = binding.emailInput.isInputValid();
        binding.secondButton.setEnabled(allValid);
    }

    // On Click ------------------------------------------------------------------------------------
    @Override
    public void onClick(View view) {
        if (view.getId() == com.components.R.id.btn_transparent) {
            forgotPassService();
        }

    }

    private void forgotPassService() {

        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail(binding.emailInput.getEmailInput());

        forgotPasswordViewModel.doForgotPasswordRequest(forgotPasswordRequest);
        forgotPasswordViewModel.getForgotPassword().observe(getViewLifecycleOwner(), change -> {
            switch (change.status) {
                case SUCCESS:
                    showCustomDialogMessage(
                            "Hemos enviado un correo de recuperación de contraseña a tu correo." ,
                            "¡Revisa tu correo!",
                            "",
                            "Cerrar",
                            null,
                            ()->{
                                clearBackStackTo(binding.getRoot(), R.id.landingFragment);
                            },
                            ContextCompat.getColor(getContext(), com.components.R.color.base_blue)

                    );
                    break;

                case ERROR:
                    hideCustomDialogProgress();
                    showCustomDialogMessage(
                            change.message,
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