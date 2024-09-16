package com.screens.flows.forgotPassword.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.screens.base.BaseViewModel;
import com.senaschapinas.base.Resource;
import com.senaschapinas.flows.ForgotPassword.ForgotPasswordRepositoryService;
import com.senaschapinas.flows.ForgotPassword.ForgotPasswordRequest;

public class ForgotPasswordViewModel extends BaseViewModel {

    private ForgotPasswordRepositoryService forgotPasswordRepositoryService;
    private MutableLiveData<Resource<Void>> forgotPasswordResource = new MutableLiveData<>();



    public ForgotPasswordViewModel(@NonNull Application application) {
        super(application);
        forgotPasswordRepositoryService = ForgotPasswordRepositoryService.getInstance();
    }

    public void doForgotPasswordRequest(ForgotPasswordRequest forgotPasswordRequest) {
        forgotPasswordResource = forgotPasswordRepositoryService.doForgotPassword(forgotPasswordRequest);
    }

    public LiveData<Resource<Void>> getForgotPassword() {
        return forgotPasswordResource;
    }
}
