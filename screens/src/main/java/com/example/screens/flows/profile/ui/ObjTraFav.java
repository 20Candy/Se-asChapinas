package com.example.screens.flows.profile.ui;

public class ObjTraFav {

    private String traduccionEspanol;
    private String traduccionLensegua;

    // Constructor
    public ObjTraFav(String traduccionEspanol, String traduccionLensegua) {

        this.traduccionEspanol = traduccionEspanol;
        this.traduccionLensegua = traduccionLensegua;
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
