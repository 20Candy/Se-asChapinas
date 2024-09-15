package com.screens.flows.dictionary.vm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.components.dictionary.CardData;
import com.screens.base.BaseViewModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DictionaryViewModel extends BaseViewModel {
    public DictionaryViewModel(@NonNull Application application) {
        super(application);
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

}
