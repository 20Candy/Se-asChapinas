package com.screens.flows.profile.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.screens.base.BaseViewModel;
import com.senaschapinas.base.Resource;
import com.senaschapinas.flows.GetUserInfo.GetUserInfoRepositoryService;
import com.senaschapinas.flows.GetUserInfo.GetUserInfoRequest;
import com.senaschapinas.flows.GetUserInfo.GetUserInfoResponse;

public class ProfileViewModel extends BaseViewModel {

    private boolean showVideoFavorite= true;
    private String nombre;

    private GetUserInfoRepositoryService getUserInfoRepositoryService;
    private MutableLiveData<Resource<GetUserInfoResponse>> userInfoResource = new MutableLiveData<>();


    public ProfileViewModel(@NonNull Application application) {
        super(application);
        getUserInfoRepositoryService = GetUserInfoRepositoryService.getInstance();

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


    // Método para obtener la información del usuario
    public void fetchUserInfo(GetUserInfoRequest getUserInfoRequest) {
        userInfoResource = getUserInfoRepositoryService.getUserInfo(getUserInfoRequest);
    }

    public LiveData<Resource<GetUserInfoResponse>> getUserInfoResult() {
        return userInfoResource;
    }
}
