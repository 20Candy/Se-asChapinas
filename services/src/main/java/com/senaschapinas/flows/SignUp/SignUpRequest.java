package com.senaschapinas.flows.SignUp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpRequest {

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("quetzalito")
    private String quetzalito;

    public String getMail() {
        return email;
    }

    public void setMail(String mail) {
        this.email = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuetzalito() {
        return quetzalito;
    }

    public void setQuetzalito(String quetzalito) {
        this.quetzalito = quetzalito;
    }
}
