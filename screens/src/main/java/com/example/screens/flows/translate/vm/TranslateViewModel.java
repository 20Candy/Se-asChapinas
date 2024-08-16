package com.example.screens.flows.translate.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.screens.base.BaseViewModel;
import com.example.screens.flows.profile.ui.ObjTraFav;
import com.example.screens.flows.profile.ui.ObjVideoFav;

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
