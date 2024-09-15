package com.senaschapinas.flows.AddDictionary;

import androidx.lifecycle.MutableLiveData;

import com.senaschapinas.base.Resource;
import com.senaschapinas.base.serviceAPI;
import com.senaschapinas.base.serviceRetrofit;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDictionaryRepositoryService {

    private serviceAPI apiService;
    private static AddDictionaryRepositoryService instance;

    public AddDictionaryRepositoryService() {
        this.apiService = serviceRetrofit.create();
    }

    public static AddDictionaryRepositoryService getInstance() {
        if (instance == null) {
            instance = new AddDictionaryRepositoryService();
        }
        return instance;
    }

    public MutableLiveData<Resource<Void>> addDictionary(AddDictionaryRequest request) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();

        apiService.addDictionary(request).enqueue(new Callback<Void>() {
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
