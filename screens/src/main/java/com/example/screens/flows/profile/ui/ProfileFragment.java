package com.example.screens.flows.profile.ui;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ProfileFragment extends BaseFragment {

    //Binding --------------------------------------------------------------------------------------
    FragmentProfileBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    private ProfileViewModel profileViewModel;
    private SharedPreferencesManager sharedPreferencesManager;

    private VideoFavoriteAdapter videoFavoriteAdapter;
    private TranslateFavoriteAdapter translateFavoriteAdapter;


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

    @Override
    public void onResume() {
        super.onResume();
        ProfileViewModel.setBottomNavVisible(true);
        ProfileViewModel.selectTab(BottomNavMenu.TAB_PROFILE);
    }

    // Metodos privados de la clase ----------------------------------------------------------------
    private void setListeners(){
        binding.navBar.setOnTabSelectedListener((tabId) ->{
            switch (tabId) {
                // Tab video
                case SettingsNavBar.VIDEO:
                    binding.rvFavoritos.setAdapter(videoFavoriteAdapter);
                    break;
                // Tab translate
                case SettingsNavBar.TRANSLATE:
                    binding.rvFavoritos.setAdapter(translateFavoriteAdapter);

                    break;
            }
        });

        binding.imgSettings.setOnClickListener(new DebounceClickListener(view -> {
            navigateTo(binding.getRoot(), R.id.action_profileFragment_to_settingsFragment, null);

        }));

        binding.llScore.setOnClickListener(new DebounceClickListener(view -> {
            sharedPreferencesManager = new SharedPreferencesManager(requireContext());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            String todayDate = dateFormat.format(new Date());

            String lastShownDate = sharedPreferencesManager.getLastChallengeShowDate();
            if (!todayDate.equals(lastShownDate) && sharedPreferencesManager.isChallengeShow() ) {
                navigateTo(binding.getRoot(), R.id.action_profileFragment_to_challengeFragment, null);

            }else{
                Toast.makeText(getContext(), "Ya has completado tu reto diario", Toast.LENGTH_SHORT).show();

            }

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

    private void setVideoAdapter(ArrayList<ObjVideoFav> videoFavorites){
        videoFavoriteAdapter = new VideoFavoriteAdapter(getContext(), videoFavorites, video -> {
            // TODO NAVEGAR A PANTALLA DE VIDEO
        });
        binding.rvFavoritos.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.rvFavoritos.getContext(),
                LinearLayoutManager.VERTICAL);
        binding.rvFavoritos.addItemDecoration(dividerItemDecoration);
    }



    private void setTranslateAdapter(ArrayList<ObjTraFav> traduccionesFav){
        translateFavoriteAdapter = new TranslateFavoriteAdapter(getContext(), traduccionesFav, traduccion -> {
            // TODO NAVEGAR A PANTALLA DE TRADUCCION
        });
        binding.rvFavoritos.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.rvFavoritos.getContext(),
                LinearLayoutManager.VERTICAL);
        binding.rvFavoritos.addItemDecoration(dividerItemDecoration);
    }



    // Servicios -----------------------------------------------------------------------------------

    private void servicioPerfil(){
        // TODO SERVICIO Y OBJETO

        // ON SUCEES SETAR VALORES
        String nombre = "Ejemplo";
        String racha = "10";
        String imagen = "azul";

        setViewData(nombre, racha, imagen);

        servicioVideosFavoritos();

    }

    private void servicioVideosFavoritos(){

        // TODO SERVICIO
        // ON SUCESS

        // TODO VIDEO OBTENER PRIMERA IMAGEN

        ArrayList<ObjVideoFav> videoFavorites = new ArrayList<>();
        videoFavorites.add(new ObjVideoFav("", "Hoy voy a la universidad", "Yo hoy universidad ir"));
        videoFavorites.add(new ObjVideoFav("", "Hoy voy a la universidad", "Yo hoy universidad ir"));
        videoFavorites.add(new ObjVideoFav("", "Hoy voy a la universidad", "Yo hoy universidad ir"));
        videoFavorites.add(new ObjVideoFav("", "Hoy voy a la universidad", "Yo hoy universidad ir"));
        videoFavorites.add(new ObjVideoFav("", "Hoy voy a la universidad", "Yo hoy universidad ir"));


        setVideoAdapter(videoFavorites);

        servicioTraduccionesFavoritas();
    }


    private void servicioTraduccionesFavoritas(){
        // TODO SERVICIO
        // ON SUCESS

        ArrayList<ObjTraFav> traduccionesFavoritas = new ArrayList<>();
        traduccionesFavoritas.add(new ObjTraFav( "Hoy voy a la universidad", "Yo hoy universidad ir"));
        traduccionesFavoritas.add(new ObjTraFav( "Hoy voy a la universidad", "Yo hoy universidad ir"));
        traduccionesFavoritas.add(new ObjTraFav( "Hoy voy a la universidad", "Yo hoy universidad ir"));
        traduccionesFavoritas.add(new ObjTraFav( "Hoy voy a la universidad", "Yo hoy universidad ir"));
        traduccionesFavoritas.add(new ObjTraFav( "Hoy voy a la universidad", "Yo hoy universidad ir"));

        setTranslateAdapter(traduccionesFavoritas);
        binding.rvFavoritos.setAdapter(videoFavoriteAdapter);



    }
}