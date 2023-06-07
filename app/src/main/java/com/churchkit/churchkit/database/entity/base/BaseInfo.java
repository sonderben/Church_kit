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
    protected String abbreviation;
    protected long population;
    protected String country;
    protected String videoDate;

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    protected String audioDate;
    protected String textDate;
    protected String url;
    protected String size;
    protected boolean isDownloaded=false;
    protected int testament;
    protected boolean forceUpdate;

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Ignore


    public BaseInfo(String id, String language, String name,String abbreviation, int testament, String url,String size){
        this.id = id;
        this.language = language;
        this.name = name;
        this.abbreviation = abbreviation;
        this.testament = testament;
        this.url = url;
        this.size = size;
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

    @Override
    public String toString() {
        return "BaseInfo{" +
                "id='" + id + '\'' +
                ", language='" + language + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", size='" + size + '\'' +
                ", isDownloaded=" + isDownloaded +
                ", testament=" + testament +
                '}';
    }
//id='ENGESV'
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
