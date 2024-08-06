package com.example.screens.flows.video.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.screens.R;
import com.example.screens.base.BaseFragment;
import com.example.screens.databinding.FragmentVideoBinding;
import com.example.screens.flows.video.vm.VideoViewModel;

import java.io.File;
import java.util.Locale;

public class VideoFragment extends BaseFragment {

    // Binding --------------------------------------------------------------------------------------
    FragmentVideoBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    VideoViewModel videoViewModel;
    private boolean addFavorite = false;
    private String lensegua = "";
    private String espanol = "";

    private TextToSpeech textToSpeech;

    // Metodos de ciclo de vida --------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoViewModel = new ViewModelProvider(requireActivity()).get(VideoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVideoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBackPressed(() -> {});
        initTextToSpeech();
        setVideoPlayer();
        setTraductions();
        setListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        VideoViewModel.setBottomNavVisible(false);
        resumeVideo();

    }

    @Override
    public void onPause() {
        super.onPause();
        pauseVideo();
        binding.videoView.setMediaController(null); // Ocultar el MediaController cuando el video está en pausa

    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    // Metodos privados de la clase ----------------------------------------------------------------
    private void setVideoPlayer() {
        if (getArguments() != null && getArguments().containsKey("video_path")) {
            String videoPath = getArguments().getString("video_path");
            binding.videoView.setVideoURI(Uri.parse(videoPath));

            // Configura el video player sin MediaController
            MediaController mediaController = new MediaController(getContext()) {
                @Override
                public void show() {
                    // Sobreescribe este método para evitar que se muestren los controles
                }

                @Override
                public void hide() {
                    // Sobreescribe este método para evitar que se oculten los controles
                }
            };

            binding.videoView.setMediaController(mediaController);
            binding.videoView.requestFocus();
            binding.videoView.start();
        }
    }


    private void setTraductions() {
        if (getArguments() != null && getArguments().containsKey("lensegua")) {
            this.lensegua = getArguments().getString("lensegua");
            binding.tvLensegua.setText(this.lensegua);
        }
        if (getArguments() != null && getArguments().containsKey("espanol")) {
            this.espanol = getArguments().getString("espanol");
            binding.tvEspanol.setText(this.espanol);
        }
    }

    private void setListeners() {
        binding.imgClose.setOnClickListener(view -> {
            borrarVideo();
            clearBackStackTo(binding.getRoot(), R.id.homeFragment);
        });

        binding.imgHeart.setOnClickListener(view -> {
            addFavorite = !addFavorite;

            if (addFavorite) {
                servicioAgregarFavorito();
                binding.imgHeart.setBackground(ContextCompat.getDrawable(getContext(), com.example.components.R.drawable.full_heart));
            } else {
                servicioEliminarFavorito();
                binding.imgHeart.setBackground(ContextCompat.getDrawable(getContext(), com.example.components.R.drawable.heart));
            }
        });

        binding.imgSpeaker.setOnClickListener(view -> {
            Bundle params = new Bundle();
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
            textToSpeech.speak(this.espanol, TextToSpeech.QUEUE_FLUSH, params, "UniqueID");
        });

        binding.imgShare.setOnClickListener(view -> {
            compartirVideo();
        });
    }

    private void initTextToSpeech() {
        textToSpeech = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(new Locale("es", "MX"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(getContext(), "Error al reproducir texto", Toast.LENGTH_SHORT).show();
                } else {
                    textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            binding.imgSpeaker.setBackground(ContextCompat.getDrawable(getContext(), com.example.components.R.drawable.speaker_full));
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            binding.imgSpeaker.setBackground(ContextCompat.getDrawable(getContext(), com.example.components.R.drawable.speaker));
                        }

                        @Override
                        public void onError(String utteranceId) {
                            Toast.makeText(getContext(), "Error al reproducir texto", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String utteranceId, int errorCode) {
                            Toast.makeText(getContext(), "Error al reproducir texto", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(getContext(), "Error al reproducir texto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void borrarVideo() {
        if (getArguments() != null && getArguments().containsKey("video_path")) {
            String videoPath = getArguments().getString("video_path");
            File videoFile = new File(videoPath);

            if (videoFile.exists()) {
                if (videoFile.delete()) {
                    Log.d("VideoFragment", "Video borrado exitosamente de la caché.");
                } else {
                    Log.d("VideoFragment", "Error al borrar el video de la caché.");
                }
            } else {
                Log.d("VideoFragment", "Archivo no encontrado en la caché.");
            }
        }
    }

    private void compartirVideo() {
        if (getArguments() != null && getArguments().containsKey("video_path")) {
            String videoPath = getArguments().getString("video_path");
            File videoFile = new File(videoPath);
            Uri videoUri = FileProvider.getUriForFile(
                    getContext(),
                    getContext().getPackageName() + ".fileprovider",
                    videoFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("video/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, this.espanol);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Verificamos si hay aplicaciones que puedan manejar el intento
            if (shareIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(Intent.createChooser(shareIntent, "Compartir video"));
            } else {
                Toast.makeText(getContext(), "No hay aplicaciones disponibles para compartir el video", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "No se pudo obtener la ruta del video", Toast.LENGTH_SHORT).show();
        }
    }

    private void resumeVideo() {
        if (binding.videoView != null && !binding.videoView.isPlaying()) {
            binding.videoView.start();
        }
    }

    private void pauseVideo() {
        if (binding.videoView != null && binding.videoView.isPlaying()) {
            binding.videoView.pause();
        }
    }

    // Servicios -----------------------------------------------------------------------------------
    private void servicioAgregarFavorito() {
        // TODO
    }

    private void servicioEliminarFavorito() {
        // TODO
    }
}
