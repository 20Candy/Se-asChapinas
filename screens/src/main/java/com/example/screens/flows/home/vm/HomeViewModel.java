package com.example.screens.flows.home.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.screens.base.BaseViewModel;
import com.example.screens.flows.profile.ui.ObjVideoFav;

public class HomeViewModel extends BaseViewModel {

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }


    private MutableLiveData<ObjVideoFav> selectedVideo = new MutableLiveData<>();

    public void selectVideo(ObjVideoFav video) {
        selectedVideo.setValue(video);
    }

    public LiveData<ObjVideoFav> getSelectedVideo() {
        return selectedVideo;
    }





}
