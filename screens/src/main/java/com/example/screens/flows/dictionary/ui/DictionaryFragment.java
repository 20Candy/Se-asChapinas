package com.example.screens.flows.dictionary.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.components.cardDictionary.CardData;
import com.example.components.cardDictionary.CardDictionary;
import com.example.screens.base.BaseFragment;
import com.example.screens.databinding.FragmentDictionaryBinding;
import com.example.screens.flows.dictionary.vm.DictionaryViewModel;
import com.example.screens.flows.video.vm.VideoViewModel;

import java.util.ArrayList;
import java.util.List;


public class DictionaryFragment extends BaseFragment {

    //Binding --------------------------------------------------------------------------------------
    FragmentDictionaryBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    private DictionaryViewModel dictionaryViewModel;

    DictionaryCardAdapter adapter;


    // Metodos de ciclo de vida --------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dictionaryViewModel = new ViewModelProvider(requireActivity()).get(DictionaryViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDictionaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBackPressed(() -> {});
        setupRecyclerView();
        setListeners();
    }

    public void onResume() {
        super.onResume();
        VideoViewModel.setBottomNavVisible(true);

    }

    // Metodos privados de la clase ----------------------------------------------------------------
    private void setupRecyclerView() {
        List<CardData> cardDataList = createCardData();

        binding.rvCards.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new DictionaryCardAdapter(getActivity(), cardDataList);
        binding.rvCards.setAdapter(adapter);
    }


    private List<CardData> createCardData() {
        List<CardData> cardDataList = new ArrayList<>();
        cardDataList.add(new CardData("A", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        cardDataList.add(new CardData("Ab", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        cardDataList.add(new CardData("B", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        cardDataList.add(new CardData("C", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        cardDataList.add(new CardData("D", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        cardDataList.add(new CardData("E", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        cardDataList.add(new CardData("F", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        cardDataList.add(new CardData("G", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        cardDataList.add(new CardData("H", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        cardDataList.add(new CardData("I", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        cardDataList.add(new CardData("J", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        cardDataList.add(new CardData("K", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        cardDataList.add(new CardData("L", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        return cardDataList;
    }

    private void setListeners(){

        adapter.setOnHeartClickListener(new DictionaryCardAdapter.OnHeartClickListener() {
            @Override
            public void onHeartClicked(boolean heartFull, CardData cardData) {
                // Agregar a favoritos
                if (heartFull) {
                    servicioAgregarFavorito();
                // Borrar de favoritos
                } else {
                    servicioEliminarFavorito();
                }
            }
        });

    }

    // Servicios -----------------------------------------------------------------------------------
    private void servicioAgregarFavorito() {
        // TODO
    }

    private void servicioEliminarFavorito() {
        // TODO
    }


}