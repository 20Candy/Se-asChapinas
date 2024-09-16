package com.senaschapinas.flows.SendTraduction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendTraductionRequest {

    @JsonProperty("id_user")
    private String id_user;

    @JsonProperty("sentence_lensegua")
    private String sentence_lensegua;


    public String getIdUser() {
        return id_user;
    }

    public void setIdUser(String idUser) {
        this.id_user = idUser;
    }

    public String getSentenceLensegua() {
        return sentence_lensegua;
    }

    public void setSentenceLensegua(String sentenceLensegua) {
        this.sentence_lensegua = sentenceLensegua;
    }

}
