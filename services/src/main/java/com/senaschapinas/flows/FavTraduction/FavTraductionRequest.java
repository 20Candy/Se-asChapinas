package com.senaschapinas.flows.FavTraduction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FavTraductionRequest {

    @JsonProperty("id_user")
    private String idUser;

    @JsonProperty("id_sentence")
    private String idSentence;

    // Getters and setters
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdSentence() {
        return idSentence;
    }

    public void setIdSentence(String idSentence) {
        this.idSentence = idSentence;
    }
}
