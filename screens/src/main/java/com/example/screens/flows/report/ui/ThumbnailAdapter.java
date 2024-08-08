package com.example.screens.flows.report.ui;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.screens.R;

import java.util.ArrayList;

class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ViewHolder> {
    private ArrayList<Bitmap> thumbnails;
    private OnThumbnailClickListener listener;
    private int selectedPosition = -1;  // -1 indica que no hay selección

    public ThumbnailAdapter(ArrayList<Bitmap> thumbnails, OnThumbnailClickListener listener) {
        this.thumbnails = thumbnails;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumbnail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.thumbnail.setImageBitmap(thumbnails.get(position));
        if (selectedPosition == position) {
            holder.background.setVisibility(View.VISIBLE);
        } else {
            holder.background.setVisibility(View.INVISIBLE);  // Elimina cualquier fondo si no está seleccionado
        }
        holder.thumbnail.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();  // Usa getAdapterPosition para obtener la posición actual
            if (adapterPosition != RecyclerView.NO_POSITION) {  // Verifica que la posición sea válida
                listener.onThumbnailClick(adapterPosition);
                selectedPosition = adapterPosition;  // Actualiza la posición seleccionada con la posición correcta
                notifyDataSetChanged();  // Notifica para que se actualice la vista
            }
        });
    }


    @Override
    public int getItemCount() {
        return thumbnails.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail, background;

        ViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            background = itemView.findViewById(R.id.imgBack);
        }
    }

    interface OnThumbnailClickListener {
        void onThumbnailClick(int position);
    }
}
