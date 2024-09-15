package com.senaschapinas.flows.FavTraduction;

import androidx.lifecycle.MutableLiveData;

import com.senaschapinas.base.Resource;
import com.senaschapinas.base.serviceAPI;
import com.senaschapinas.base.serviceRetrofit;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavTraductionRepositoryService {

    private serviceAPI apiService;
    private static FavTraductionRepositoryService instance;

    public FavTraductionRepositoryService() {
        this.apiService = serviceRetrofit.create();
    }

    public static FavTraductionRepositoryService getInstance() {
        if (instance == null) {
            instance = new FavTraductionRepositoryService();
        }
        return instance;
    }

    public MutableLiveData<Resource<Void>> favTraduction(FavTraductionRequest request) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();

        apiService.favTraduction(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(Resource.success(null));
                } else {
                    try {
                        // Extraer el cuerpo de error como una cadena
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String errorMessage = jsonObject.getString("message");

                        liveData.setValue(Resource.error(errorMessage, null));
                    } catch (Exception e) {
                        e.printStackTrace();
                        liveData.setValue(Resource.error("Error de servicio", null));
                    }
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
