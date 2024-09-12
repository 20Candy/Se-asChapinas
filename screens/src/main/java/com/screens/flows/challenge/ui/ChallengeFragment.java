package com.screens.flows.challenge.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.components.buttons.DebounceClickListener;
import com.components.challenge.ChallengeCards;
import com.components.dictionary.CardData;
import com.screens.R;
import com.screens.base.BaseFragment;
import com.screens.databinding.FragmentChallengeBinding;
import com.screens.flows.challenge.vm.ChallengeViewModel;
import com.screens.flows.dictionary.vm.DictionaryViewModel;

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
        serviceChallengeScore();
        setListeners();

    }

    @Override
    public void onResume() {
        super.onResume();
        ChallengeViewModel.setBottomNavVisible(false);
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
            closeChallenge();
        }));

        binding.tvOmitir.setOnClickListener(new DebounceClickListener( v->{
            closeChallenge();
        }));

        binding.secondButton.setOnClickListener(new DebounceClickListener( v->{
            serviceChallengeComplete();
        }));
    }

    private void setCardsData() {
        List<CardData> cardDataList = dictionaryViewModel.createCardData(requireContext());
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
            // Convierte el nombre del recurso en un ID
            int resId = requireContext().getResources().getIdentifier(card.getImgLenseguaResId(), "drawable", requireContext().getPackageName());
            if (resId != 0) {
                resources.add(resId);
            }
        }

        // Establece los recursos en tu componente de vista personalizado
        binding.cards.setImageResources(resources);

        // Establece la imagen correcta en la vista para comparar en el juego
        int correctResId = requireContext().getResources().getIdentifier(correctCard.getImgLenseguaResId(), "drawable", requireContext().getPackageName());
        if (correctResId != 0) {
            binding.cards.setCorrectImageRes(correctResId);
        }

        // Establece la palabra correcta en el TextView
        binding.tvWord.setText('"' + correctCard.getTitle() + '"');

        // Inicia el juego
        binding.cards.startGame();


        // TODO OBTENER CHALLENGE SCORE
    }

    // Servicios -----------------------------------------------------------------------------------
    private void serviceChallengeScore(){
        //TODO

        //ON SUCESS
        binding.tvScore.setText("10");
        setCardsData();

    }

    private void serviceChallengeComplete(){
        //TODO

        //ON SUCESS
        closeChallenge();

    }

    private void closeChallenge(){
        if(getArguments()!=null && getArguments().getBoolean("fromHome") ){
            NavHostFragment.findNavController(this).navigate(R.id.homeFragment);
        }else{
            NavHostFragment.findNavController(this).popBackStack();

        }
    }

}