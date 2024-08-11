package com.example.screens.flows.dictionary.ui;

class CardData {
    String title;
    int imgLenseguaResId;
    int imgCardResId;

    CardData(String title, int imgLenseguaResId, int imgCardResId) {
        this.title = title;
        this.imgLenseguaResId = imgLenseguaResId;
        this.imgCardResId = imgCardResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgLenseguaResId() {
        return imgLenseguaResId;
    }

    public void setImgLenseguaResId(int imgLenseguaResId) {
        this.imgLenseguaResId = imgLenseguaResId;
    }

    public int getImgCardResId() {
        return imgCardResId;
    }

    public void setImgCardResId(int imgCardResId) {
        this.imgCardResId = imgCardResId;
    }
}

