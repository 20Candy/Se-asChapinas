package com.senaschapinas.flows.AddStreak;

import androidx.lifecycle.MutableLiveData;

import com.senaschapinas.base.Resource;
import com.senaschapinas.base.serviceAPI;
import com.senaschapinas.base.serviceRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStreakRepositoryService {

    private serviceAPI apiService;
    private static AddStreakRepositoryService instance;

    public AddStreakRepositoryService() {
        this.apiService = serviceRetrofit.create();
    }

    public static AddStreakRepositoryService getInstance() {
        if (instance == null) {
            instance = new AddStreakRepositoryService();
        }
        return instance;
    }

    public MutableLiveData<Resource<Void>> addStreak(AddStreakRequest streakRequest) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();

        apiService.addStreak(streakRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(Resource.success(null));
                } else {
                    liveData.setValue(Resource.error("Error de servicio: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                liveData.setValue(Resource.error("Fallo de servicio: " + t.getMessage(), null));
            }
        });

        return liveData;
    }
}
