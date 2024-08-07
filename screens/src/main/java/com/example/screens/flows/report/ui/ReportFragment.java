package com.example.screens.flows.report.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.screens.R;
import com.example.screens.base.BaseFragment;

import android.media.MediaMetadataRetriever;
import android.graphics.Bitmap;

import java.io.IOException;
import java.util.ArrayList;

public class ReportFragment extends BaseFragment {

    private VideoView videoView;
    private RecyclerView thumbnailsView;
    private ThumbnailAdapter adapter;
    private String videoPath = "";
    private ArrayList<Bitmap> thumbnailImages = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        videoView = view.findViewById(R.id.videoView);
        thumbnailsView = view.findViewById(R.id.thumbnailsView);

        if (getArguments() != null && getArguments().containsKey("video_path")) {
            videoPath = getArguments().getString("video_path");
        }

        try {
            generateThumbnails();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setupVideoView();
        setupThumbnailsView();

        return view;
    }

    private void setupVideoView() {
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.start();
    }

    private void setupThumbnailsView() {
        thumbnailsView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new ThumbnailAdapter(thumbnailImages, this::seekToPosition);
        thumbnailsView.setAdapter(adapter);
    }

    private void generateThumbnails() throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        // Suponiendo que queremos 5 thumbnails a intervalos regulares
        for (int i = 0; i < 5; i++) {
            long time = videoView.getDuration() / 5 * i;
            Bitmap thumb = retriever.getFrameAtTime(time, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            thumbnailImages.add(thumb);
        }
        retriever.release();
    }

    private void seekToPosition(int position) {
        if (videoView != null) {
            videoView.seekTo(position);
        }
    }
}
