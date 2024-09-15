package com.senaschapinas.flows.ForgotPassword;

import androidx.lifecycle.MutableLiveData;

import com.senaschapinas.base.Resource;
import com.senaschapinas.base.serviceAPI;
import com.senaschapinas.base.serviceRetrofit;


import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordRepositoryService {

    private serviceAPI apiService;
    private static ForgotPasswordRepositoryService instance;

    public ForgotPasswordRepositoryService() {
        this.apiService = serviceRetrofit.create();
    }

    public static ForgotPasswordRepositoryService getInstance(){
        if(instance == null){
            instance = new ForgotPasswordRepositoryService();
        }
        return instance;
    }


    public MutableLiveData<Resource<Void>> doForgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();

        apiService.doForgotPassword(forgotPasswordRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
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
            public void onFailure(Call<Void> call, Throwable t) {
                liveData.setValue(Resource.error("Error en el servicio: " + t.getMessage(), null));
            }
        });

        return liveData;
    }

}
