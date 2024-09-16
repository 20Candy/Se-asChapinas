package com.senaschapinas.flows.GetDictionary;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetDictionaryRequest {

    @JsonProperty("id_user")
    private String id_user;

    // Constructor
    public GetDictionaryRequest(String id_user) {
        this.id_user = id_user;
    }

    // Getter and Setter
    public String getIdUser() {
        return id_user;
    }

    public void setIdUser(String id_user) {
        this.id_user = id_user;
    }
}
