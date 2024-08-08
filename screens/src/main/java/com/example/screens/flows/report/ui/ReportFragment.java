package com.example.screens.flows.report.ui;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.screens.R;
import com.example.screens.base.BaseFragment;
import com.example.screens.databinding.FragmentReportBinding;
import com.example.screens.databinding.FragmentVideoBinding;
import com.example.screens.flows.video.vm.VideoViewModel;

import android.media.MediaMetadataRetriever;
import android.graphics.Bitmap;

import java.io.IOException;
import java.util.ArrayList;

public class ReportFragment extends BaseFragment {

    // Binding --------------------------------------------------------------------------------------

    FragmentReportBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------

    private ThumbnailAdapter adapter;
    private String videoPath = "";
    private ArrayList<Bitmap> thumbnailImages = new ArrayList<>();
    private Bitmap selectedThumbnailBitmap;

    // Metodos de ciclo de vida --------------------------------------------------------------------


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);

        if (getArguments() != null && getArguments().containsKey("video_path")) {
            videoPath = getArguments().getString("video_path");
        }

        try {
            generateThumbnails();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setupThumbnailsView();
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

    private void setupThumbnailsView() {
        binding.thumbnailsView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new ThumbnailAdapter(thumbnailImages, this::seekToPosition);
        binding.thumbnailsView.setAdapter(adapter);
    }

    private void generateThumbnails() throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);  // Asegúrate de que 'videoPath' es la ruta correcta al archivo de vídeo

        String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long videoDuration = Long.parseLong(durationStr);  // Duración total del video en milisegundos

        if (videoDuration == 0) {
            Log.e("Thumbnail Generator", "Video duration is zero or unavailable.");
            return;
        }

        int numberOfThumbnails = 15;  // Número fijo de thumbnails
        long interval = videoDuration / (numberOfThumbnails - 1);  // Calcula el intervalo entre thumbnails

        for (int i = 0; i < numberOfThumbnails; i++) {
            long time = interval * i * 1000;  // Calcula el momento en microsegundos para el thumbnail
            Bitmap thumb = retriever.getFrameAtTime(time, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);  // Obtiene el frame en ese momento
            if (thumb != null) {
                thumbnailImages.add(thumb);  // Añade el thumbnail a la lista
            }
        }

        retriever.release();  // Libera el objeto retriever

        // Establecer la primera imagen si existe alguna
        if (!thumbnailImages.isEmpty()) {
            getActivity().runOnUiThread(() -> {
                seekToPosition(0);
            });
        }
    }




    private void seekToPosition(int position) {
        // No más búsqueda en VideoView, ahora establecer imagen en ImageView
        Bitmap selectedImage = thumbnailImages.get(position);
        binding.imagePreview.setImageBitmap(selectedImage);
        selectedThumbnailBitmap = selectedImage; // Guarda la imagen seleccionada
        checkAndToggleButtonState(); // Verifica el estado para habilitar/deshabilitar el botón
    }



    private void setListeners() {
        binding.imgClose2.setOnClickListener(v -> {
            clearBackStackTo(binding.getRoot(), R.id.videoFragment);
        });

        // Observar cambios en el EditText
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
    }

    private void setButtonResources(){
        binding.secondButton.setBackgroundDisable(ContextCompat.getDrawable(getContext(), com.example.components.R.drawable.light_red_transparent_button));
        binding.secondButton.setBackgroundEnable(ContextCompat.getDrawable(getContext(), com.example.components.R.drawable.transparente_red_button));
        binding.secondButton.setTextColorDisable(ContextCompat.getColor(getContext(), com.example.components.R.color.disable_red));
        binding.secondButton.setTextColorEnable(ContextCompat.getColor(getContext(), com.example.components.R.color.base_red));

    }


    private void checkAndToggleButtonState() {
        boolean isImageSelected = selectedThumbnailBitmap != null;
        boolean isTextNotEmpty = binding.tvReport.getText().toString().trim().length() > 0;

        binding.secondButton.setEnabled(isImageSelected && isTextNotEmpty);
    }


    // Servicios -----------------------------------------------------------------------------------

}
