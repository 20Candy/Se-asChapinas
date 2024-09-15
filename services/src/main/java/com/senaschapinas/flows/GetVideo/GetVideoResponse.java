package com.senaschapinas.flows.GetVideo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetVideoResponse {

    @JsonProperty("video")
    private String videoUrl;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
