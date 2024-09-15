package com.senaschapinas.flows.GetDictionary;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetDictionaryRequest {

    @JsonProperty("id_user")
    private String idUser;

    // Constructor
    public GetDictionaryRequest(String idUser) {
        this.idUser = idUser;
    }

    // Getter and Setter
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
