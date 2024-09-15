package com.senaschapinas.flows.SendTraduction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendTraductionResponse {

    @JsonProperty("id_sentence")
    private String idSentence;

    @JsonProperty("traduction_esp")
    private String traductionEsp;

    public String getIdSentence() {
        return idSentence;
    }

    public void setIdSentence(String idSentence) {
        this.idSentence = idSentence;
    }

    public String getTraductionEsp() {
        return traductionEsp;
    }

    public void setTraductionEsp(String traductionEsp) {
        this.traductionEsp = traductionEsp;
    }
}
