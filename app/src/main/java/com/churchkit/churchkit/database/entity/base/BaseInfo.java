package com.churchkit.churchkit.database.entity.base;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.churchkit.churchkit.database.entity.song.SongInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public abstract class BaseInfo {
    private String iso;

    @PrimaryKey
    @NonNull
    protected String id;
    protected String language;
    protected String name;
    protected long population;
    protected String country;
    protected String videoDate;
    protected String audioDate;
    protected String textDate;
    protected String url;
    protected boolean isDownloaded=false;
    protected int testament;

    @Ignore


    public BaseInfo(String id, String language, String name, int testament, String url){
        this.id = id;
        this.language = language;
        this.name = name;
        this.testament = testament;
        this.url = url;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseInfo baseInfo = (BaseInfo) o;
        return id.equals(baseInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getVideoDate() {
        return videoDate;
    }

    public void setVideoDate(String videoDate) {
        this.videoDate = videoDate;
    }

    public String getAudioDate() {
        return audioDate;
    }

    public void setAudioDate(String audioDate) {
        this.audioDate = audioDate;
    }

    public String getTextDate() {
        return textDate;
    }

    public void setTextDate(String textDate) {
        this.textDate = textDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public int getTestament() {
        return testament;
    }

    public void setTestament(int testament) {
        this.testament = testament;
    }
}
