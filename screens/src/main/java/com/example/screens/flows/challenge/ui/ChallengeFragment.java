package com.example.screens.flows.challenge.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.components.buttons.DebounceClickListener;
import com.example.components.challenge.ChallengeCards;
import com.example.components.dictionary.CardData;
import com.example.screens.R;
import com.example.screens.base.BaseFragment;
import com.example.screens.databinding.FragmentChallengeBinding;
import com.example.screens.databinding.FragmentHomeBinding;
import com.example.screens.flows.dictionary.vm.DictionaryViewModel;
import com.example.screens.utils.SharedPreferencesManager;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.bumptech.glide.Glide;


public class ChallengeFragment extends BaseFragment {

    //Binding --------------------------------------------------------------------------------------
    FragmentChallengeBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------

    private DictionaryViewModel dictionaryViewModel;

    // Metodos de ciclo de vida --------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dictionaryViewModel = new ViewModelProvider(requireActivity()).get(DictionaryViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChallengeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCardsData();
        setListeners();
    }

    // Metodos privados de la clase ----------------------------------------------------------------

    private void setListeners(){
        binding.cards.setCallback(new ChallengeCards.Callback() {
            @Override
            public void onCorrectAnswer() {
                // Manejar respuesta correcta
                binding.imgGif.setVisibility(View.VISIBLE);

                Glide.with(requireActivity())
                        .load(requireActivity().getResources().getDrawable(R.drawable.confim))
                        .into(binding.imgGif);

                binding.imgBack.setVisibility(View.INVISIBLE);
                binding.tvOmitir.setVisibility(View.GONE);
                binding.secondButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onIncorrectAnswer() {
                // Manejar respuesta incorrecta
            }
        });

        binding.imgBack.setOnClickListener(new DebounceClickListener( v->{
            NavHostFragment.findNavController(this).popBackStack();
        }));

        binding.tvOmitir.setOnClickListener(new DebounceClickListener( v->{
            NavHostFragment.findNavController(this).popBackStack();
        }));

        binding.secondButton.setOnClickListener(new DebounceClickListener( v->{
            serviceChallengeComplete();
        }));
    }

    private void setCardsData() {
        List<CardData> cardDataList = dictionaryViewModel.createCardData();
        Random random = new Random();

        // Selecciona aleatoriamente una tarjeta correcta
        CardData correctCard = cardDataList.get(random.nextInt(cardDataList.size()));

        // Crea una lista para almacenar las opciones incorrectas
        List<CardData> incorrectCards = new ArrayList<>(cardDataList);
        incorrectCards.remove(correctCard);

        // Mezcla y selecciona 3 opciones incorrectas
        Collections.shuffle(incorrectCards);
        List<CardData> selectedIncorrectCards = incorrectCards.subList(0, 3);

        // Combina la opción correcta con las incorrectas
        List<CardData> options = new ArrayList<>(selectedIncorrectCards);
        options.add(correctCard);

        // Mezcla la lista final de opciones para que la correcta no esté siempre en la misma posición
        Collections.shuffle(options);

        // Configura las imágenes en la vista
        List<Integer> resources = new ArrayList<>();
        for (CardData card : options) {
            resources.add(card.getImgLenseguaResId()); // Asegúrate que CardData tiene un método getImageResId()
        }

        binding.cards.setImageResources(resources);

        // Establece la imagen correcta en la vista para comparar en el juego
        binding.cards.setCorrectImageRes(correctCard.getImgLenseguaResId());

        // Establece la palabra correcta en el TextView
        binding.tvWord.setText('"' + correctCard.getTitle() +'"');

        binding.cards.startGame();

        // TODO OBTENER CHALLENGE SCORE
    }

    // Servicios -----------------------------------------------------------------------------------
    private void serviceChallengeComplete(){
        NavHostFragment.findNavController(this).popBackStack();

    }

}