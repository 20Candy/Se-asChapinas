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
import com.senaschapinas.flows.RemoveDictionary.RemoveDictionaryRepositoryService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DictionaryViewModel extends BaseViewModel {

    private AddDictionaryRepositoryService addDictionaryRepositoryService;
    private RemoveDictionaryRepositoryService removeDictionaryRepositoryService;

    private MutableLiveData<Resource<Void>> addDictionaryResource = new MutableLiveData<>();
    private MutableLiveData<Resource<Void>> removeDictionaryResource = new MutableLiveData<>();

    public DictionaryViewModel(@NonNull Application application) {
        super(application);
        addDictionaryRepositoryService = AddDictionaryRepositoryService.getInstance();
        removeDictionaryRepositoryService = RemoveDictionaryRepositoryService.getInstance();
    }

    List<CardData> cardDataList = new ArrayList<>();

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
}
