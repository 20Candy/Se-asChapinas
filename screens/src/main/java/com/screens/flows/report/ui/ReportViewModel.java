package com.screens.flows.report.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.screens.base.BaseViewModel;
import com.senaschapinas.base.Resource;
import com.senaschapinas.flows.ReportVideo.ReportVideoRepositoryService;
import com.senaschapinas.flows.ReportVideo.ReportVideoRequest;

public class ReportViewModel extends BaseViewModel {

    private ReportVideoRepositoryService reportVideoRepositoryService;
    private MutableLiveData<Resource<Void>> reportVideoResource = new MutableLiveData<>();

    public ReportViewModel(@NonNull Application application) {
        super(application);
        reportVideoRepositoryService = ReportVideoRepositoryService.getInstance();
    }

    public void reportVideo(ReportVideoRequest reportVideoRequest) {
        reportVideoResource = reportVideoRepositoryService.reportVideo(reportVideoRequest);
    }

    public LiveData<Resource<Void>> getReportVideoResult() {
        return reportVideoResource;
    }
}
