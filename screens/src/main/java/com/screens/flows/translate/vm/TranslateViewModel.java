package com.screens.flows.translate.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.screens.base.BaseViewModel;
import com.senaschapinas.base.Resource;
import com.senaschapinas.flows.FavTraduction.FavTraductionRepositoryService;
import com.senaschapinas.flows.FavTraduction.FavTraductionRequest;
import com.senaschapinas.flows.GetUserInfo.ObjTraFav;
import com.senaschapinas.flows.RemoveTraduction.RemoveTraductionRepositoryService;
import com.senaschapinas.flows.RemoveVideo.RemoveVideoRepositoryService;
import com.senaschapinas.flows.SendTraduction.SendTraductionRepositoryService;
import com.senaschapinas.flows.SendTraduction.SendTraductionRequest;
import com.senaschapinas.flows.SendTraduction.SendTraductionResponse;

public class TranslateViewModel extends BaseViewModel {

    private SendTraductionRepositoryService sendTraductionRepository;
    private RemoveTraductionRepositoryService removeTraductionRepositoryService;
    private FavTraductionRepositoryService favTraductionRepositoryService;
    private MutableLiveData<Resource<SendTraductionResponse>> sendTraductionResource = new MutableLiveData<>();
    private MutableLiveData<Resource<Void>> removeVideoResource = new MutableLiveData<>();
    private MutableLiveData<Resource<Void>> favTraductionResource = new MutableLiveData<>();

    private String id_sentence = "";

    public TranslateViewModel(@NonNull Application application) {
        super(application);
        sendTraductionRepository = SendTraductionRepositoryService.getInstance();
        removeTraductionRepositoryService = RemoveTraductionRepositoryService.getInstance();
        favTraductionRepositoryService = FavTraductionRepositoryService.getInstance();
    }

    private MutableLiveData<ObjTraFav> selectedTrans = new MutableLiveData<>();

    public void selectTrans(ObjTraFav video) {
        selectedTrans.setValue(video);
    }

    public LiveData<ObjTraFav> getSelectedTrans() {
        return selectedTrans;
    }

    // Método para enviar una traducción
    public void sendTraduction(SendTraductionRequest sendTraductionRequest) {
        sendTraductionResource = sendTraductionRepository.sendTraduction(sendTraductionRequest);
    }

    public LiveData<Resource<SendTraductionResponse>> getSendTraductionResult() {
        return sendTraductionResource;
    }

    // Método para eliminar una traducción
    public void removeTranslation(String id_sentence) {
        removeVideoResource = removeTraductionRepositoryService.removeTraduction(id_sentence);
    }

    public LiveData<Resource<Void>> getRemoveTranslationResult() {
        return removeVideoResource;
    }

    // Método para añadir una traducción a favoritos
    public void addTraductionToFavorites(FavTraductionRequest favTraductionRequest) {
        favTraductionResource = favTraductionRepositoryService.favTraduction(favTraductionRequest);
    }

    public LiveData<Resource<Void>> getFavTraductionResult() {
        return favTraductionResource;
    }

    // Id sentence
    public String getId_sentence() {
        return id_sentence;
    }

    public void setId_sentence(String id_sentence) {
        this.id_sentence = id_sentence;
    }
}
