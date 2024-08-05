package com.example.screens.flows.video.ui;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.screens.R;
import com.example.screens.base.BaseFragment;
import com.example.screens.databinding.FragmentHomeBinding;
import com.example.screens.databinding.FragmentVideoBinding;
import com.example.screens.flows.home.vm.HomeViewModel;
import com.example.screens.flows.landing.vm.LandingViewModel;
import com.example.screens.flows.video.vm.VideoViewModel;
import com.example.screens.utils.SharedPreferencesManager;


public class VideoFragment extends BaseFragment {

    //Binding --------------------------------------------------------------------------------------
    FragmentVideoBinding binding;

    // Atributos de la clase -----------------------------------------------------------------------
    VideoViewModel videoViewModel;

    // Metodos de ciclo de vida --------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoViewModel = new ViewModelProvider(requireActivity()).get(VideoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVideoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBackPressed(() -> {});
        setVideoPlayer();
    }

    @Override
    public void onResume() {
        super.onResume();

        VideoViewModel.setBottomNavVisible(false);
    }

    // Metodos privados de la clase ----------------------------------------------------------------

    private void setVideoPlayer(){
        if (getArguments() != null && getArguments().containsKey("video_path")) {
            String videoPath = getArguments().getString("video_path");
            binding.videoView.setVideoURI(Uri.parse(videoPath));
            binding.videoView.start();
        }
    }

}