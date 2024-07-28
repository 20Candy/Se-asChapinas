package com.example.screens.flows.home.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.components.buttons.DebounceClickListener;
import com.example.components.buttons.RecordButton;
import com.example.screens.R;
import com.example.screens.base.BaseFragment;
import com.example.screens.databinding.FragmentHomeBinding;
import com.example.screens.flows.home.vm.HomeViewModel;
import com.example.screens.utils.SharedPreferencesManager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    //Binding --------------------------------------------------------------------------------------
    FragmentHomeBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------

    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private HomeViewModel homeViewModel;
    private SharedPreferencesManager sharedPreferencesManager;

    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;

    private boolean isRecording = false;
    private MediaRecorder mediaRecorder;
    private String cameraId = null;
    private boolean isUsingFrontCamera = false; // Comienza con la cámara trasera

    // Metodos de ciclo de vida --------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
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
        verifyAndHandlePermissions();
        setupListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
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
            // TODO NEVEGAR A FAVORITOS
        });

        binding.btnCamera.setOnClickListener(v->{
            flipCamera();
        });

    }

    private void verifyAndHandlePermissions() {
        // Es primer login
        if (sharedPreferencesManager.isFirstLogin()) {
            sharedPreferencesManager.setFirstLogin(false);  // Actualiza first login status
            requestPermissionsIfNeeded();

        // No es primer login
        } else {
            //Usuario permite encender camara al abrir la app
            if (homeViewModel.getEncenderCamara()) {
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
        if (view.getId() == com.example.components.R.id.btn_main) {
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
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Da permisos
                encenderCamara();
            } else {
                // Muestra pantalla gris nuevamente
                showGreyScreen(true);
            }
        }
    }


    // Camera --------------------------------------------------------------------------------------

    private void flipCamera() {
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }

        if (cameraCaptureSessions != null) {
            cameraCaptureSessions.close();
            cameraCaptureSessions = null;
        }

        // Determina si usas la cámara frontal o trasera
        isUsingFrontCamera = !isUsingFrontCamera;

        // Reabre la cámara con el nuevo ID
        openCamera(isUsingFrontCamera);
    }

    private void openCamera(boolean useFrontCamera) {
        CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String id : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(id);
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null) {
                    if ((useFrontCamera && facing == CameraCharacteristics.LENS_FACING_FRONT) ||
                            (!useFrontCamera && facing == CameraCharacteristics.LENS_FACING_BACK)) {
                        cameraId = id;
                        break;
                    }
                }
            }
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void encenderCamara() {
        binding.clEmpty.setVisibility(View.GONE);
        binding.clCamera.setVisibility(View.VISIBLE);

        CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Abrir la cámara
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = binding.camera.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if (null == cameraDevice) {
                        return;
                    }
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(getContext(), "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        if(null == cameraDevice) {
            return;
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void startRecording() {
        if (cameraDevice == null || !binding.camera.isAvailable()) return;

        try {
            setUpMediaRecorder();
            SurfaceTexture texture = binding.camera.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface previewSurface = new Surface(texture);
            Surface recordSurface = mediaRecorder.getSurface();

            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            captureRequestBuilder.addTarget(previewSurface);
            captureRequestBuilder.addTarget(recordSurface);

            cameraDevice.createCaptureSession(Arrays.asList(previewSurface, recordSurface),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            cameraCaptureSessions = session;
                            updatePreview();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isRecording = true;
                                    mediaRecorder.start();
                                }
                            });
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            isRecording = false;
        }

        videoTraductionService();

    }

    private void setUpMediaRecorder() throws IOException {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE); // Fuente de video
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // Formato de salida
        mediaRecorder.setOutputFile(getVideoFilePath(getContext())); // Archivo de salida
        mediaRecorder.setVideoEncodingBitRate(10000000); // Tasa de bits de video
        mediaRecorder.setVideoFrameRate(30); // Tasa de cuadros de video
        mediaRecorder.setVideoSize(imageDimension.getWidth(), imageDimension.getHeight()); // Tamaño de video
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264); // Codificador de video
        mediaRecorder.prepare(); // Preparar el MediaRecorder para la grabación
    }

    private String getVideoFilePath(Context context) {
        final File dir = context.getExternalFilesDir(null);
        return (dir == null ? "" : (dir.getAbsolutePath() + "/"))
                + System.currentTimeMillis() + ".mp4";
    }

    // Servicios -----------------------------------------------------------------------------------
    private void videoTraductionService(){
        // TODO IMPLEMENTAR SERVICIO Y LOADER

        navigateTo(binding.getRoot(), R.id.action_homeFragment_to_videoFragment, null);

    }





}
