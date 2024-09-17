package com.senaschapinas.flows.RemoveTraduction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RemoveTraductionRequest {

    @JsonProperty("id_user")
    private String id_user;

    @JsonProperty("id_sentence")
    private String id_sentence;

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_sentence() {
        return id_sentence;
    }

    public void setId_sentence(String id_sentence) {
        this.id_sentence = id_sentence;
    }
}
