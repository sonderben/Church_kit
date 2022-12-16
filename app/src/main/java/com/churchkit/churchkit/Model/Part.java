package com.churchkit.churchkit.Model;

public class Part {
    private String title;
    private String titleAcronym;
    private int color;

    public Part(String title, String titleAcronym,int color) {
        this.title = title;
        this.titleAcronym = titleAcronym;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public int getColor() {
        return color;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleAcronym() {
        return titleAcronym;
    }

    public void setTitleAcronym(String titleAcronym) {
        this.titleAcronym = titleAcronym;
    }
}
