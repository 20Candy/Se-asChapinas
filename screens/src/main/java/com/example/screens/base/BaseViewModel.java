package com.example.screens.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BaseViewModel extends AndroidViewModel {

    private static final MutableLiveData<Boolean> isBottomNavVisible = new MutableLiveData<>(true);

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getIsBottomNavVisible() {
        return isBottomNavVisible;
    }

    public static void setBottomNavVisible(boolean isVisible) {
        isBottomNavVisible.setValue(isVisible);
    }
}
