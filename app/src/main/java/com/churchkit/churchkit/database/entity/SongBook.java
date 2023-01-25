package com.churchkit.churchkit.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "song_book",indices = {@Index(value = {"name"},unique = true)})
public class SongBook {
    @ColumnInfo(name = "song_book_id")
    @PrimaryKey(autoGenerate = true)
    private int songBookId;
    private String name;
    @NonNull
    private String abbreviation;

    private short color;
    @ColumnInfo(name = "song_amount")
    private int songAmount;

    private short image;
    private short num;

    @Override
    public String toString() {
        return "SongBook{" +
                "songBookId=" + songBookId +
                ", name='" + name + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", color=" + color +
                ", songAmount=" + songAmount +
                ", image=" + image +
                ", num=" + num +
                '}';
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public SongBook(/*int songBookId,*/ String name,String abbreviation, short color, int songAmount, short image, short num) {
        //this.songBookId = songBookId;
        this.name = name;
        this.abbreviation = abbreviation;
        this.color = color;
        this.songAmount = songAmount;
        this.image = image;
        this.num = num;
    }

    public int getSongBookId() {
        return songBookId;
    }

    public void setSongBookId(int songBookId) {
        this.songBookId = songBookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getColor() {
        return color;
    }

    public void setColor(short color) {
        this.color = color;
    }

    public int getSongAmount() {
        return songAmount;
    }

    public void setSongAmount(int songAmount) {
        this.songAmount = songAmount;
    }

    public short getImage() {
        return image;
    }

    public void setImage(short image) {
        this.image = image;
    }

    public short getNum() {
        return num;
    }

    public void setNum(short num) {
        this.num = num;
    }
}
