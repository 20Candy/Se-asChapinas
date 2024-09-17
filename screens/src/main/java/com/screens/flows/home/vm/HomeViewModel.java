package com.screens.flows.home.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.screens.base.BaseViewModel;
import com.senaschapinas.base.Resource;
import com.senaschapinas.flows.GetUserInfo.ObjVideoFav;
import com.senaschapinas.flows.SendVideo.SendVideoRepositoryService;
import com.senaschapinas.flows.SendVideo.SendVideoResponse;

import java.io.File;

public class HomeViewModel extends BaseViewModel {

    private SendVideoRepositoryService sendVideoRepositoryService;
    private MutableLiveData<Resource<SendVideoResponse>> sendVideoResource = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        sendVideoRepositoryService = SendVideoRepositoryService.getInstance();
    }

    private MutableLiveData<ObjVideoFav> selectedVideo = new MutableLiveData<>();

    public void selectVideo(ObjVideoFav video) {
        selectedVideo.setValue(video);
    }

    public LiveData<ObjVideoFav> getSelectedVideo() {
        return selectedVideo;
    }

    // Método para enviar un video
    public void sendVideo(String idUser, File videoFile) {
        sendVideoResource = sendVideoRepositoryService.sendVideo(idUser, videoFile);
    }

    public LiveData<Resource<SendVideoResponse>> getSendVideoResult() {
        return sendVideoResource;
    }
}
