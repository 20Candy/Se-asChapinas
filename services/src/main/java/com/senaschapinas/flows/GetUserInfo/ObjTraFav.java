package com.senaschapinas.flows.GetUserInfo;

public class ObjTraFav {

    private String traduction_esp;
    private String sentence_lensegua;

    // Constructor
    public ObjTraFav(String traduction_esp, String sentence_lensegua) {

        this.traduction_esp = traduction_esp;
        this.sentence_lensegua = sentence_lensegua;
    }


    public String getTraduction_esp() {
        return traduction_esp;
    }

    public void setTraduction_esp(String traduction_esp) {
        this.traduction_esp = traduction_esp;
    }

    public String getSentence_lensegua() {
        return sentence_lensegua;
    }

    public void setSentence_lensegua(String sentence_lensegua) {
        this.sentence_lensegua = sentence_lensegua;
    }
}
