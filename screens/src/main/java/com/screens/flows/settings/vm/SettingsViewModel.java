package com.screens.flows.settings.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.screens.base.BaseViewModel;
import com.senaschapinas.base.Resource;
import com.senaschapinas.flows.DeleteUser.DeleteUserRepositoryService;

public class SettingsViewModel extends BaseViewModel {

    private DeleteUserRepositoryService deleteUserRepositoryService;
    private MutableLiveData<Resource<Void>> deleteUserResource = new MutableLiveData<>();

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        deleteUserRepositoryService = DeleteUserRepositoryService.getInstance();
    }

    public void deleteUser(String idUser) {
        deleteUserResource = deleteUserRepositoryService.deleteUser(idUser);
    }

    public LiveData<Resource<Void>> getDeleteUserResult() {
        return deleteUserResource;
    }
}
