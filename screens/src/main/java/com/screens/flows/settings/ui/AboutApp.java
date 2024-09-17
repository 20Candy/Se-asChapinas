package com.screens.flows.settings.ui;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.components.buttons.DebounceClickListener;
import com.screens.R;
import com.screens.base.BaseFragment;
import com.screens.databinding.FragmentAboutAppBinding;
import com.screens.databinding.FragmentSettingsBinding;

public class AboutApp extends BaseFragment {

    // Binding --------------------------------------------------------------------------------------

    FragmentAboutAppBinding binding;

    // Metodos del ciclo de vida ---------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAboutAppBinding.inflate(inflater, container, false);
        return binding.getRoot();    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    // Metodos privados -------------------------------------------------------

    private void setListeners(){
        binding.imgBackAbout.setOnClickListener(new DebounceClickListener(v-> {
            NavHostFragment.findNavController(this).popBackStack();
        }));

    }
}