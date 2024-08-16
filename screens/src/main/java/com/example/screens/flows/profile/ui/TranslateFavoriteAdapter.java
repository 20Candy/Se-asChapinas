package com.example.screens.flows.profile.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.components.buttons.DebounceClickListener;
import com.example.screens.R;

import java.util.List;

public class TranslateFavoriteAdapter extends RecyclerView.Adapter<TranslateFavoriteAdapter.TranslateFavoriteViewHolder> {

    private List<ObjTraFav> translateFavorites;
    private LayoutInflater inflater;
    private OnTranslateFavoriteClickListener itemClickListener;

    public interface OnTranslateFavoriteClickListener {
        void onTranslateFavoriteClick(ObjTraFav traFav);
    }

    // Constructor
    public TranslateFavoriteAdapter(Context context, List<ObjTraFav> translateFavorites, OnTranslateFavoriteClickListener itemClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.translateFavorites = translateFavorites;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public TranslateFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.translate_favorite, parent, false);
        return new TranslateFavoriteViewHolder(view, itemClickListener); // Changed listener to itemClickListener
    }

    @Override
    public void onBindViewHolder(@NonNull TranslateFavoriteViewHolder holder, int position) {
        ObjTraFav item = translateFavorites.get(position);
        holder.tvTextEsp.setText(item.getTraduccionEspanol());
        holder.tvLensegua.setText(item.getTraduccionLensegua());
    }

    @Override
    public int getItemCount() {
        return translateFavorites.size();
    }

    // ViewHolder class
    public class TranslateFavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTextEsp;
        TextView tvLensegua;
        ConstraintLayout clContainer;

        TranslateFavoriteViewHolder(View itemView, final OnTranslateFavoriteClickListener listener) {
            super(itemView);
            tvTextEsp = itemView.findViewById(R.id.tvTextEsp);
            tvLensegua = itemView.findViewById(R.id.tvLensegua);
            clContainer = itemView.findViewById(R.id.clContainer);

            itemView.setOnClickListener(new DebounceClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onTranslateFavoriteClick(translateFavorites.get(getAdapterPosition()));
                }
            }));
        }
    }
}
