package com.example.screens.flows.landing.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.screens.R;
import com.example.screens.databinding.FragmentLandingBinding;

public class LandingFragment extends Fragment {

    //Binding --------------------------------------------------------------------------------------
    FragmentLandingBinding binding;

    //Atributos de la clase ------------------------------------------------------------------------


    // Metodos de ciclo de vida --------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLandingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}