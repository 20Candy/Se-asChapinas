package com.example.screens.flows.home.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.screens.base.BaseViewModel;

public class HomeViewModel extends BaseViewModel {

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }


    // Variable para encender o no camara al iniciar la app
    public boolean encenderCamara = false;

    public boolean getEncenderCamara() {
        return encenderCamara;
    }

    public void setEncenderCamara(boolean encenderCamara) {
        this.encenderCamara = encenderCamara;
    }

}
