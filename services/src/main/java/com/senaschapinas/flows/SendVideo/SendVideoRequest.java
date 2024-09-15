package com.senaschapinas.flows.SendVideo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendVideoRequest {

    @JsonProperty("id_user")
    private String id_user;

    @JsonProperty("video")
    private String video;

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
