package com.senaschapinas.flows.SignUp;

import androidx.lifecycle.MutableLiveData;

import com.senaschapinas.base.Resource;
import com.senaschapinas.base.serviceAPI;
import com.senaschapinas.base.serviceRetrofit;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpRepositoryService {

    private serviceAPI apiService;
    private static SignUpRepositoryService instance;

    public SignUpRepositoryService() {
        this.apiService = serviceRetrofit.create();
    }

    public static SignUpRepositoryService getInstance() {
        if (instance == null) {
            instance = new SignUpRepositoryService();
        }
        return instance;
    }

    public MutableLiveData<Resource<SignUpResponse>> doSignUp(SignUpRequest request) {
        MutableLiveData<Resource<SignUpResponse>> liveData = new MutableLiveData<>();

        apiService.doSignUp(request).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
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
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                liveData.setValue(Resource.error("Fallo se servicio: " + t.getMessage(), null));
            }
        });

        return liveData;
    }

}
