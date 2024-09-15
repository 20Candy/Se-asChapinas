package com.senaschapinas.flows.SendTraduction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendTraductionRequest {

    @JsonProperty("id_user")
    private String idUser;

    @JsonProperty("sentence_lensegua")
    private String sentenceLensegua;

    @JsonProperty("id_sentence")
    private String idSentence;

    @JsonProperty("traduction_esp")
    private String traductionEsp;


    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getSentenceLensegua() {
        return sentenceLensegua;
    }

    public void setSentenceLensegua(String sentenceLensegua) {
        this.sentenceLensegua = sentenceLensegua;
    }

    public String getIdSentence() {
        return idSentence;
    }

    public void setIdSentence(String idSentence) {
        this.idSentence = idSentence;
    }

    public String getTraductionEsp() {
        return traductionEsp;
    }

    public void setTraductionEsp(String traductionEsp) {
        this.traductionEsp = traductionEsp;
    }
}
