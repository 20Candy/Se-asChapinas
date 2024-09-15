package com.screens.flows.profile.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.components.buttons.DebounceClickListener;
import com.screens.R;
import com.senaschapinas.flows.GetUserInfo.ObjVideoFav;

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
        holder.tvText.setText(currentVideo.getTraduction_esp());
       // TODO PONER IMAGEN DE VIDEO
        holder.imageView.setImageDrawable(context.getResources().getDrawable(com.components.R.drawable.light_blue_line));
    }

    @Override
    public int getItemCount() {
        return videoFavorites.size();
    }

    static class VideoFavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;
        ImageView imageView;

        VideoFavoriteViewHolder(View itemView, final OnVideoFavoriteClickListener listener) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
            imageView = itemView.findViewById(R.id.imgVideo);

            itemView.setOnClickListener(new DebounceClickListener( v -> {

                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onVideoFavoriteClick(videoFavorites.get(getAdapterPosition()));
                }

            }));
        }
    }
}
