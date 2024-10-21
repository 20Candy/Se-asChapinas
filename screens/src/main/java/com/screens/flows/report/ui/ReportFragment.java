package com.screens.flows.report.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.components.bottomsheet.BottomSheet;
import com.components.buttons.DebounceClickListener;
import com.screens.R;
import com.screens.base.BaseFragment;
import com.screens.databinding.FragmentReportBinding;
import com.screens.flows.login.vm.LoginViewModel;
import com.screens.flows.video.vm.VideoViewModel;
import com.screens.utils.SharedPreferencesManager;
import com.senaschapinas.flows.ReportVideo.ReportVideoRequest;

import android.media.MediaMetadataRetriever;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportFragment extends BaseFragment {

    // Binding --------------------------------------------------------------------------------------

    FragmentReportBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------

    private ThumbnailAdapter adapter;
    private String videoPath = "";
    private ArrayList<Bitmap> thumbnailImages = new ArrayList<>();
    private Bitmap selectedThumbnailBitmap;

    private BottomSheet bottomSheet;

    private ReportViewModel reportViewModel;

    SharedPreferencesManager sharedPreferencesManager;



    // Metodos de ciclo de vida --------------------------------------------------------------------


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportViewModel = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);

    }

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);

        if (getArguments() != null && getArguments().containsKey("video_path")) {
            videoPath = getArguments().getString("video_path");
        }

        generateThumbnails();

        setListeners();
        setButtonResources();
        checkAndToggleButtonState();

        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        VideoViewModel.setBottomNavVisible(false);

    }


    // Metodos privados de la clase ----------------------------------------------------------------

    private void setupThumbnailsView(ArrayList<Bitmap> thumbnailImages) {
        binding.thumbnailsView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new ThumbnailAdapter(thumbnailImages, this::seekToPosition);
        binding.thumbnailsView.setAdapter(adapter);
    }


    private void generateThumbnails() {

        showCustomDialogProgress(requireContext());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            ArrayList<Bitmap> thumbnails = new ArrayList<>();
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(videoPath);
                String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                if (durationStr == null) {
                    Log.e("Thumbnail Generator", "Duration string is null.");
                    return;
                }

                long videoDuration = Long.parseLong(durationStr);

                if (videoDuration == 0) {
                    Log.e("Thumbnail Generator", "Video duration is zero or unavailable.");
                    return;
                }

                int numberOfThumbnails = 15;
                long interval = videoDuration / (numberOfThumbnails - 1);

                for (int i = 0; i < numberOfThumbnails; i++) {
                    long time = interval * i * 1000;
                    Bitmap thumb = retriever.getFrameAtTime(time, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    if (thumb != null) {
                        thumbnails.add(thumb);
                    }
                }
            } catch (Exception e) {
                Log.e("Thumbnail Generator", "Error generating thumbnails: " + e.getMessage(), e);
            } finally {
                try {
                    retriever.release();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            handler.post(() -> {
                setupThumbnailsView(thumbnails);
                hideCustomDialogProgress();
                thumbnailImages = thumbnails;
                if (!thumbnailImages.isEmpty()) {
                    seekToPosition(0);
                }
            });
        });
    }



    private void seekToPosition(int position) {
        // No más búsqueda en VideoView, ahora establecer imagen en ImageView
        Bitmap selectedImage = thumbnailImages.get(position);
        binding.imagePreview.setImageBitmap(selectedImage);
        selectedThumbnailBitmap = selectedImage; // Guarda la imagen seleccionada
        checkAndToggleButtonState(); // Verifica el estado para habilitar/deshabilitar el botón
    }



    private void setListeners() {

        //Image close
        binding.imgClose2.setOnClickListener(v -> {
            clearBackStackTo(binding.getRoot(), R.id.videoFragment);
        });

        // Edittext
        binding.tvReport.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkAndToggleButtonState();  // Cada vez que cambie el texto, verifica el estado del botón
            }
        });

        // boton
        binding.secondButton.setOnClickListener(new DebounceClickListener( v->{

            bottomSheet = new BottomSheet(
                    // Cancel
                    () -> {
                        clearBackStackTo(binding.getRoot(), R.id.videoFragment);
                    },
                    // Continue
                    () -> {
                        servicioReporte();
                    },
                    ContextCompat.getString(getContext(), com.screens.R.string.bottomsheet_title),
                    ContextCompat.getString(getContext(), com.screens.R.string.bottomsheet_content),
                    ContextCompat.getString(getContext(), com.screens.R.string.bottomsheet_button1),
                    ContextCompat.getString(getContext(), com.screens.R.string.bottomsheet_button2)

            );
            bottomSheet.show(getChildFragmentManager(), "myTokenBottomSheet");
        }));
    }

    private void setButtonResources(){
        binding.secondButton.setBackgroundDisable(ContextCompat.getDrawable(getContext(), com.components.R.drawable.light_red_transparent_button));
        binding.secondButton.setBackgroundEnable(ContextCompat.getDrawable(getContext(), com.components.R.drawable.transparente_red_button));
        binding.secondButton.setTextColorDisable(ContextCompat.getColor(getContext(), com.components.R.color.disable_red));
        binding.secondButton.setTextColorEnable(ContextCompat.getColor(getContext(), com.components.R.color.base_red));

    }


    private void checkAndToggleButtonState() {
        boolean isImageSelected = selectedThumbnailBitmap != null;
        boolean isTextNotEmpty = binding.tvReport.getText().toString().trim().length() > 0;

        binding.secondButton.setEnabled(isImageSelected && isTextNotEmpty);
    }

    private String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream); // Comprimir la imagen a un formato JPEG con calidad del 80%
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }




    // Servicios -----------------------------------------------------------------------------------
    private void servicioReporte() {

        showCustomDialogProgress(requireContext());

        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        String idUser = sharedPreferencesManager.getIdUsuario();

        String reportImg = encodeBitmapToBase64(selectedThumbnailBitmap);

        ReportVideoRequest request = new ReportVideoRequest();
        request.setReportImg(reportImg);
        request.setReportMessage(binding.tvReport.getText().toString());
        request.setIdUser(idUser);
        request.setIdVideo("");

        reportViewModel.reportVideo(request);
        reportViewModel.getReportVideoResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case SUCCESS:
                        hideCustomDialogProgress();
                        showCustomDialogMessage(
                                "Tu reporte ha sido enviado exitosamente",
                                "¡Tu reporte nos ayuda a mejorar!",
                                "Regresar",
                                "",
                                () -> {
                                    clearBackStackTo(binding.getRoot(), R.id.videoFragment);
                                },
                                ContextCompat.getColor(getContext(), com.components.R.color.base_blue)
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
                        break;
                }
            }
        });
    }


}
