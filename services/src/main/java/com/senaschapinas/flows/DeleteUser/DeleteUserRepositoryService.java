package com.senaschapinas.flows.DeleteUser;

import androidx.lifecycle.MutableLiveData;

import com.senaschapinas.base.Resource;
import com.senaschapinas.base.serviceAPI;
import com.senaschapinas.base.serviceRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteUserRepositoryService {

    private serviceAPI apiService;
    private static DeleteUserRepositoryService instance;

    public DeleteUserRepositoryService() {
        this.apiService = serviceRetrofit.create();
    }

    public static DeleteUserRepositoryService getInstance() {
        if (instance == null) {
            instance = new DeleteUserRepositoryService();
        }
        return instance;
    }

    public MutableLiveData<Resource<Void>> deleteUser(String idUser) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();

        apiService.deleteUser(idUser).enqueue(new Callback<Void>() {
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
