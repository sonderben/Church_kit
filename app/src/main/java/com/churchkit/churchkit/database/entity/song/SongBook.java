package com.churchkit.churchkit.database.entity.song;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity(tableName = "song_book",indices = {@Index(value = {"name"},unique = true)})
public class SongBook {
    @ColumnInfo(name = "song_book_id")
    @PrimaryKey()
    @NonNull
    private String songBookId;
    private String name;
    @NonNull
    private String abbreviation;


    private int songAmount;

    @SerializedName("position")
    private int position;

    @Override
    public String toString() {
        return "SongBook{" +
                "songBookId=" + songBookId +
                ", name='" + name + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", songAmount=" + songAmount +
                ", position=" + position +
                '}';
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public SongBook(String songBookId, String name,String abbreviation, int songAmount, int position) {
        this.songBookId = songBookId;
        this.name = name;
        this.abbreviation = abbreviation;
        this.songAmount = songAmount;
    }

    public String getSongBookId() {
        return songBookId;
    }

    public void setSongBookId(String songBookId) {
        this.songBookId = songBookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public int getSongAmount() {
        return songAmount;
    }

    public void setSongAmount(int songAmount) {
        this.songAmount = songAmount;
    }



    public int getPosition() {
        return position;
    }

    public void setPosition(int num) {
        this.position = num;
    }
}
