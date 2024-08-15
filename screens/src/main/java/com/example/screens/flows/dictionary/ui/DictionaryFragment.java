package com.example.screens.flows.dictionary.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.components.dictionary.CardData;
import com.example.screens.base.BaseFragment;
import com.example.screens.databinding.FragmentDictionaryBinding;
import com.example.screens.flows.dictionary.vm.DictionaryViewModel;
import com.example.screens.flows.video.vm.VideoViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class DictionaryFragment extends BaseFragment {

    //Binding --------------------------------------------------------------------------------------
    FragmentDictionaryBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    private DictionaryViewModel dictionaryViewModel;

    DictionaryCardAdapter adapter;
    CategoriesAdapter categoriesAdapter;


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
        setupRecyclerView(dictionaryViewModel.createCardData());
        setupRecyclerViewCategories(createCategories());
        setListeners();
    }

    public void onResume() {
        super.onResume();
        VideoViewModel.setBottomNavVisible(true);

    }

    // Metodos privados de la clase ----------------------------------------------------------------
    private void setupRecyclerView(List<CardData> list) {

        binding.rvCards.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DictionaryCardAdapter(getActivity(), list);
        binding.rvCards.setAdapter(adapter);
    }

    private void setupRecyclerViewCategories(List<String> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvCategories.setLayoutManager(layoutManager);

        categoriesAdapter = new CategoriesAdapter(getActivity(), list, (isFavorite, category) -> {
            if (isFavorite) {
                servicioFavoritosDiccionario();
            } else {
                filtrarLista(category, "categoria");
            }
        });
        binding.rvCategories.setAdapter(categoriesAdapter);
    }




    private List<String> createCategories() {
        return new ArrayList<>(Arrays.asList("Favoritos", "Acciones", "Lugares", "Estados", "Tiempo", "Pronombres", "Preguntas"));
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


        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarLista(newText, "titulo");
                return false;
            }
        });


    }

    private void filtrarLista(String filtro, String tipo) {
        List<CardData> listaFiltrada = new ArrayList<>();
        List<CardData> listaOriginal = dictionaryViewModel.createCardData();

        if (filtro != null && !filtro.isEmpty()) {
            listaFiltrada = listaOriginal.stream()
                    .filter(card -> {
                        if ("titulo".equalsIgnoreCase(tipo)) {
                            return card.getTitle() != null && card.getTitle().toLowerCase().contains(filtro.toLowerCase());
                        } else if ("categoria".equalsIgnoreCase(tipo)) {
                            return card.getCategoria() != null && card.getCategoria().toLowerCase().contains(filtro.toLowerCase());
                        }
                        return false; // Tipo no reconocido
                    })
                    .collect(Collectors.toList());
            showEmptyState(listaFiltrada.isEmpty());
            setupRecyclerView(listaFiltrada);
        } else {
            // Si el filtro es nulo o vacío, mostrar la lista original
            showEmptyState(false);
            setupRecyclerView(listaOriginal);
        }
    }


    private void showEmptyState(Boolean show){
        if(show){
            binding.clEmpty.setVisibility(View.VISIBLE);
            binding.rvCards.setVisibility(View.GONE);
        }else{
            binding.clEmpty.setVisibility(View.GONE);
            binding.rvCards.setVisibility(View.VISIBLE);
        }

    }


    // Servicios -----------------------------------------------------------------------------------
    private void servicioAgregarFavorito() {
        // TODO
    }

    private void servicioEliminarFavorito() {
        // TODO
    }

    private void servicioFavoritosDiccionario(){
        // TODO
    }


}