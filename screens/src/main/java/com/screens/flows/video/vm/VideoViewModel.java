package com.screens.flows.video.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.screens.base.BaseViewModel;
import com.senaschapinas.base.Resource;
import com.senaschapinas.flows.FavVideo.FavVideoRepositoryService;
import com.senaschapinas.flows.RemoveVideo.RemoveVideoRepositoryService;
import com.senaschapinas.flows.RemoveVideo.RemoveVideoRequest;

import java.io.File;

public class VideoViewModel extends BaseViewModel {

    private RemoveVideoRepositoryService removeVideoRepositoryService;
    private FavVideoRepositoryService favVideoRepositoryService;
    private MutableLiveData<Resource<Void>> removeVideoResource = new MutableLiveData<>();
    private MutableLiveData<Resource<Void>> favVideoResource = new MutableLiveData<>();

    public VideoViewModel(@NonNull Application application) {
        super(application);
        removeVideoRepositoryService = RemoveVideoRepositoryService.getInstance();
        favVideoRepositoryService = FavVideoRepositoryService.getInstance();
    }

    // Método para eliminar un video
    public void removeVideo(RemoveVideoRequest request) {
        removeVideoResource = removeVideoRepositoryService.removeVideo(request);
    }

    public LiveData<Resource<Void>> getRemoveVideoResult() {
        return removeVideoResource;
    }

    // Método para agregar un video a favoritos
    public void favVideo(String id_user, String id_video, File imageFile) {
        favVideoResource = favVideoRepositoryService.favVideo(id_user, id_video, imageFile);
    }

    public LiveData<Resource<Void>> getFavVideoResult() {
        return favVideoResource;
    }
}
