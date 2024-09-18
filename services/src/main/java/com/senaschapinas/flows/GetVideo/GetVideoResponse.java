package com.senaschapinas.flows.GetVideo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetVideoResponse {

    @JsonProperty("video")
    private String video;

    public String getVideoUrl() {
        return video;
    }

    public void setVideoUrl(String video) {
        this.video = video;
    }
}
