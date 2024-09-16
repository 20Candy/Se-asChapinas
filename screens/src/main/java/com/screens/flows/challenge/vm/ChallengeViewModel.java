package com.screens.flows.challenge.vm;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.screens.base.BaseViewModel;
import com.senaschapinas.base.Resource;
import com.senaschapinas.flows.AddStreak.AddStreakRepositoryService;
import com.senaschapinas.flows.AddStreak.AddStreakRequest;

public class ChallengeViewModel extends BaseViewModel {

    private AddStreakRepositoryService addStreakRepositoryService;
    private MutableLiveData<Resource<Void>> addStreakResource = new MutableLiveData<>();

    public ChallengeViewModel(@NonNull Application application) {
        super(application);
        addStreakRepositoryService = AddStreakRepositoryService.getInstance();
    }

    // Método para agregar una racha
    public void addStreak(AddStreakRequest streakRequest) {
        addStreakResource = addStreakRepositoryService.addStreak(streakRequest);
    }

    public LiveData<Resource<Void>> getAddStreakResult() {
        return addStreakResource;
    }
}
