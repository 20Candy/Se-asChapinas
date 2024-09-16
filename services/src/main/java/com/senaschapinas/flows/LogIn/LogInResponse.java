package com.senaschapinas.flows.LogIn;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogInResponse {

    @JsonProperty("id")
    private Integer id;


    public Integer getId_user() {
        return id;
    }

    public void setId_user(Integer id_user) {
        this.id = id_user;
    }

}
