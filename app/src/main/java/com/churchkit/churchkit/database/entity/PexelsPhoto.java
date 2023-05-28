package com.churchkit.churchkit.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pexels_photo")
public class PexelsPhoto {
    @PrimaryKey
    @NonNull
    private long id;
    private int width;
    private int height;
    private String url;
    private String photographer;
    private String avgColor;
    private String urlOriginal;
    private String urlMedium;
    private String urlSmall;
    private String urlTiny;
    private String urlPortrait;
    private String  urlLandscape;
    private String alt;

    public PexelsPhoto(long id,int width, int height, String url, String photographer, String avgColor, String urlOriginal, String urlMedium, String urlSmall, String urlTiny, String urlPortrait, String urlLandscape, String alt) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.url = url;
        this.urlSmall = urlSmall;
        this.photographer = photographer;
        this.avgColor = avgColor;
        this.urlOriginal = urlOriginal;
        this.urlMedium = urlMedium;
        this.urlTiny = urlTiny;
        this.urlPortrait = urlPortrait;
        this.urlLandscape = urlLandscape;
        this.alt = alt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getUrl() {
        return url;
    }

    public String getPhotographer() {
        return photographer;
    }

    public String getAvgColor() {
        return avgColor;
    }

    public String getUrlOriginal() {
        return urlOriginal;
    }

    @Override
    public String toString() {
        return "PexelsPhoto{" +
                "id=" + id +
                ", photographer='" + photographer + '\'' +
                ", urlOriginal='" + urlOriginal + '\'' +
                ", urlMedium='" + urlMedium + '\'' +
                ", urlSmall='" + urlSmall + '\'' +
                ", urlPortrait='" + urlPortrait + '\'' +
                ", urlLandscape='" + urlLandscape + '\'' +
                '}';
    }

    public String getUrlMedium() {
        return urlMedium;
    }

    public String getUrlTiny() {
        return urlTiny;
    }

    public String getUrlSmall() {
        return urlSmall;
    }

    public String getUrlPortrait() {
        return urlPortrait;
    }

    public String getUrlLandscape() {
        return urlLandscape;
    }

    public String getAlt() {
        return alt;
    }
}
