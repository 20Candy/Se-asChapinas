package com.senaschapinas.flows.SignUp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpResponse {

    @JsonProperty("id_user")
    private Integer id_user;


    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }
}
