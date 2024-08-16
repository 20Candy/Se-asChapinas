package com.example.screens.flows.profile.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.components.buttons.DebounceClickListener;
import com.example.screens.R;
import java.util.List;

public class VideoFavoriteAdapter extends RecyclerView.Adapter<VideoFavoriteAdapter.VideoFavoriteViewHolder> {
    private Context context;
    private static List<ObjVideoFav> videoFavorites;
    private LayoutInflater inflater;

    private OnVideoFavoriteClickListener listener;


    public interface OnVideoFavoriteClickListener {
        void onVideoFavoriteClick(ObjVideoFav videoFavorite);
    }


    public VideoFavoriteAdapter(Context context, List<ObjVideoFav> videoFavorites, OnVideoFavoriteClickListener listener) {
        this.context = context;
        this.videoFavorites = videoFavorites;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.video_favorite, parent, false);
        return new VideoFavoriteViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoFavoriteViewHolder holder, int position) {
        ObjVideoFav currentVideo = videoFavorites.get(position);
        holder.tvText.setText(currentVideo.getTraduccionEspanol());
       // TODO PONER IMAGEN DE VIDEO
    }

    @Override
    public int getItemCount() {
        return videoFavorites.size();
    }

    static class VideoFavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;

        VideoFavoriteViewHolder(View itemView, final OnVideoFavoriteClickListener listener) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);

            itemView.setOnClickListener(new DebounceClickListener( v -> {

                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onVideoFavoriteClick(videoFavorites.get(getAdapterPosition()));
                }

            }));
        }
    }
}
