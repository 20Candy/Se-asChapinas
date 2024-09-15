package com.screens.flows.signup.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.screens.base.BaseViewModel;
import com.senaschapinas.base.Resource;
import com.senaschapinas.flows.SignUp.SignUpRepositoryService;
import com.senaschapinas.flows.SignUp.SignUpRequest;
import com.senaschapinas.flows.SignUp.SignUpResponse;

public class SignUpViewModel extends BaseViewModel {

    private SignUpRepositoryService signUpRepositoryService;
    private MutableLiveData<Resource<SignUpResponse>> signUpResource = new MutableLiveData<>();

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        signUpRepositoryService = SignUpRepositoryService.getInstance();
    }

    public void doSignUpRequest(SignUpRequest signUpRequest) {
        signUpResource = signUpRepositoryService.doSignUp(signUpRequest);
    }

    public LiveData<Resource<SignUpResponse>> getSignUpResult() {
        return signUpResource;
    }
}
