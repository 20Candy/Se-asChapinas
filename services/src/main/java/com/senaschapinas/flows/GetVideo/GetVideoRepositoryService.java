package com.senaschapinas.flows.GetVideo;

import androidx.lifecycle.MutableLiveData;

import com.senaschapinas.base.Resource;
import com.senaschapinas.base.serviceAPI;
import com.senaschapinas.base.serviceRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetVideoRepositoryService {

    private serviceAPI apiService;
    private static GetVideoRepositoryService instance;

    public GetVideoRepositoryService() {
        this.apiService = serviceRetrofit.create();
    }

    public static GetVideoRepositoryService getInstance() {
        if (instance == null) {
            instance = new GetVideoRepositoryService();
        }
        return instance;
    }

    public MutableLiveData<Resource<String>> getVideo(String idUser, String idVideo) {
        MutableLiveData<Resource<String>> liveData = new MutableLiveData<>();

        GetVideoRequest request = new GetVideoRequest(idUser, idVideo);
        apiService.getVideo(request).enqueue(new Callback<GetVideoResponse>() {
            @Override
            public void onResponse(Call<GetVideoResponse> call, Response<GetVideoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String videoUrl = response.body().getVideoUrl();
                    liveData.setValue(Resource.success(videoUrl));
                } else {
                    liveData.setValue(Resource.error("Error de servicio: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<GetVideoResponse> call, Throwable t) {
                liveData.setValue(Resource.error("Fallo de servicio: " + t.getMessage(), null));
            }
        });

        return liveData;
    }
}
