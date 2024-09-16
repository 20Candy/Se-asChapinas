package com.senaschapinas.flows.FavTraduction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FavTraductionRequest {

    @JsonProperty("id_user")
    private String id_user;

    @JsonProperty("id_sentence")
    private String id_sentence;

    // Getters and setters
    public String getIdUser() {
        return id_user;
    }

    public void setIdUser(String id_user) {
        this.id_user = id_user;
    }

    public String getIdSentence() {
        return id_sentence;
    }

    public void setIdSentence(String id_sentence) {
        this.id_sentence = id_sentence;
    }
}
