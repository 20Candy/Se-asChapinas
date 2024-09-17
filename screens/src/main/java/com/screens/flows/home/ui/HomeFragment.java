package com.screens.flows.home.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.FileOutputOptions;
import androidx.camera.video.PendingRecording;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.components.buttons.DebounceClickListener;
import com.components.buttons.RecordButton;
import com.components.navMenu.BottomNavMenu;
import com.screens.R;
import com.screens.base.BaseFragment;
import com.screens.databinding.FragmentHomeBinding;
import com.screens.flows.home.vm.HomeViewModel;
import com.screens.utils.SharedPreferencesManager;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    //Binding --------------------------------------------------------------------------------------
    FragmentHomeBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------

    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private HomeViewModel homeViewModel;
    private SharedPreferencesManager sharedPreferencesManager;

    private boolean isUsingFrontCamera = false; // Comienza con la cámara trasera
    private String filepath = "";

    private VideoCapture videoCapture;
    private Recording recording;



    // Metodos de ciclo de vida --------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        Recorder recorder = new Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.FHD))
                .build();

        videoCapture = VideoCapture.withOutput(recorder);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBackPressed(() -> {});
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String todayDate = dateFormat.format(new Date());

        String lastShownDate = sharedPreferencesManager.getLastChallengeShowDate();

        if (!todayDate.equals(lastShownDate) && sharedPreferencesManager.isChallengeShow()) {
            Log.d("firstlogin", sharedPreferencesManager.isFirstLogin()+"");
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromHome", true);
            navigateTo(binding.getRoot(), R.id.action_homeFragment_to_challengeFragment2, bundle);

        }else{
            verifyAndHandlePermissions();
            setupListeners();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        HomeViewModel.setBottomNavVisible(true);
        if (hasAllPermissionsGranted() && binding.clCamera.getVisibility() == View.VISIBLE) {
            encenderCamara();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        apagarCamara(); // Libera los recursos de la cámara cuando el fragmento se pausa
    }

    private void apagarCamara() {
        if (videoCapture != null) {
            ProcessCameraProvider cameraProvider = getCameraProvider();
            if (cameraProvider != null) {
                cameraProvider.unbindAll(); // Desvincula todos los recursos de la cámara
            }
            videoCapture = null;
        }
    }

    private ProcessCameraProvider getCameraProvider() {
        try {
            return ProcessCameraProvider.getInstance(getContext()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }



    // Metodos privados de la clase ----------------------------------------------------------------
    private void setupListeners() {
        binding.btnEmpezar.setOnClickListener(new DebounceClickListener(this));

        // Configura el listener del RecordButton
        binding.btnRecord.setOnRecordListener(new RecordButton.OnRecordListener() {
            @Override
            public void onStartRecording() {
                startRecording();
                binding.btnFavorite.setVisibility(View.VISIBLE);
                binding.btnCamera.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopRecording() {
                stopRecording();
            }
        });

        binding.btnFavorite.setOnClickListener(v->{
            HomeViewModel.selectTab(BottomNavMenu.TAB_PROFILE);
        });

        binding.btnCamera.setOnClickListener(v->{
            flipCamera();
        });

    }

    private void verifyAndHandlePermissions() {
        // Es primer login
        if (sharedPreferencesManager.isFirstLogin()) {
            requestPermissionsIfNeeded();

        // No es primer login
        } else {
            //Usuario permite encender camara al abrir la app
            if (sharedPreferencesManager.isOpenCamera()) {
                // Tiene permisos abrir camara
                if (hasAllPermissionsGranted()) {
                    encenderCamara();
                // No tiene permisos muestra pantalla gris
                } else {
                    showGreyScreen(true);
                }
            // Usuario no permite encender camara al abrir la app
            } else {
                showGreyScreen(false);
            }
        }
    }


    private boolean hasAllPermissionsGranted() {
        return ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissionsIfNeeded() {
        requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    private void showGreyScreen(boolean permissionsNeeded) {
        binding.clEmpty.setVisibility(View.VISIBLE);
        binding.clCamera.setVisibility(View.GONE);
        binding.tvEmtpy.setText(permissionsNeeded ? R.string.permissions_alert : R.string.camera_alert); // Cambia mensaje
        binding.btnEmpezar.setText(permissionsNeeded ? getString(R.string.button_give_permissions) : getString(R.string.button_activate_camera));
    }

    // On Click ------------------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        if (view.getId() == com.components.R.id.btn_main) {
            // Si tiene permisos enciende camara
            if (hasAllPermissionsGranted()) {
                encenderCamara();
            // Si no tiene permisos pide
            } else {
                requestPermissionsIfNeeded();
            }

        }
    }

    // Permisos ------------------------------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Da permisos
                encenderCamara();
            } else {
                // Muestra pantalla gris nuevamente
                showGreyScreen(true);
            }
        }
    }


    // Camera --------------------------------------------------------------------------------------


    private void encenderCamara() {
        binding.clEmpty.setVisibility(View.GONE);
        binding.clCamera.setVisibility(View.VISIBLE);

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(binding.camera.getSurfaceProvider());

                CameraSelector cameraSelector = isUsingFrontCamera
                        ? CameraSelector.DEFAULT_FRONT_CAMERA
                        : CameraSelector.DEFAULT_BACK_CAMERA;

                // Asegúrate de crear un nuevo VideoCapture cada vez
                Recorder recorder = new Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.FHD))
                        .build();

                videoCapture = VideoCapture.withOutput(recorder);

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }


    private void startRecording() {
        if (videoCapture == null) {
            return;
        }

        File videoFile = new File(getContext().getExternalFilesDir(null), "video.mp4");
        filepath = videoFile.getAbsolutePath();
        FileOutputOptions outputOptions = new FileOutputOptions.Builder(videoFile).build();

        PendingRecording pendingRecording = ((Recorder) videoCapture.getOutput())
                .prepareRecording(getContext(), outputOptions);

        recording = pendingRecording.start(ContextCompat.getMainExecutor(getContext()), videoRecordEvent -> {
            if (videoRecordEvent instanceof VideoRecordEvent.Finalize) {
                // Verificar si hubo errores durante la grabación
                if (((VideoRecordEvent.Finalize) videoRecordEvent).getError() != VideoRecordEvent.Finalize.ERROR_NONE) {
                    Log.e("VideoFragment", "Error en la grabación del video: " + ((VideoRecordEvent.Finalize) videoRecordEvent).getError());
                } else {
                    // Confirmación de que el archivo está guardado y listo
                    hideCustomDialogProgress();
                    videoTraductionService(videoFile);

                }
            }
        });
    }


    private void stopRecording() {
        if (recording != null) {
            recording.stop();
            recording = null;
        }


    }

    private void flipCamera() {
        isUsingFrontCamera = !isUsingFrontCamera;
        encenderCamara(); // Reinitialize the camera with the new selector
    }


    // Servicios -----------------------------------------------------------------------------------
    private void videoTraductionService(File video) {
        showCustomDialogProgress(requireContext());

        sharedPreferencesManager = new SharedPreferencesManager(requireContext());


        homeViewModel.sendVideo(sharedPreferencesManager.getIdUsuario(), video);
        homeViewModel.getSendVideoResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case SUCCESS:
                        String lensegua= "";
                        String espanol = "";
                        String video_id = "";

                        if(resource.data != null){
                            if(resource.data.getTraduction_esp() != null){
                                espanol = resource.data.getTraduction_esp();
                            }
                            if(resource.data.getTraduction_lensegua() != null){
                                lensegua = resource.data.getTraduction_lensegua();
                            }
                            if(resource.data.getId_video() != null){
                                video_id = resource.data.getId_video();
                            }
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString("video_path", this.filepath);
                        bundle.putString("lensegua", lensegua);
                        bundle.putString("espanol", espanol);
                        bundle.putString("video_id", video_id);

                        navigateTo(binding.getRoot(), R.id.action_homeFragment_to_videoFragment, bundle);

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
                        break;

                }
            }
        });
    }






}
