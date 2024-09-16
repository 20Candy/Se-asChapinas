package com.senaschapinas.flows.GetUserInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetUserInfoRequest {

    @JsonProperty("id_user")
    private String id_user;

    public String getIdUser() {
        return id_user;
    }

    public void setIdUser(String id_user) {
        this.id_user = id_user;
    }
}
