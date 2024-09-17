package com.screens.flows.video.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.media.MediaMetadataRetriever;
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

import com.components.bottomsheet.BottomSheet;
import com.components.navMenu.BottomNavMenu;
import com.screens.R;
import com.screens.base.BaseFragment;
import com.screens.databinding.FragmentVideoBinding;
import com.screens.flows.video.vm.VideoViewModel;
import com.screens.utils.SharedPreferencesManager;
import com.senaschapinas.flows.RemoveVideo.RemoveVideoRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private BottomSheet bottomSheet;

    private String videoPath = "";

    private String id_video = "";
    private SharedPreferencesManager sharedPreferencesManager;



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
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        releaseVideoView();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        releaseVideoView();

    }


    // Metodos privados de la clase ----------------------------------------------------------------
    private void setVideoPlayer() {
        if (getArguments() != null && getArguments().containsKey("video_path")) {
            binding.videoView.getHolder().setFormat(PixelFormat.UNKNOWN); // Resetear la superficie


            videoPath = getArguments().getString("video_path");
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
        if (getArguments() != null && getArguments().containsKey("favorito")) {
            addFavorite = true;
            binding.imgHeart.setBackground(ContextCompat.getDrawable(getContext(), com.components.R.drawable.full_heart));
        }
        if(getArguments() != null && getArguments().containsKey("id_video")){
            this.id_video = getArguments().getString("id_video");
        }
    }

    private void releaseVideoView() {
        if (binding.videoView != null) {
            binding.videoView.stopPlayback();
            binding.videoView.setMediaController(null);
            binding.videoView.setOnPreparedListener(null);
            binding.videoView.setOnCompletionListener(null);
            binding.videoView.setOnErrorListener(null);
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
                try {
                    obtenerImagen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                binding.imgHeart.setBackground(ContextCompat.getDrawable(getContext(), com.components.R.drawable.full_heart));
            } else {
                servicioEliminarFavorito();
                binding.imgHeart.setBackground(ContextCompat.getDrawable(getContext(), com.components.R.drawable.heart));
            }
        });

        binding.imgSpeaker.setOnClickListener(view -> {
            Bundle params = new Bundle();
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
            textToSpeech.speak(this.espanol, TextToSpeech.QUEUE_FLUSH, params, "UniqueID");
            binding.imgSpeaker.setBackground(ContextCompat.getDrawable(getContext(), com.components.R.drawable.speaker_full));

        });

        binding.imgShare.setOnClickListener(view -> {
            compartirVideo();
        });

        binding.imgReport.setOnClickListener(view -> {
            bottomSheet = new BottomSheet(
                    // Cancel action
                    () -> VideoViewModel.selectTab(BottomNavMenu.TAB_DICTIONARY),
                    // Continue action
                    () -> {
                        Bundle bundle = new Bundle();
                        bundle.putString("video_path", videoPath);
                        navigateTo(binding.getRoot(), R.id.action_videoFragment_to_reportFragment, bundle);
                    },
                    ContextCompat.getDrawable(getContext(), com.components.R.drawable.search_white)
            );
            bottomSheet.show(getChildFragmentManager(), "myTokenBottomSheet");
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
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            binding.imgSpeaker.setBackground(ContextCompat.getDrawable(getContext(), com.components.R.drawable.speaker));
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

    private void compartirVideo() {

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


    private void obtenerImagen() throws IOException {
        File imagenFile = capturarFotogramaComoArchivo();
        if (imagenFile != null) {
            servicioAgregarFavorito(imagenFile);
        } else {
            Toast.makeText(getContext(), "Error al capturar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private File capturarFotogramaComoArchivo() throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        Bitmap fotograma = retriever.getFrameAtTime(binding.videoView.getCurrentPosition() * 1000); // Fotograma en el momento actual del video
        retriever.release();

        if (fotograma == null) {
            return null;
        }

        // Crear un archivo temporal para guardar la imagen
        File imagenFile = new File(requireContext().getCacheDir(), "video_frame.png");
        try (FileOutputStream fos = new FileOutputStream(imagenFile)) {
            fotograma.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return imagenFile;
        } catch (IOException e) {
            Log.e("VideoFragment", "Error al guardar la imagen: " + e.getMessage());
            return null;
        }
    }


    // Servicios -----------------------------------------------------------------------------------
    private void servicioAgregarFavorito(File imagen) {
        showCustomDialogProgress(requireContext());


        videoViewModel.favVideo(sharedPreferencesManager.getIdUsuario(),this.id_video,imagen);

        videoViewModel.getFavVideoResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case SUCCESS:
                        hideCustomDialogProgress();
                        if (imagen.exists()) {
                            boolean deleted = imagen.delete();
                            if (!deleted) {
                                Log.e("VideoFragment", "No se pudo borrar el archivo temporal");
                            }
                        }
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

    private void servicioEliminarFavorito() {
        showCustomDialogProgress(requireContext());

        RemoveVideoRequest request = new RemoveVideoRequest();
        request.setId_video(this.id_video);
        request.setId_user(sharedPreferencesManager.getIdUsuario());

        videoViewModel.removeVideo(request);
        videoViewModel.getRemoveVideoResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case SUCCESS:
                        hideCustomDialogProgress();

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
