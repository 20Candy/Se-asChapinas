package com.senaschapinas.flows.GetDictionary;

import androidx.lifecycle.MutableLiveData;

import com.senaschapinas.base.Resource;
import com.senaschapinas.base.serviceAPI;
import com.senaschapinas.base.serviceRetrofit;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDictionaryRepositoryService {

    private serviceAPI apiService;
    private static GetDictionaryRepositoryService instance;

    public GetDictionaryRepositoryService() {
        this.apiService = serviceRetrofit.create();
    }

    public static GetDictionaryRepositoryService getInstance() {
        if (instance == null) {
            instance = new GetDictionaryRepositoryService();
        }
        return instance;
    }

    public MutableLiveData<Resource<GetDictionaryResponse>> getDictionary(String idUser) {
        MutableLiveData<Resource<GetDictionaryResponse>> liveData = new MutableLiveData<>();

        GetDictionaryRequest request = new GetDictionaryRequest(idUser);
        apiService.getDictionary(request).enqueue(new Callback<GetDictionaryResponse>() {
            @Override
            public void onResponse(Call<GetDictionaryResponse> call, Response<GetDictionaryResponse> response) {
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
            public void onFailure(Call<GetDictionaryResponse> call, Throwable t) {
                liveData.setValue(Resource.error("Fallo de servicio: " + t.getMessage(), null));
            }
        });

        return liveData;
    }
}
