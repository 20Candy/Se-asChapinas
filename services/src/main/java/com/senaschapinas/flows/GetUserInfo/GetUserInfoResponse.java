package com.senaschapinas.flows.GetUserInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GetUserInfoResponse {

    @JsonProperty("mail")
    private String mail;

    @JsonProperty("streak")
    private int streak;

    @JsonProperty("quetzalito")
    private String quetzalito;

    @JsonProperty("videos_fav")
    private List<ObjVideoFav> videos_fav;

    @JsonProperty("traductions_fav")
    private List<ObjTraFav> traductions_fav;

    // Getters y setters
    public String getEmail() {
        return mail;
    }

    public void setEmail(String mail) {
        this.mail = mail;
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
        return videos_fav;
    }

    public void setVideosFav(List<ObjVideoFav> videos_fav) {
        this.videos_fav = videos_fav;
    }

    public List<ObjTraFav> getTraductionsFav() {
        return traductions_fav;
    }

    public void setTraductionsFav(List<ObjTraFav> traductions_fav) {
        this.traductions_fav = traductions_fav;
    }
}
