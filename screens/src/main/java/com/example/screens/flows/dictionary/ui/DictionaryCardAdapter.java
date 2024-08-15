package com.example.screens.flows.dictionary.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.components.dictionary.CardData;
import com.example.components.dictionary.CardDictionary;
import com.l4digital.fastscroll.FastScroller;

import java.util.List;

public class DictionaryCardAdapter extends RecyclerView.Adapter<DictionaryCardAdapter.DictionaryViewHolder>
        implements FastScroller.SectionIndexer{

    private List<CardData> cardDataList;
    private Context context;
    private OnHeartClickListener heartClickListener;

    // Constructor
    public DictionaryCardAdapter(Context context, List<CardData> cardDataList) {
        this.context = context;
        this.cardDataList = cardDataList;
    }

    // Set the listener
    public void setOnHeartClickListener(OnHeartClickListener listener) {
        this.heartClickListener = listener;
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
        holder.cardDictionary.setTvTitle(data.getTitle());
        holder.cardDictionary.setImgLensegua(ResourcesCompat.getDrawable(context.getResources(), data.getImgLenseguaResId(), null));
        holder.cardDictionary.setImgCard(ResourcesCompat.getDrawable(context.getResources(), data.getImgCardResId(), null));

        // Mostrar letra si es la primera en la sección
        if (position == 0 || !cardDataList.get(position - 1).getTitle().substring(0, 1).equals(data.getTitle().substring(0, 1))) {
            holder.cardDictionary.getTvLetter().setVisibility(View.VISIBLE);
            holder.cardDictionary.getTvLetter().setText(data.getTitle().substring(0, 1));
        } else {
            holder.cardDictionary.getTvLetter().setVisibility(View.INVISIBLE);
        }

        // Configura el listener para el clic en el corazón
        holder.cardDictionary.setHeartClickListener(new CardDictionary.HeartClickListener() {
            @Override
            public void onHeartClicked(boolean heartFull, CardData cardData) {
                if (heartClickListener != null) {
                    heartClickListener.onHeartClicked(heartFull, cardData);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return cardDataList.size();
    }

    @Override
    public CharSequence getSectionText(int position) {
        return cardDataList.get(position).getTitle().substring(0, 1);
    }

    // ViewHolder class
    public static class DictionaryViewHolder extends RecyclerView.ViewHolder {
        public CardDictionary cardDictionary;

        public DictionaryViewHolder(CardDictionary cardDictionary) {
            super(cardDictionary);
            this.cardDictionary = cardDictionary;
        }
    }

    // Interfaz para manejar los clics en el corazón
    public interface OnHeartClickListener {
        void onHeartClicked(boolean heartFull, CardData cardData);
    }

}
