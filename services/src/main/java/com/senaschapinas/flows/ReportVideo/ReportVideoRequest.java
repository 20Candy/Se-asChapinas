package com.senaschapinas.flows.ReportVideo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportVideoRequest {

    @JsonProperty("id_user")
    private String idUser;

    @JsonProperty("id_video")
    private String idVideo;

    @JsonProperty("report_message")
    private String reportMessage;

    @JsonProperty("report_img")
    private String reportImg;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(String idVideo) {
        this.idVideo = idVideo;
    }

    public String getReportMessage() {
        return reportMessage;
    }

    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }

    public String getReportImg() {
        return reportImg;
    }

    public void setReportImg(String reportImg) {
        this.reportImg = reportImg;
    }
}
