package com.screens.flows.dictionary.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.components.dictionary.CardData;
import com.screens.base.BaseFragment;
import com.screens.databinding.FragmentDictionaryBinding;
import com.screens.flows.dictionary.vm.DictionaryViewModel;
import com.screens.flows.video.vm.VideoViewModel;
import com.screens.utils.SharedPreferencesManager;
import com.senaschapinas.flows.AddDictionary.AddDictionaryRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class DictionaryFragment extends BaseFragment {

    //Binding --------------------------------------------------------------------------------------
    FragmentDictionaryBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    private DictionaryViewModel dictionaryViewModel;

    DictionaryCardAdapter adapter;
    CategoriesAdapter categoriesAdapter;

    private String selectedCategory = null;

    SharedPreferencesManager sharedPreferencesManager;



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
        onBackPressed(() -> {
        });
        setupRecyclerView(dictionaryViewModel.createCardData(requireContext()));
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
                // Si la categoría ya está seleccionada, quitar el filtro
                if (category.equals(selectedCategory)) {
                    selectedCategory = null; // Resetear la categoría seleccionada
                    setupRecyclerView(dictionaryViewModel.createCardData(requireContext())); // Mostrar toda la lista
                } else {
                    selectedCategory = category; // Establecer la nueva categoría seleccionada
                    filtrarLista(category, "categoria"); // Aplicar el filtro de categoría
                }
            }
        });

        binding.rvCategories.setAdapter(categoriesAdapter);
    }


    private List<String> createCategories() {
        return new ArrayList<>(Arrays.asList("Favoritos", "Acciones", "Lugares", "Estados", "Tiempo", "Personas", "Preguntas", "Objetos"));
    }


    private void setListeners() {

        adapter.setOnHeartClickListener(new DictionaryCardAdapter.OnHeartClickListener() {
            @Override
            public void onHeartClicked(boolean heartFull, CardData cardData) {
                // Agregar a favoritos
                if (heartFull) {
                    servicioAgregarFavorito(cardData.getTitle());
                    // Borrar de favoritos
                } else {
                    servicioEliminarFavorito(cardData.getTitle());
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
        List<CardData> listaOriginal = dictionaryViewModel.createCardData(requireContext());

        if (filtro != null && !filtro.isEmpty()) {
            listaFiltrada = listaOriginal.stream()
                    .filter(card -> {
                        if ("titulo".equalsIgnoreCase(tipo)) {
                            return card.getTitle() != null && card.getTitle().toLowerCase().contains(filtro.toLowerCase());
                        } else if ("categoria".equalsIgnoreCase(tipo)) {
                            return card.getCategoria() != null && card.getCategoria().toLowerCase().contains(filtro.toLowerCase());
                        }
                        // Devuelve false si el tipo no coincide con "titulo" o "categoria"
                        return false;
                    })
                    .collect(Collectors.toList());

            // Mostrar estado vacío si la lista filtrada está vacía
            showEmptyState(listaFiltrada.isEmpty(), "Palabra no disponible\n ¡Seguimos creciendo!");
            setupRecyclerView(listaFiltrada);
        } else {
            // Si el filtro es nulo o vacío, mostrar la lista original
            showEmptyState(false, "");
            setupRecyclerView(listaOriginal);
        }
    }


    private void showEmptyState(Boolean show, String text) {
        if (show) {
            binding.tvEmpty.setText(text);
            binding.clEmpty.setVisibility(View.VISIBLE);
            binding.rvCards.setVisibility(View.GONE);
        } else {
            binding.clEmpty.setVisibility(View.GONE);
            binding.rvCards.setVisibility(View.VISIBLE);
        }

    }


    // Servicios -----------------------------------------------------------------------------------
    private void servicioAgregarFavorito(String idWord) {
        showCustomDialogProgress(requireContext());

        sharedPreferencesManager = new SharedPreferencesManager(requireContext());

        AddDictionaryRequest request = new AddDictionaryRequest();
        request.setIdUser(sharedPreferencesManager.getIdUsuario());
        request.setIdWord(idWord);

        dictionaryViewModel.addWordToDictionary(request);
        dictionaryViewModel.getAddDictionaryResult().observe(getViewLifecycleOwner(), resource -> {
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


    private void servicioEliminarFavorito( String idWord) {
        showCustomDialogProgress(requireContext());

        sharedPreferencesManager = new SharedPreferencesManager(requireContext());

        dictionaryViewModel.removeDictionaryEntry(sharedPreferencesManager.getIdUsuario(), idWord);
        dictionaryViewModel.getRemoveDictionaryResult().observe(getViewLifecycleOwner(), resource -> {
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


    private void servicioFavoritosDiccionario() {
        // TODO
        showEmptyState(true, "Aquí aparecerán tus palabras favoritas");

    }


    public List<CardData> getFavoriteCardData(Context context, List<String> favoriteWords) {
        // Cargar la lista completa de palabras desde el JSON y convertirla en un Map
        List<CardData> cardDataList = dictionaryViewModel.createCardData(context);
        Map<String, CardData> cardDataMap = cardDataList.stream()
                .collect(Collectors.toMap(cardData -> cardData.getTitle().toLowerCase(), cardData -> cardData));

        // Convertir la lista de palabras favoritas en un HashSet para una búsqueda rápida
        Set<String> favoriteWordsSet = new HashSet<>(favoriteWords.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet()));

        // Filtrar las palabras favoritas utilizando el Map
        return favoriteWordsSet.stream()
                .filter(cardDataMap::containsKey)
                .map(cardDataMap::get)
                .collect(Collectors.toList());
    }
}