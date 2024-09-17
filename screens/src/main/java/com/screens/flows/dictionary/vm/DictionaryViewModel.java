package com.screens.flows.dictionary.vm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.components.dictionary.CardData;
import com.screens.base.BaseViewModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.senaschapinas.base.Resource;
import com.senaschapinas.flows.AddDictionary.AddDictionaryRepositoryService;
import com.senaschapinas.flows.AddDictionary.AddDictionaryRequest;
import com.senaschapinas.flows.GetDictionary.GetDictionaryRepositoryService;
import com.senaschapinas.flows.GetDictionary.GetDictionaryResponse;
import com.senaschapinas.flows.RemoveDictionary.RemoveDictionaryRepositoryService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DictionaryViewModel extends BaseViewModel {

    private AddDictionaryRepositoryService addDictionaryRepositoryService;
    private RemoveDictionaryRepositoryService removeDictionaryRepositoryService;
    private GetDictionaryRepositoryService getDictionaryRepositoryService;

    private MutableLiveData<Resource<Void>> addDictionaryResource = new MutableLiveData<>();
    private MutableLiveData<Resource<Void>> removeDictionaryResource = new MutableLiveData<>();
    private MutableLiveData<Resource<GetDictionaryResponse>> getDictionaryResource = new MutableLiveData<>();

    private MutableLiveData<List<CardData>> filteredCardData = new MutableLiveData<>();
    List<CardData> cardDataList = new ArrayList<>();

    private List<String> favoriteTitles = new ArrayList<>(); // Lista para los títulos favoritos


    public DictionaryViewModel(@NonNull Application application) {
        super(application);
        addDictionaryRepositoryService = AddDictionaryRepositoryService.getInstance();
        removeDictionaryRepositoryService = RemoveDictionaryRepositoryService.getInstance();
        getDictionaryRepositoryService = GetDictionaryRepositoryService.getInstance();
    }


    public List<CardData> createCardData(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("data.json");
            Reader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonArray cardDataJsonArray = jsonObject.getAsJsonArray("cardDataList");

            Type cardDataListType = new TypeToken<List<CardData>>() {}.getType();
            cardDataList = gson.fromJson(cardDataJsonArray, cardDataListType);

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cardDataList;
    }

    public List<String> createCategories(Context context) {
        List<String> categories = null;
        try {
            // Abre el archivo JSON desde los activos
            InputStream inputStream = context.getAssets().open("categories.json");
            Reader reader = new InputStreamReader(inputStream);

            // Usa Gson para analizar el JSON
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonArray categoriesJsonArray = jsonObject.getAsJsonArray("categories");

            // Convierte el JSON en una lista de cadenas
            Type categoryListType = new TypeToken<List<String>>() {}.getType();
            categories = gson.fromJson(categoriesJsonArray, categoryListType);

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return categories;
    }

    // Método para añadir una palabra al diccionario
    public void addWordToDictionary(AddDictionaryRequest request) {
        addDictionaryResource = addDictionaryRepositoryService.addDictionary(request);
    }

    public LiveData<Resource<Void>> getAddDictionaryResult() {
        return addDictionaryResource;
    }

    // Método para eliminar una entrada del diccionario
    public void removeDictionaryEntry(String idUser, String idWord) {
        removeDictionaryResource = removeDictionaryRepositoryService.removeDictionaryEntry(idUser, idWord);
    }

    public LiveData<Resource<Void>> getRemoveDictionaryResult() {
        return removeDictionaryResource;
    }

    // Método para obtener el diccionario
    public void fetchDictionary(String idUser) {
        getDictionaryResource = getDictionaryRepositoryService.getDictionary(idUser);
    }

    public LiveData<Resource<GetDictionaryResponse>> getDictionaryResult() {
        return getDictionaryResource;
    }


    // Método para filtrar la lista según el filtro y el tipo
    public void filtrarLista(Context context, String filtro, String tipo) {
        List<CardData> listaOriginal = createCardData(context);
        List<CardData> listaFiltrada = new ArrayList<>();

        // Lista de títulos favoritos en minúsculas para la comparación
        Set<String> favoriteTitlesSet = new HashSet<>(favoriteTitles.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet()));

        if (filtro != null && !filtro.isEmpty()) {
            for (CardData card : listaOriginal) {
                if ("titulo".equalsIgnoreCase(tipo)) {
                    if (card.getTitle() != null && card.getTitle().toLowerCase().contains(filtro.toLowerCase())) {
                        listaFiltrada.add(card);
                    }
                } else if ("categoria".equalsIgnoreCase(tipo)) {
                    if (card.getCategoria() != null && card.getCategoria().toLowerCase().contains(filtro.toLowerCase())) {
                        listaFiltrada.add(card);
                    }
                } else if ("favoritos".equalsIgnoreCase(tipo)) {
                    listaFiltrada = getFavoriteCardData();
                }
            }
        } else {
            listaFiltrada = listaOriginal; // Si no hay filtro, usar la lista original
        }

        // Aplicar el estado de favorito a las tarjetas filtradas
        for (CardData card : listaFiltrada) {
            card.setFavorite(favoriteTitlesSet.contains(card.getTitle().toLowerCase()));
        }

        filteredCardData.setValue(listaFiltrada);
    }



    // Método para obtener los datos filtrados
    public LiveData<List<CardData>> getFilteredCardData() {
        return filteredCardData;
    }

    // Método para crear o obtener los datos de cardData
    public List<CardData> getCardData(Context context) {
        if (cardDataList == null  || cardDataList.isEmpty()) {
            cardDataList = createCardData(context);
        }
        return cardDataList;
    }

    // Método para limpiar y recargar los datos cuando se actualizan los favoritos
    public void reloadCardData(Context context) {
        cardDataList = createCardData(context);
    }


    public List<CardData> getFavoriteCardData() {
        Map<String, CardData> cardDataMap = cardDataList.stream()
                .collect(Collectors.toMap(cardData -> cardData.getTitle().toLowerCase(), cardData -> cardData));

        return favoriteTitles.stream()
                .map(String::toLowerCase)
                .filter(cardDataMap::containsKey)
                .map(cardDataMap::get)
                .collect(Collectors.toList());
    }

    // Método para obtener los títulos favoritos
    public List<String> getFavoriteTitles() {
        return favoriteTitles;
    }

    // Método para establecer los títulos favoritos
    public void setFavoriteTitles(List<String> favoriteTitles) {
        this.favoriteTitles = favoriteTitles;
    }

}
