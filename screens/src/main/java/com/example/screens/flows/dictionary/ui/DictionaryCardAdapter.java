package com.example.screens.flows.dictionary.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.components.cardDictionary.CardDictionary;
import com.example.screens.R;

import java.util.List;

public class DictionaryCardAdapter extends RecyclerView.Adapter<DictionaryCardAdapter.DictionaryViewHolder> {

    private List<CardData> cardDataList;
    private Context context;

    // Constructor
    public DictionaryCardAdapter(Context context, List<CardData> cardDataList) {
        this.context = context;
        this.cardDataList = cardDataList;
    }

    @NonNull
    @Override
    public DictionaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout
        CardDictionary cardDictionary = new CardDictionary(context);
        return new DictionaryViewHolder(cardDictionary);
    }

    @Override
    public void onBindViewHolder(@NonNull DictionaryViewHolder holder, int position) {
        CardData data = cardDataList.get(position);
        // Set the title
        holder.cardDictionary.setTvTitle(data.title);
        // Set the images
        holder.cardDictionary.setImgLensegua(ResourcesCompat.getDrawable(context.getResources(), data.imgLenseguaResId, null));
        holder.cardDictionary.setImgCard(ResourcesCompat.getDrawable(context.getResources(), data.imgCardResId, null));
    }

    @Override
    public int getItemCount() {
        return cardDataList.size();
    }

    // ViewHolder class
    public static class DictionaryViewHolder extends RecyclerView.ViewHolder {
        public CardDictionary cardDictionary;

        public DictionaryViewHolder(CardDictionary cardDictionary) {
            super(cardDictionary);
            this.cardDictionary = cardDictionary;
        }
    }
}
