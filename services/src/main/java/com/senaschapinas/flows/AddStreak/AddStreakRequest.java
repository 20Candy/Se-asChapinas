package com.senaschapinas.flows.AddStreak;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddStreakRequest {

    @JsonProperty("id_user")
    private String idUser;


    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
