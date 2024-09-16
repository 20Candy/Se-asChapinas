package com.screens.flows.settings.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.components.bottomsheet.BottomSheet;
import com.components.buttons.DebounceClickListener;
import com.screens.R;
import com.screens.base.BaseFragment;
import com.screens.databinding.FragmentSettingsBinding;
import com.screens.flows.login.vm.LoginViewModel;
import com.screens.flows.settings.vm.SettingsViewModel;
import com.screens.utils.SharedPreferencesManager;


public class SettingsFragment extends BaseFragment {

    // Binding --------------------------------------------------------------------------------------

    FragmentSettingsBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    private SharedPreferencesManager sharedPreferencesManager;
    private SettingsViewModel settingsViewModel;
    private BottomSheet bottomSheet;



    // Metodos de ciclo de vida --------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        setInitValues();
        setListeners();
    }

    // Metodos privados de la clase ----------------------------------------------------------------
    private void setListeners(){
        binding.imgBack.setOnClickListener(new DebounceClickListener(v-> {

            NavHostFragment.findNavController(this).popBackStack();

        }));

        binding.llcontra.setOnClickListener(new DebounceClickListener(v->{
            navigateTo(binding.getRoot(), R.id.action_settingsFragment_to_changePasswordFragment, null);

        }));


        binding.llcerrar.setOnClickListener(new DebounceClickListener(v->{
            showCustomDialogMessage(
                    "¿Estás seguro que quieres cerrar sesión?",
                    "Estas cerrando sesión",
                    "Confirmar",
                    "Cancelar",
                    () -> {
                        // Cofirmar
                        sharedPreferencesManager.setIdUsuario("");
                        goToLandingFragment(binding.getRoot());

                    }
            );

        }));

        binding.lleliminar.setOnClickListener(new DebounceClickListener(v->{

            bottomSheet = new BottomSheet(
                    // Cancel
                    () -> {
                        clearBackStackTo(binding.getRoot(), R.id.videoFragment);
                    },
                    // Continue
                    () -> {
                        servicioEliminarCuenta();
                    },
                    "¿Estás seguro que quieres eliminar tu cuenta?",
                    "Tus videos y traducciones seran eliminados permanentemente",
                    "Regresar",
                    "Eliminar"

            );
            bottomSheet.show(getChildFragmentManager(), "myTokenBottomSheet");

        }));

        binding.llsobre.setOnClickListener(new DebounceClickListener(v->{
            navigateTo(binding.getRoot(), R.id.action_settingsFragment_to_aboutApp, null);

        }));

        binding.llreto.setOnClickListener(new DebounceClickListener(v->{
            binding.switchReto.setChecked(!binding.switchReto.isChecked());
            sharedPreferencesManager.setChallengeShow(binding.switchReto.isChecked());

        }));

        binding.llcamara.setOnClickListener(new DebounceClickListener(v->{
            binding.switchCamara.setChecked(!binding.switchCamara.isChecked());
            sharedPreferencesManager.setOpenCamera(binding.switchCamara.isChecked());

        }));

    }

    private void setInitValues(){
        binding.switchReto.setChecked(sharedPreferencesManager.isChallengeShow());
        binding.switchCamara.setChecked(sharedPreferencesManager.isOpenCamera());


        try {
            Context context = getContext();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String versionName = packageInfo.versionName;

            binding.tvVersion.setText("Versión " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



    }



    // Servicio ------------------------------------------------------------------------------------
    private void servicioEliminarCuenta() {
        showCustomDialogProgress(requireContext());

        String idUser = sharedPreferencesManager.getIdUsuario();
        settingsViewModel.deleteUser(idUser);
        settingsViewModel.getDeleteUserResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case SUCCESS:
                        hideCustomDialogProgress();

                        showCustomDialogMessage(
                                "Todos tus datos han sido borrados con éxito",
                                "Tu cuenta ha sido eliminada exitosamente",
                                "",
                                "Entiendo",
                                null,
                                () ->{
                                    goToLandingFragment(binding.getRoot());
                                },
                                ContextCompat.getColor(getContext(), com.components.R.color.base_red)
                        );


                        break;
                    case ERROR:
                        hideCustomDialogProgress();
                        showCustomDialogMessage(
                                resource.message,
                                "Oops algo salió mal",
                                "",
                                "Cerrar",
                                null,
                                ContextCompat.getColor(getContext(), com.components.R.color.base_red)
                        );
                   ;
                }
            }
        });
    }


}