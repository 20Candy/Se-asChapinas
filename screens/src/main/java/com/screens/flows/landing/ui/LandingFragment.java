package com.screens.flows.landing.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.components.buttons.DebounceClickListener;
import com.screens.R;
import com.screens.base.BaseFragment;
import com.screens.databinding.FragmentLandingBinding;
import com.screens.flows.landing.vm.LandingViewModel;


public class LandingFragment extends BaseFragment implements View.OnClickListener{

    //Binding --------------------------------------------------------------------------------------
    FragmentLandingBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    private LandingViewModel landingViewModel;


    // Metodos de ciclo de vida --------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        landingViewModel = new ViewModelProvider(requireActivity()).get(LandingViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLandingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        onBackPressed(() -> {});
        super.onViewCreated(view, savedInstanceState);
        startListeners();
    }

    @Override
    public void onResume() {
        super.onResume();

        LandingViewModel.setBottomNavVisible(false);
    }

    // Metodos privados de la clase ----------------------------------------------------------------
    private void startListeners(){
        binding.mainButton.setOnClickListener(new DebounceClickListener(this));
        binding.secondButton.setOnClickListener(new DebounceClickListener(this));
    }

    // On Click ------------------------------------------------------------------------------------
    @Override
    public void onClick(View view) {
        if (view.getId() == com.components.R.id.btn_main) {
            navigateTo(binding.getRoot(), R.id.action_landingFragment_to_signUpFragment, null);

        } else if (view.getId() == com.components.R.id.btn_transparent) {
            navigateTo(binding.getRoot(), R.id.action_landingFragment_to_logInFragment, null);
        }

    }




}