package com.screens.flows.video.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.screens.base.BaseViewModel;
import com.senaschapinas.base.Resource;
import com.senaschapinas.flows.RemoveVideo.RemoveVideoRepositoryService;
import com.senaschapinas.flows.RemoveVideo.RemoveVideoRequest;

public class VideoViewModel extends BaseViewModel {

    private RemoveVideoRepositoryService removeVideoRepositoryService;
    private MutableLiveData<Resource<Void>> removeVideoResource = new MutableLiveData<>();

    public VideoViewModel(@NonNull Application application) {
        super(application);
        removeVideoRepositoryService = RemoveVideoRepositoryService.getInstance();
    }

    // Método para eliminar un video
    public void removeVideo(RemoveVideoRequest request) {
        removeVideoResource = removeVideoRepositoryService.removeVideo(request);
    }

    public LiveData<Resource<Void>> getRemoveVideoResult() {
        return removeVideoResource;
    }


}
