package com.senaschapinas.flows.ForgotPassword;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForgotPasswordRequest {

    @JsonProperty("email")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
