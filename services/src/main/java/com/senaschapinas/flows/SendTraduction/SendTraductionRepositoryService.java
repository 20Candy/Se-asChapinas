package com.senaschapinas.flows.SendTraduction;

import androidx.lifecycle.MutableLiveData;

import com.senaschapinas.base.Resource;
import com.senaschapinas.base.serviceAPI;
import com.senaschapinas.base.serviceRetrofit;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendTraductionRepositoryService {

    private serviceAPI apiService;
    private static SendTraductionRepositoryService instance;

    public SendTraductionRepositoryService() {
        this.apiService = serviceRetrofit.create();
    }

    public static SendTraductionRepositoryService getInstance() {
        if (instance == null) {
            instance = new SendTraductionRepositoryService();
        }
        return instance;
    }

    public MutableLiveData<Resource<SendTraductionResponse>> sendTraduction(SendTraductionRequest request) {
        MutableLiveData<Resource<SendTraductionResponse>> liveData = new MutableLiveData<>();

        apiService.sendTraduction(request).enqueue(new Callback<SendTraductionResponse>() {
            @Override
            public void onResponse(Call<SendTraductionResponse> call, Response<SendTraductionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
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
            public void onFailure(Call<SendTraductionResponse> call, Throwable t) {
                liveData.setValue(Resource.error("Fallo de servicio: " + t.getMessage(), null));
            }
        });

        return liveData;
    }
}
