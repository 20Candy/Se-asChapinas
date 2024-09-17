package com.senaschapinas.flows.RemoveVideo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RemoveVideoRequest {

    @JsonProperty("id_user")
    private String id_user;

    @JsonProperty("id_video")
    private String id_video;

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_video() {
        return id_video;
    }

    public void setId_video(String id_video) {
        this.id_video = id_video;
    }
}
