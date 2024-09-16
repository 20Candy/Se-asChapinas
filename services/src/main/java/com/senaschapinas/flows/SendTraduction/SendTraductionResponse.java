package com.senaschapinas.flows.SendTraduction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendTraductionResponse {

    @JsonProperty("id_sentence")
    private String id_sentence;

    @JsonProperty("traduction_esp")
    private String traduction_esp;

    public String getIdSentence() {
        return id_sentence;
    }

    public void setIdSentence(String idSentence) {
        this.id_sentence = idSentence;
    }

    public String getTraductionEsp() {
        return traduction_esp;
    }

    public void setTraductionEsp(String traductionEsp) {
        this.traduction_esp = traductionEsp;
    }
}
