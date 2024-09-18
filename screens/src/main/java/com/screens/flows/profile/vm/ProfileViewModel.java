package com.screens.flows.profile.vm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.screens.base.BaseViewModel;
import com.senaschapinas.base.Resource;
import com.senaschapinas.flows.GetUserInfo.GetUserInfoRepositoryService;
import com.senaschapinas.flows.GetUserInfo.GetUserInfoRequest;
import com.senaschapinas.flows.GetUserInfo.GetUserInfoResponse;
import com.senaschapinas.flows.GetVideo.GetVideoRepositoryService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileViewModel extends BaseViewModel {

    private boolean showVideoFavorite= true;
    private String nombre;

    private String racha = "0";

    private GetUserInfoRepositoryService getUserInfoRepositoryService;
    private GetVideoRepositoryService getVideoRepositoryService;

    private MutableLiveData<Resource<GetUserInfoResponse>> userInfoResource = new MutableLiveData<>();
    private MutableLiveData<Resource<String>> videoUrlResource = new MutableLiveData<>();


    public ProfileViewModel(@NonNull Application application) {
        super(application);
        getUserInfoRepositoryService = GetUserInfoRepositoryService.getInstance();
        getVideoRepositoryService = GetVideoRepositoryService.getInstance();

    }

    public boolean getShowVideoFavorite() {
        return showVideoFavorite;
    }

    public void setShowVideoFavorite(boolean show) {
        showVideoFavorite = show;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRacha() {
        return racha;
    }

    public void setRacha(String racha) {
        this.racha = racha;
    }

    // Método para obtener la información del usuario
    public void fetchUserInfo(GetUserInfoRequest getUserInfoRequest) {
        userInfoResource = getUserInfoRepositoryService.getUserInfo(getUserInfoRequest);
    }

    public LiveData<Resource<GetUserInfoResponse>> getUserInfoResult() {
        return userInfoResource;
    }

    // Método para obtener el video
    public void fetchVideo(String idUser, String idVideo) {
        videoUrlResource = getVideoRepositoryService.getVideo(idUser, idVideo);
    }

    public LiveData<Resource<String>> getVideoResult() {
        return videoUrlResource;
    }



}
