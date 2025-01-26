package com.screens.flows.changePassword.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.components.buttons.DebounceClickListener;
import com.components.navMenu.BottomNavMenu;
import com.screens.R;
import com.screens.base.BaseFragment;
import com.screens.databinding.FragmentChangePasswordBinding;
import com.screens.flows.changePassword.vm.ChangePasswordViewModel;
import com.screens.flows.home.vm.HomeViewModel;
import com.screens.flows.profile.vm.ProfileViewModel;
import com.screens.flows.video.vm.VideoViewModel;
import com.screens.utils.SharedPreferencesManager;
import com.senaschapinas.flows.ChangePassword.ChangePasswordRequest;
import com.senaschapinas.flows.LogIn.LoginRequest;

public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {

    //Binding --------------------------------------------------------------------------------------
    FragmentChangePasswordBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    ProfileViewModel profileViewModel;
    ChangePasswordViewModel changePasswordViewModel;
    SharedPreferencesManager sharedPreferencesManager;
    boolean fromDeeplink;

    // Metodos de ciclo de vida --------------------------------------------------------------------


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        changePasswordViewModel = new ViewModelProvider(requireActivity()).get(ChangePasswordViewModel.class);
    }

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
        binding.passWordInput.setValidationListener(isValid -> checkAllInputs());
    }


    private void checkAllInputs() {
        boolean allValid = binding.passWordInput.isInputValid();
        binding.mainButton.setEnabled(allValid);
    }

    private void getProfileData(){
        if(profileViewModel.getNombre() != null || !profileViewModel.getNombre().isEmpty()){
            binding.emailInput.setText(profileViewModel.getNombre());
        }

        if(getArguments() != null){
            if(getArguments().containsKey("email")){
                binding.emailInput.setText(getArguments().getString("email"));
                fromDeeplink = true;
            }
            ChangePasswordViewModel.setBottomNavVisible(false);
        }
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
        showCustomDialogProgress(requireContext());

        sharedPreferencesManager = new SharedPreferencesManager(requireContext());

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setId_user(fromDeeplink? binding.emailInput.getEmailInput() : sharedPreferencesManager.getIdUsuario());
        changePasswordRequest.setNew_password(binding.passWordInput.getPassword());

        changePasswordViewModel.doChangePasswordRequest(changePasswordRequest);
        changePasswordViewModel.getChangePassword().observe(getViewLifecycleOwner(), login -> {
            switch (login.status) {
                case SUCCESS:
                    hideCustomDialogProgress();

                    showCustomDialogMessage(
                            "Tu contraseña ha sido aztualizada existosamente.",
                            "¡Contraseña actualizada!",
                            "Cerrar",
                            "",
                            () -> {
                                // Cofirmar
                                if(fromDeeplink){
                                    NavHostFragment.findNavController(this).popBackStack();
                                }else{
                                    HomeViewModel.selectTab(BottomNavMenu.TAB_HOME);
                                }

                            },
                            ContextCompat.getColor(getContext(), com.components.R.color.base_blue)
                    );

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