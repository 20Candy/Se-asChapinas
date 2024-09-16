package com.senaschapinas.flows.AddDictionary;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddDictionaryRequest {

    @JsonProperty("id_user")
    private String id_user;

    @JsonProperty("id_word")
    private String id_word;

    // Getters and setters
    public String getIdUser() {
        return id_user;
    }

    public void setIdUser(String id_user) {
        this.id_user = id_user;
    }

    public String getIdWord() {
        return id_word;
    }

    public void setIdWord(String id_word) {
        this.id_word = id_word;
    }
}
