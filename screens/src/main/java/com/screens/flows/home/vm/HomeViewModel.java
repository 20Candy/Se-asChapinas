package com.screens.flows.home.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.screens.base.BaseViewModel;
import com.screens.flows.profile.ui.ObjVideoFav;

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
