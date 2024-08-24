package com.example.screens.flows.dictionary.vm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.components.dictionary.CardData;
import com.example.screens.base.BaseViewModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileReader;
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


//    public List<CardData> createCardData() {
//        cardDataList.add(new CardData("Agua", com.example.components.R.drawable.agua_lensegua, com.example.components.R.drawable.agua, "acciones"));
//        cardDataList.add(new CardData("Ayer", com.example.components.R.drawable.ayer_lensegua, com.example.components.R.drawable.ayer, "tiempo"));
//        cardDataList.add(new CardData("Ayuda", com.example.components.R.drawable.ayuda_lensegua, com.example.components.R.drawable.ayuda, "acciones"));
//        cardDataList.add(new CardData("Baño", com.example.components.R.drawable.bano_lensegua, com.example.components.R.drawable.bano, "lugares"));
//        cardDataList.add(new CardData("Beber", com.example.components.R.drawable.beber_lensegua, com.example.components.R.drawable.beber, "acciones"));
//        cardDataList.add(new CardData("Cansado", com.example.components.R.drawable.cansado_lensegua, com.example.components.R.drawable.cansado, "estados"));
//        cardDataList.add(new CardData("Casa", com.example.components.R.drawable.casa_lensegua, com.example.components.R.drawable.casa, "lugares"));
//        cardDataList.add(new CardData("Colegio", com.example.components.R.drawable.colegio_lensegua, com.example.components.R.drawable.colegio, "lugares"));
//        cardDataList.add(new CardData("Comer", com.example.components.R.drawable.comer_lensegua, com.example.components.R.drawable.comer, "acciones"));
//        cardDataList.add(new CardData("Comida", com.example.components.R.drawable.comida_lensegua, com.example.components.R.drawable.comida, "objetos")); //pendiente
//        cardDataList.add(new CardData("Cómo", com.example.components.R.drawable.como_lensegua, com.example.components.R.drawable.como, "preguntas"));
//        cardDataList.add(new CardData("Cúando", com.example.components.R.drawable.cuando_lensegua, com.example.components.R.drawable.cuando, "preguntas"));
//        cardDataList.add(new CardData("Cuánto", com.example.components.R.drawable.cuanto_lensegua, com.example.components.R.drawable.cuatno, "preguntas"));
//        cardDataList.add(new CardData("Dónde", com.example.components.R.drawable.donde_lensegua, com.example.components.R.drawable.donde, "preguntas"));
//        cardDataList.add(new CardData("Hacer", com.example.components.R.drawable.hacer_lensegua, com.example.components.R.drawable.hacer, "aciones"));
//        cardDataList.add(new CardData("Hambre", com.example.components.R.drawable.hambre_lensegua, com.example.components.R.drawable.hambre, "estados"));
//        cardDataList.add(new CardData("Hospital", com.example.components.R.drawable.hospital_lensegua, com.example.components.R.drawable.hospital, "lugares"));
//        cardDataList.add(new CardData("Hoy", com.example.components.R.drawable.hoy_lensegua, com.example.components.R.drawable.hoy, "tiempo"));
//        cardDataList.add(new CardData("Ir", com.example.components.R.drawable.ir_lensegua, com.example.components.R.drawable.ir, "acciones"));
//        cardDataList.add(new CardData("Mañana", com.example.components.R.drawable.manana_lensegua, com.example.components.R.drawable.manana, "tiempo"));
//        cardDataList.add(new CardData("Necesitar", com.example.components.R.drawable.necesitar_lensegua, com.example.components.R.drawable.necesitar, "acciones"));
//        cardDataList.add(new CardData("Policia", com.example.components.R.drawable.policia_lensegua, com.example.components.R.drawable.policia, "personas"));
//        cardDataList.add(new CardData("Qué", com.example.components.R.drawable.que_lensegua, com.example.components.R.drawable.que, "preguntas"));
//        cardDataList.add(new CardData("Quién", com.example.components.R.drawable.quien_lensegua, com.example.components.R.drawable.quien, "preguntas"));
//        cardDataList.add(new CardData("Querer", com.example.components.R.drawable.querer_lensegua, com.example.components.R.drawable.querer, "acciones"));
//        cardDataList.add(new CardData("Sed", com.example.components.R.drawable.sed_lensegua, com.example.components.R.drawable.sed, "estados"));
//        cardDataList.add(new CardData("Tener", com.example.components.R.drawable.tener_lensegua, com.example.components.R.drawable.tener, "acciones"));
//        cardDataList.add(new CardData("Trabajo", com.example.components.R.drawable.trabajo_lensegua, com.example.components.R.drawable.trabajo, "lugares"));
//        cardDataList.add(new CardData("Tu", com.example.components.R.drawable.tu_lensegua, com.example.components.R.drawable.tu, "personas"));
//        cardDataList.add(new CardData("Universidad", com.example.components.R.drawable.universidad_lensegua, com.example.components.R.drawable.universidad, "lugares"));
//        cardDataList.add(new CardData("Yo", com.example.components.R.drawable.yo_lensegya, com.example.components.R.drawable.yo, "personas"));
//        cardDataList.add(new CardData("?", com.example.components.R.drawable.pregunta_lensegua, com.example.components.R.drawable.pregunta, "preguntas"));
//
//        return cardDataList;
//    }


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
