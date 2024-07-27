package com.example.screens.flows.landing.ui;

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


public class LandingFragment extends BaseFragment implements View.OnClickListener{

    //Binding --------------------------------------------------------------------------------------
    FragmentLandingBinding binding;

    // Metodos de ciclo de vida --------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLandingBinding.inflate(inflater, container, false);
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
    }

    // On Click ------------------------------------------------------------------------------------
    @Override
    public void onClick(View view) {
        if (view.getId() == com.example.components.R.id.btn_main) {
            navigateTo(binding.getRoot(), R.id.action_landingFragment_to_signUpFragment, null);

        } else if (view.getId() == com.example.components.R.id.btn_transparent) {
            navigateTo(binding.getRoot(), R.id.action_landingFragment_to_logInFragment, null);
        }

    }




}