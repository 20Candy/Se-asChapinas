package com.senaschapinas.flows.SignUp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpResponse {

    @JsonProperty("id_user")
    private Boolean id_user;


    public Boolean getId_user() {
        return id_user;
    }

    public void setId_user(Boolean id_user) {
        this.id_user = id_user;
    }
}
