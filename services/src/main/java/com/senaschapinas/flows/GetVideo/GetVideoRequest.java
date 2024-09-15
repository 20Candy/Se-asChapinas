package com.senaschapinas.flows.GetVideo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetVideoRequest {

    @JsonProperty("id_user")
    private String idUser;

    @JsonProperty("id_video")
    private String idVideo;

    // Constructor
    public GetVideoRequest(String idUser, String idVideo) {
        this.idUser = idUser;
        this.idVideo = idVideo;
    }

    // Getters y Setters
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
}
