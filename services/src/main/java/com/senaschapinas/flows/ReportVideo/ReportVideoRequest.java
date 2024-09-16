package com.senaschapinas.flows.ReportVideo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportVideoRequest {

    @JsonProperty("id_user")
    private String id_user;

    @JsonProperty("id_video")
    private String id_video;

    @JsonProperty("report_message")
    private String reportMessage;

    @JsonProperty("report_img")
    private String reportImg;

    public String getIdUser() {
        return id_user;
    }

    public void setIdUser(String idUser) {
        this.id_user = idUser;
    }

    public String getIdVideo() {
        return id_video;
    }

    public void setIdVideo(String idVideo) {
        this.id_video = idVideo;
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
