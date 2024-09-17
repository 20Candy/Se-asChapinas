package com.senaschapinas.flows.SendVideo;

import androidx.lifecycle.MutableLiveData;

import com.senaschapinas.base.Resource;
import com.senaschapinas.base.serviceAPI;
import com.senaschapinas.base.serviceRetrofit;


import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendVideoRepositoryService {

    private serviceAPI apiService;
    private static SendVideoRepositoryService instance;

    public SendVideoRepositoryService() {
        this.apiService = serviceRetrofit.create();
    }

    public static SendVideoRepositoryService getInstance(){
        if(instance == null){
            instance = new SendVideoRepositoryService();
        }
        return instance;
    }


    public MutableLiveData<Resource<SendVideoResponse>> sendVideo(String idUser, File videoFile) {
        MutableLiveData<Resource<SendVideoResponse>> liveData = new MutableLiveData<>();

        // Crear RequestBody
        RequestBody idUserRequest = RequestBody.create(MediaType.parse("text/plain"), idUser);
        RequestBody requestFile = RequestBody.create(MediaType.parse("video/mp4"), videoFile);
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video", videoFile.getName(), requestFile);



        apiService.doSendVideo(idUserRequest, videoPart).enqueue(new Callback<SendVideoResponse>() {
            @Override
            public void onResponse(Call<SendVideoResponse> call, Response<SendVideoResponse> response) {
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
            public void onFailure(Call<SendVideoResponse> call, Throwable t) {
                liveData.setValue(Resource.error("Error en el servicio: " + t.getMessage(), null));
            }
        });

        return liveData;
    }
}
