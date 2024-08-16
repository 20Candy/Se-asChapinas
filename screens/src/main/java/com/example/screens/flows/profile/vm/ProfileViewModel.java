package com.example.screens.flows.profile.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.screens.base.BaseViewModel;

public class ProfileViewModel extends BaseViewModel {

    private boolean showVideoFavorite= true;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public boolean getShowVideoFavorite() {
        return showVideoFavorite;
    }

    public void setShowVideoFavorite(boolean show) {
        showVideoFavorite = show;
    }


}
