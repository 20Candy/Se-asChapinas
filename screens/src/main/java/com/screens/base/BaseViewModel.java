package com.screens.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BaseViewModel extends AndroidViewModel {

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    private static final MutableLiveData<Boolean> isBottomNavVisible = new MutableLiveData<>(true);

    public LiveData<Boolean> getIsBottomNavVisible() {
        return isBottomNavVisible;
    }

    public static void setBottomNavVisible(boolean isVisible) {
        isBottomNavVisible.setValue(isVisible);
    }


    private static final MutableLiveData<Integer> selectedTab = new MutableLiveData<>();

    public MutableLiveData<Integer> getSelectedTab() {
        return selectedTab;
    }

    public static void selectTab(int tabId) {
        selectedTab.setValue(tabId);
    }


}
