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

public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    //Binding --------------------------------------------------------------------------------------
    FragmentSignUpBinding binding;

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
    }

    // On Click ------------------------------------------------------------------------------------
    @Override
    public void onClick(View view) {

    }
}