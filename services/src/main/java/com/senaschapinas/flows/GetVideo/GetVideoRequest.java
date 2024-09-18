package com.senaschapinas.flows.GetVideo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetVideoRequest {

    @JsonProperty("id_user")
    private String id_user;

    @JsonProperty("id_video")
    private String id_video;

    // Constructor
    public GetVideoRequest(String id_user, String id_video) {
        this.id_user = id_user;
        this.id_video = id_video;
    }

    // Getters y Setters
    public String getIdUser() {
        return id_user;
    }

    public void setIdUser(String id_user) {
        this.id_user = id_user;
    }

    public String getIdVideo() {
        return id_video;
    }

    public void setIdVideo(String id_video) {
        this.id_video = id_video;
    }
}
