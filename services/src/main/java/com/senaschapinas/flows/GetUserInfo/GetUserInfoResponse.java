package com.senaschapinas.flows.GetUserInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GetUserInfoResponse {

    @JsonProperty("email")
    private String email;

    @JsonProperty("streak")
    private int streak;

    @JsonProperty("quetzalito")
    private String quetzalito;

    @JsonProperty("videos_fav")
    private List<ObjVideoFav> videosFav;

    @JsonProperty("traductions_fav")
    private List<ObjTraFav> traductionsFav;

    // Getters y setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public String getQuetzalito() {
        return quetzalito;
    }

    public void setQuetzalito(String quetzalito) {
        this.quetzalito = quetzalito;
    }

    public List<ObjVideoFav> getVideosFav() {
        return videosFav;
    }

    public void setVideosFav(List<ObjVideoFav> videosFav) {
        this.videosFav = videosFav;
    }

    public List<ObjTraFav> getTraductionsFav() {
        return traductionsFav;
    }

    public void setTraductionsFav(List<ObjTraFav> traductionsFav) {
        this.traductionsFav = traductionsFav;
    }
}
