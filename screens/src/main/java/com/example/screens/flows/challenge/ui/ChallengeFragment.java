package com.example.screens.flows.challenge.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.components.challenge.ChallengeCards;
import com.example.screens.R;
import com.example.screens.base.BaseFragment;
import com.example.screens.databinding.FragmentChallengeBinding;
import com.example.screens.databinding.FragmentHomeBinding;
import com.example.screens.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class ChallengeFragment extends BaseFragment {

    //Binding --------------------------------------------------------------------------------------
    FragmentChallengeBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------

    // Metodos de ciclo de vida --------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            }

            @Override
            public void onIncorrectAnswer() {
                // Manejar respuesta incorrecta
            }
        });
    }

    private void setCardsData(){

        List<Integer> resources = new ArrayList<>();
        // Agrega los recursos de imagen aquí, ejemplo:
        resources.add(com.example.components.R.drawable.ayer_lensegua);
        resources.add(com.example.components.R.drawable.agua_lensegua);
        resources.add(com.example.components.R.drawable.que_lensegua);
        resources.add(com.example.components.R.drawable.cuando_lensegua);

        binding.cards.setImageResources(resources);
        binding.cards.setCorrectImageRes(com.example.components.R.drawable.cuando_lensegua);
        binding.cards.startGame();
    }

}