package com.senaschapinas.flows.GetUserInfo;

public class ObjTraFav {

    private String id_traduction;
    private String traduction;
    private String sentence_lensegua;

    // Constructor
    public ObjTraFav(String traduction, String sentence_lensegua) {

        this.traduction = traduction;
        this.sentence_lensegua = sentence_lensegua;
    }

    public String getTraduction_esp() {
        return traduction;
    }

    public void setTraduction_esp(String traduction) {
        this.traduction = traduction;
    }

    public String getSentence_lensegua() {
        return sentence_lensegua;
    }

    public void setSentence_lensegua(String sentence_lensegua) {
        this.sentence_lensegua = sentence_lensegua;
    }

    public String getId_traduction() {
        return id_traduction;
    }

    public void setId_traduction(String id_traduction) {
        this.id_traduction = id_traduction;
    }
}
