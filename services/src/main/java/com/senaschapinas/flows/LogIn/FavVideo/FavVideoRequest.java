package com.senaschapinas.flows.LogIn.FavVideo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FavVideoRequest {

    @JsonProperty("id_user")
    private String idUser;

    @JsonProperty("id_video")
    private String idVideo;

    @JsonProperty("prev_video")
    private String prevVideo;

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

    public String getPrevVideo() {
        return prevVideo;
    }

    public void setPrevVideo(String prevVideo) {
        this.prevVideo = prevVideo;
    }
}
