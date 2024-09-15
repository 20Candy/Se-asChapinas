package com.senaschapinas.flows.AddDictionary;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddDictionaryRequest {

    @JsonProperty("id_user")
    private String idUser;

    @JsonProperty("id_word")
    private String idWord;

    // Getters and setters
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdWord() {
        return idWord;
    }

    public void setIdWord(String idWord) {
        this.idWord = idWord;
    }
}
