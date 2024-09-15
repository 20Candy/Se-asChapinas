package com.senaschapinas.flows.GetUserInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetUserInfoRequest {

    @JsonProperty("id_user")
    private String idUser;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
