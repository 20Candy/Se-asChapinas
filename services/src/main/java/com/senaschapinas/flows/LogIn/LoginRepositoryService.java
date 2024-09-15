package com.senaschapinas.flows.LogIn;

import androidx.lifecycle.MutableLiveData;

import com.senaschapinas.base.Resource;
import com.senaschapinas.base.serviceAPI;
import com.senaschapinas.base.serviceRetrofit;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepositoryService {

    private serviceAPI apiService;
    private static LoginRepositoryService instance;

    public LoginRepositoryService() {
        this.apiService = serviceRetrofit.create();
    }

    public static LoginRepositoryService getInstance(){
        if(instance == null){
            instance = new LoginRepositoryService();
        }
        return instance;
    }


    public MutableLiveData<Resource<LogInResponse>> doLogin(LoginRequest loginRequest) {
        MutableLiveData<Resource<LogInResponse>> liveData = new MutableLiveData<>();

        apiService.doLogIn(loginRequest).enqueue(new Callback<LogInResponse>() {
            @Override
            public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
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
            public void onFailure(Call<LogInResponse> call, Throwable t) {
                liveData.setValue(Resource.error("Error en el servicio: " + t.getMessage(), null));
            }
        });

        return liveData;
    }

}
