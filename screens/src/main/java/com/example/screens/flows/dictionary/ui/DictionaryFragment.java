package com.example.screens.flows.dictionary.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.components.navMenu.BottomNavMenu;
import com.example.screens.R;
import com.example.screens.base.BaseFragment;
import com.example.screens.databinding.FragmentDictionaryBinding;
import com.example.screens.databinding.FragmentHomeBinding;
import com.example.screens.flows.dictionary.vm.DictionaryViewModel;
import com.example.screens.flows.home.vm.HomeViewModel;
import com.example.screens.flows.video.vm.VideoViewModel;
import com.example.screens.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;


public class DictionaryFragment extends BaseFragment {

    //Binding --------------------------------------------------------------------------------------
    FragmentDictionaryBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    private DictionaryViewModel dictionaryViewModel;


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
    }

    public void onResume() {
        super.onResume();
        VideoViewModel.setBottomNavVisible(true);

    }

    // Metodos privados de la clase ----------------------------------------------------------------
    private void setupRecyclerView() {
        List<CardData> cardDataList = createCardData();

        DictionaryCardAdapter adapter = new DictionaryCardAdapter(getActivity(), cardDataList);
        binding.rvCards.setAdapter(adapter);
        binding.rvCards.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    private List<CardData> createCardData() {
        List<CardData> cardDataList = new ArrayList<>();
        cardDataList.add(new CardData("Title 1", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        cardDataList.add(new CardData("Title 2", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        cardDataList.add(new CardData("Title 3", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua));
        return cardDataList;
    }


}