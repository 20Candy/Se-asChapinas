package com.senaschapinas.flows.SendVideo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendVideoResponse {

    @JsonProperty("id_video")
    private String id_video;

    @JsonProperty("traduction_lensegua")
    private String traduction_lensegua;

    @JsonProperty("traduction_esp")
    private String traduction_esp;


}
