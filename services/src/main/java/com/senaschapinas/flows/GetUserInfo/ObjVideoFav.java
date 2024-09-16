package com.senaschapinas.flows.GetUserInfo;

public class ObjVideoFav {
    private String id_video;
    private String traduction_esp;
    private String sentence_lensegua;

    private String prev_image;

    // Constructor
    public ObjVideoFav(String id_video, String traduction_esp, String sentence_lensegua, String prev_image) {
        this.id_video = id_video;
        this.traduction_esp = traduction_esp;
        this.sentence_lensegua = sentence_lensegua;
        this.prev_image = prev_image;
    }

    // Getters y Setters
    public String getId_video() {
        return id_video;
    }

    public void setId_video(String id_video) {
        this.id_video = id_video;
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

    public String getPreview() {
        return prev_image;
    }

    public void setPreview(String prev_image) {
        this.prev_image = prev_image;
    }
}
