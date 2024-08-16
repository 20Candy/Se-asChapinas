package com.example.screens.flows.translate.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.components.buttons.DebounceClickListener;
import com.example.components.navMenu.BottomNavMenu;
import com.example.screens.base.BaseFragment;
import com.example.screens.databinding.FragmentTranslateBinding;
import com.example.screens.flows.profile.vm.ProfileViewModel;
import com.example.screens.flows.translate.vm.TranslateViewModel;

import java.util.Locale;
import java.util.Objects;


public class TranslateFragment extends BaseFragment {

    //Binding --------------------------------------------------------------------------------------
    FragmentTranslateBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    private TranslateViewModel translateViewModel;
    private ProfileViewModel profileViewModel;
    private TextToSpeech textToSpeech;
    private boolean addFavorite = false;

    private int maxChar = 150;


    // Metodos de ciclo de vida --------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translateViewModel = new ViewModelProvider(requireActivity()).get(TranslateViewModel.class);
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
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
        initTextToSpeech();
        setupListeners();

        if(translateViewModel.getSelectedTrans().getValue()!=null){
            binding.tvTraduccionEspanol.setText(Objects.requireNonNull(translateViewModel.getSelectedTrans().getValue()).getTraduccionEspanol());
            binding.edLensegua.setText(Objects.requireNonNull(translateViewModel.getSelectedTrans().getValue()).getTraduccionLensegua());
            binding.imgHeart.setBackground(ContextCompat.getDrawable(getContext(), com.example.components.R.drawable.full_heart));
            addFavorite = true;

            setEstadoTraducido(true);

            translateViewModel.selectTrans(null);
        }
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
    private void setupListeners(){

        // Favorite button
        binding.smallButton.setOnClickListener( new DebounceClickListener( v ->{
            profileViewModel.setShowVideoFavorite(false);
            ProfileViewModel.selectTab(BottomNavMenu.TAB_PROFILE);

        }));

        // Copy button
        binding.imgCopy.setOnClickListener(new DebounceClickListener(v ->{
            binding.imgCopy.setBackground(ContextCompat.getDrawable(getContext(), com.example.components.R.drawable.copy_full));

            new Handler().postDelayed(() -> {
                // Restaura el fondo original
                binding.imgCopy.setBackground(ContextCompat.getDrawable(getContext(), com.example.components.R.drawable.copy));
            }, 2000); // 2000 milisegundos = 2 segundos

            String textToCopy = (String) binding.tvTraduccionEspanol.getText();
            ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Texto copiado", textToCopy);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(v.getContext(), "Texto copiado al portapapeles", Toast.LENGTH_SHORT).show();
        }));

        // Speaker button
        binding.imgSpeaker.setOnClickListener(new DebounceClickListener(v ->{
            Bundle params = new Bundle();
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
            textToSpeech.speak((String) binding.tvTraduccionEspanol.getText(), TextToSpeech.QUEUE_FLUSH, params, "UniqueID");
            binding.imgSpeaker.setBackground(ContextCompat.getDrawable(getContext(), com.example.components.R.drawable.speaker_full));

        }));

        // Heart button
        binding.imgHeart.setOnClickListener(new DebounceClickListener(v ->{
            addFavorite = !addFavorite;

            if (addFavorite) {
                servicioAgregarFavorito();
                binding.imgHeart.setBackground(ContextCompat.getDrawable(getContext(), com.example.components.R.drawable.full_heart));
            } else {
                servicioEliminarFavorito();
                binding.imgHeart.setBackground(ContextCompat.getDrawable(getContext(), com.example.components.R.drawable.heart));
            }
        }));

        // Traduction button
        binding.smallTransparentButton2.setOnClickListener(new DebounceClickListener(v ->{
            // Traducir
            if(binding.tvTraduccionEspanol.getText().length() <=0){
                servicioTraducir();

            // Nueva traduccion
            }else{
                setEstadoTraducido(false);
            }


        }));

        // Edittext
        binding.edLensegua.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filtrar caracteres no deseados
                String filteredText = s.toString().replaceAll("[^a-zA-Z0-9 ]", "");

                // Si el texto filtrado es diferente al texto original, actualiza el EditText
                if (!filteredText.equals(s.toString())) {
                    binding.edLensegua.setText(filteredText);
                    binding.edLensegua.setSelection(filteredText.length()); // Mueve el cursor al final
                }

                // Verificar el estado del botón
                verificarBoton(filteredText.length() <= 0);

                // Actualizar tvLimit
                int remainingChars = maxChar - filteredText.length();
                binding.tvLimit.setText(String.valueOf(remainingChars));

                // Si se excede el límite, corta el texto
                if (filteredText.length() > maxChar) {
                    String truncatedText = filteredText.substring(0, maxChar);
                    binding.edLensegua.setText(truncatedText);
                    binding.edLensegua.setSelection(maxChar); // Mueve el cursor al final
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
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

    private void verificarBoton(boolean enable){
        binding.smallTransparentButton2.setEnabled(!enable);

    }

    private void setEstadoTraducido(Boolean traducido){
        // Traduccion hecha

        if(traducido){
            binding.smallButton.setVisibility(View.GONE);
            binding.tvLimit.setVisibility(View.GONE);
            binding.smallTransparentButton2.setText("Nueva Traducción");
            binding.llOptions.setVisibility(View.VISIBLE);
            binding.tvTitleEspanol.setVisibility(View.VISIBLE);
            binding.tvTraduccionEspanol.setVisibility(View.VISIBLE);
            binding.edLensegua.setEnabled(false);

        // Nueva traduccion
        }else{
            binding.smallButton.setVisibility(View.VISIBLE);
            binding.tvLimit.setVisibility(View.VISIBLE);
            binding.tvLimit.setText(String.valueOf(maxChar));
            binding.smallTransparentButton2.setText("Traducir");
            binding.llOptions.setVisibility(View.GONE);
            binding.tvTitleEspanol.setVisibility(View.GONE);
            binding.tvTraduccionEspanol.setVisibility(View.GONE);
            binding.tvTraduccionEspanol.setText("");
            binding.edLensegua.setText("");
            binding.edLensegua.setEnabled(true);
            binding.imgHeart.setBackground(ContextCompat.getDrawable(getContext(), com.example.components.R.drawable.heart));
            addFavorite = false;


        }
    }

    private void changeButtonBackgroundWithAnimation(View view, int newBackgroundResource) {
        // Animación de desvanecimiento para el fondo actual
        view.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction(() -> {
                    // Cambia el fondo cuando la animación termina
                    view.setBackground(ContextCompat.getDrawable(getContext(), newBackgroundResource));
                    // Animación de desvanecimiento para el nuevo fondo
                    view.animate()
                            .alpha(1f)
                            .setDuration(200)
                            .start();
                })
                .start();
    }


    // Servicios -----------------------------------------------------------------------------------
    private void servicioAgregarFavorito() {
        // TODO
    }

    private void servicioEliminarFavorito() {
        // TODO
    }

    private void servicioTraducir() {

        // TODO
        // ON SUCESS
        String traduccion = "Hoy voy a la universidad";
        binding.tvTraduccionEspanol.setText(traduccion);
        setEstadoTraducido(true);
    }

}