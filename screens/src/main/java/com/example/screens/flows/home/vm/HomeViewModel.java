package com.example.screens.flows.home.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class HomeViewModel extends AndroidViewModel {

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }



    // Variable para encender o no camara al iniciar la app
    public boolean encenderCamara = true;

    public boolean getEncenderCamara() {
        return encenderCamara;
    }

    public void setEncenderCamara(boolean encenderCamara) {
        this.encenderCamara = encenderCamara;
    }

}
