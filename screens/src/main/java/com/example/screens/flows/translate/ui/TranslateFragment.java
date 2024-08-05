package com.example.screens.flows.translate.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.components.navMenu.BottomNavMenu;
import com.example.screens.R;
import com.example.screens.base.BaseFragment;
import com.example.screens.databinding.FragmentHomeBinding;
import com.example.screens.databinding.FragmentTranslateBinding;
import com.example.screens.flows.home.vm.HomeViewModel;
import com.example.screens.flows.translate.vm.TranslateViewModel;
import com.example.screens.utils.SharedPreferencesManager;


public class TranslateFragment extends BaseFragment {

    //Binding --------------------------------------------------------------------------------------
    FragmentTranslateBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    private TranslateViewModel translateViewModel;


    // Metodos de ciclo de vida --------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translateViewModel = new ViewModelProvider(requireActivity()).get(TranslateViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTranslateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBackPressed(() -> {});
    }

    // Metodos privados de la clase ----------------------------------------------------------------

}