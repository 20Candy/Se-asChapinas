package com.senaschapinas.flows.LogIn.FavVideo;

import androidx.lifecycle.MutableLiveData;

import com.senaschapinas.base.Resource;
import com.senaschapinas.base.serviceAPI;
import com.senaschapinas.base.serviceRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavVideoRepositoryService {

    private serviceAPI apiService;
    private static FavVideoRepositoryService instance;

    public FavVideoRepositoryService() {
        this.apiService = serviceRetrofit.create();
    }

    public static FavVideoRepositoryService getInstance() {
        if (instance == null) {
            instance = new FavVideoRepositoryService();
        }
        return instance;
    }

    public MutableLiveData<Resource<Void>> favVideo(FavVideoRequest request) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();

        apiService.favVideo(request).enqueue(new Callback<Void>() {
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
