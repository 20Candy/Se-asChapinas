package com.example.screens.flows.profile.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.components.SettingsNavBar.SettingsNavBar;
import com.example.components.buttons.DebounceClickListener;
import com.example.components.navMenu.BottomNavMenu;
import com.example.screens.R;
import com.example.screens.base.BaseFragment;
import com.example.screens.databinding.FragmentHomeBinding;
import com.example.screens.databinding.FragmentProfileBinding;
import com.example.screens.flows.home.vm.HomeViewModel;
import com.example.screens.flows.profile.vm.ProfileViewModel;
import com.example.screens.utils.SharedPreferencesManager;


public class ProfileFragment extends BaseFragment {

    //Binding --------------------------------------------------------------------------------------
    FragmentProfileBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    private ProfileViewModel profileViewModel;

    // Metodos de ciclo de vida --------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBackPressed(() -> {});
        setListeners();
        servicioPerfil();
    }


    // Metodos privados de la clase ----------------------------------------------------------------
    private void setListeners(){
        binding.navBar.setOnTabSelectedListener((tabId) ->{
            switch (tabId) {
                // Tab video
                case SettingsNavBar.VIDEO:
                    break;
                // Tab translate
                case SettingsNavBar.TRANSLATE:
                    break;
            }
        });

        binding.imgSettings.setOnClickListener(new DebounceClickListener(view -> {
            navigateTo(binding.getRoot(), R.id.action_profileFragment_to_settingsFragment, null);

        }));

        binding.llScore.setOnClickListener(new DebounceClickListener(view -> {
            navigateTo(binding.getRoot(), R.id.action_profileFragment_to_challengeFragment, null);

        }));

    }

    private void setViewData(String name, String score, String image){

        binding.tvName.setText(name);
        binding.tvScore.setText(score);

        if(image.equals("azul")){
            binding.imgProfile.setImageDrawable(getResources().getDrawable(com.example.components.R.drawable.q_azul));
        }else{
            //TODO
        }

    }

    // Servicios -----------------------------------------------------------------------------------

    private void servicioPerfil(){
        // TODO SERVICIO Y OBJETO

        // ON SUCEES SETAR VALORES
        String nombre = "Ejemplo";
        String racha = "10";
        String imagen = "azul";

        setViewData(nombre, racha, imagen);
    }
}