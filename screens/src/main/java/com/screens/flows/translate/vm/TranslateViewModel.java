package com.screens.flows.translate.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.screens.base.BaseViewModel;
import com.screens.flows.profile.ui.ObjTraFav;

public class TranslateViewModel extends BaseViewModel {
    public TranslateViewModel(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<ObjTraFav> selectedTrans = new MutableLiveData<>();

    public void selectTrans(ObjTraFav video) {
        selectedTrans.setValue(video);
    }

    public LiveData<ObjTraFav> getSelectedTrans() {
        return selectedTrans;
    }



}
