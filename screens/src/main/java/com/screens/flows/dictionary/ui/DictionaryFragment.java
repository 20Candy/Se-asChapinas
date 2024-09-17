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
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());

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
        onBackPressed(() -> { });

        setupRecyclerView(new ArrayList<>()); // Inicialmente vacío
        setupRecyclerViewCategories(dictionaryViewModel.createCategories(requireContext()));
        setListeners();

        // Obtener favoritos al iniciar el fragmento
        servicioFavoritosDiccionario();

        // Observa los cambios en los datos filtrados
        dictionaryViewModel.getFilteredCardData().observe(getViewLifecycleOwner(), filteredList -> {
            if (filteredList != null) {
                showEmptyState(filteredList.isEmpty(), "Palabra no disponible\n ¡Seguimos creciendo!");
                adapter.updateData(filteredList);
            }
        });
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

    }

    private void setupRecyclerViewCategories(List<String> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvCategories.setLayoutManager(layoutManager);

        categoriesAdapter = new CategoriesAdapter(getActivity(), list, (isFavorite, category) -> {
            if (isFavorite) {
                // Alternar entre mostrar y ocultar favoritos
                if ("favoritos".equalsIgnoreCase(selectedCategory)) {
                    selectedCategory = null; // Si ya estás en favoritos, restablecer el filtro
                    dictionaryViewModel.filtrarLista(requireContext(), "", "titulo"); // Mostrar toda la lista
                } else {
                    dictionaryViewModel.filtrarLista(requireContext(), "favoritos", "favoritos"); // Aplicar el filtro de favoritos
                    selectedCategory = "favoritos";
                }
            } else {
                // Si la categoría ya está seleccionada, quitar el filtro
                if (category.equals(selectedCategory)) {
                    selectedCategory = null; // Resetear la categoría seleccionada
                    dictionaryViewModel.filtrarLista(requireContext(), "", "titulo"); // Mostrar toda la lista
                } else {
                    selectedCategory = category; // Establecer la nueva categoría seleccionada
                    dictionaryViewModel.filtrarLista(requireContext(), category, "categoria"); // Aplicar el filtro de categoría
                }
            }
        });

        binding.rvCategories.setAdapter(categoriesAdapter);
    }





    private void setListeners() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                dictionaryViewModel.filtrarLista(requireContext(), newText, "titulo");
                return false;
            }
        });
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

        AddDictionaryRequest request = new AddDictionaryRequest();
        request.setIdUser(sharedPreferencesManager.getIdUsuario());
        request.setIdWord(idWord);

        dictionaryViewModel.addWordToDictionary(request);
        dictionaryViewModel.getAddDictionaryResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case SUCCESS:
                        hideCustomDialogProgress();
                        servicioFavoritosDiccionario();

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

        dictionaryViewModel.removeDictionaryEntry(sharedPreferencesManager.getIdUsuario(), idWord);
        dictionaryViewModel.getRemoveDictionaryResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case SUCCESS:
                        hideCustomDialogProgress();
                        servicioFavoritosDiccionario();

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
        showCustomDialogProgress(requireContext());

        dictionaryViewModel.fetchDictionary(sharedPreferencesManager.getIdUsuario());
        dictionaryViewModel.getDictionaryResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case SUCCESS:
                        hideCustomDialogProgress();

                        if (resource.data != null && resource.data.getPalabras() != null) {
                            List<String> palabras = resource.data.getPalabras();
                            dictionaryViewModel.setFavoriteTitles(palabras); // Establecer los favoritos

                            // Obtener el filtro actual y volver a aplicarlo
                            String currentFilter = selectedCategory != null ? selectedCategory : "";
                            String filterType = "categoria"; // O "favoritos" si estás en la vista de favoritos
                            if (currentFilter.isEmpty()) {
                                filterType = "titulo";
                            }else if (currentFilter.equals("favoritos")){
                                filterType = "favoritos";
                            }

                            dictionaryViewModel.filtrarLista(requireContext(), currentFilter, filterType);
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



}