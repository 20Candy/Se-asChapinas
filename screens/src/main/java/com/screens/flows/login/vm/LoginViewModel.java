package com.screens.flows.login.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.screens.base.BaseViewModel;
import com.senaschapinas.base.Resource;
import com.senaschapinas.flows.LogIn.LogInResponse;
import com.senaschapinas.flows.LogIn.LoginRepositoryService;
import com.senaschapinas.flows.LogIn.LoginRequest;

public class LoginViewModel extends BaseViewModel {

    private LoginRepositoryService loginRepositoryService;
    private MutableLiveData<Resource<LogInResponse>> resource = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepositoryService = LoginRepositoryService.getInstance();
    }

    public void doLoginRequest(LoginRequest loginRequest) {
        resource = loginRepositoryService.doLogin(loginRequest);
    }


    public LiveData<Resource<LogInResponse>> getLogin() {
        return resource;
    }
}
