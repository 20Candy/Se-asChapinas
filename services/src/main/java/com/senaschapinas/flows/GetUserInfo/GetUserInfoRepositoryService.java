package com.senaschapinas.flows.GetUserInfo;

import androidx.lifecycle.MutableLiveData;

import com.senaschapinas.base.Resource;
import com.senaschapinas.base.serviceAPI;
import com.senaschapinas.base.serviceRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUserInfoRepositoryService {

    private serviceAPI apiService;
    private static GetUserInfoRepositoryService instance;

    public GetUserInfoRepositoryService() {
        this.apiService = serviceRetrofit.create();
    }

    public static GetUserInfoRepositoryService getInstance() {
        if (instance == null) {
            instance = new GetUserInfoRepositoryService();
        }
        return instance;
    }

    public MutableLiveData<Resource<GetUserInfoResponse>> getUserInfo(GetUserInfoRequest getUserInfoRequest) {
        MutableLiveData<Resource<GetUserInfoResponse>> liveData = new MutableLiveData<>();

        apiService.getUserInfo(getUserInfoRequest).enqueue(new Callback<GetUserInfoResponse>() {
            @Override
            public void onResponse(Call<GetUserInfoResponse> call, Response<GetUserInfoResponse> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Error de servicio: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<GetUserInfoResponse> call, Throwable t) {
                liveData.setValue(Resource.error("Fallo de servicio: " + t.getMessage(), null));
            }
        });

        return liveData;
    }
}
