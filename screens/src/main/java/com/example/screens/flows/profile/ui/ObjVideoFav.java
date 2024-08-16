package com.example.screens.flows.profile.ui;

public class ObjVideoFav {
    private String video;
    private String traduccionEspanol;
    private String traduccionLensegua;

    // Constructor
    public ObjVideoFav(String video, String traduccionEspanol, String traduccionLensegua) {
        this.video = video;
        this.traduccionEspanol = traduccionEspanol;
        this.traduccionLensegua = traduccionLensegua;
    }

    // Getters y Setters
    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTraduccionEspanol() {
        return traduccionEspanol;
    }

    public void setTraduccionEspanol(String traduccionEspanol) {
        this.traduccionEspanol = traduccionEspanol;
    }

    public String getTraduccionLensegua() {
        return traduccionLensegua;
    }

    public void setTraduccionLensegua(String traduccionLensegua) {
        this.traduccionLensegua = traduccionLensegua;
    }
}
