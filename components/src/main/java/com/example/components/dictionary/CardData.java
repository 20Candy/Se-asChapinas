package com.example.components.dictionary;

public class CardData {
    String title;
    String imgLenseguaResId;
    String imgCardResId;

    String categoria;

    public CardData(String title, String imgLenseguaResId, String imgCardResId, String categoria) {
        this.title = title;
        this.imgLenseguaResId = imgLenseguaResId;
        this.imgCardResId = imgCardResId;
        this.categoria = categoria;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgLenseguaResId() {
        return imgLenseguaResId;
    }

    public void setImgLenseguaResId(String imgLenseguaResId) {
        this.imgLenseguaResId = imgLenseguaResId;
    }

    public String getImgCardResId() {
        return imgCardResId;
    }

    public void setImgCardResId(String imgCardResId) {
        this.imgCardResId = imgCardResId;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}

