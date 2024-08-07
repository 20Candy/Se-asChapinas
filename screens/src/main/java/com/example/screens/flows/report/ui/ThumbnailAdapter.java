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
        holder.thumbnail.setOnClickListener(v -> listener.onThumbnailClick(position));
    }

    @Override
    public int getItemCount() {
        return thumbnails.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;

        ViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }

    interface OnThumbnailClickListener {
        void onThumbnailClick(int position);
    }
}
