package com.screens.flows.changePassword.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.screens.base.BaseViewModel;
import com.senaschapinas.base.Resource;
import com.senaschapinas.flows.ChangePassword.ChangePasswordRepositoryService;
import com.senaschapinas.flows.ChangePassword.ChangePasswordRequest;

public class ChangePasswordViewModel extends BaseViewModel {
    private ChangePasswordRepositoryService changePasswordRepositoryService;

    private MutableLiveData<Resource<Void>> changePasswordResource = new MutableLiveData<>();


    public ChangePasswordViewModel(@NonNull Application application) {
        super(application);
        changePasswordRepositoryService = ChangePasswordRepositoryService.getInstance();

    }


    // Método para manejar la solicitud de cambio de contraseña
    public void doChangePasswordRequest(ChangePasswordRequest changePasswordRequest) {
        changePasswordResource = changePasswordRepositoryService.doChangePassword(changePasswordRequest);
    }

    public LiveData<Resource<Void>> getChangePassword() {
        return changePasswordResource;
    }

}
