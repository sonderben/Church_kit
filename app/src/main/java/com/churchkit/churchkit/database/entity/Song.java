package com.churchkit.churchkit.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "song",indices = {@Index(value = {"title","num"},unique = true)},
        foreignKeys = @ForeignKey(entity = SongBook.class,
                parentColumns = "song_book_id",
                childColumns = "song_book_id"))
public class Song {
    @ColumnInfo(name = "song_id")
    @PrimaryKey(autoGenerate = true)
    private long songID;
    @NonNull
    private String title;
    private int num;
    private int page;
    @ColumnInfo(name = "song_book_id")
    private Long songBookId;

    public Song(/*long songID,*/ @NonNull String title, int num, int page, Long songBookId) {
        //this.songID = songID;
        this.title = title;
        this.num = num;
        this.page = page;
        this.songBookId = songBookId;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songID=" + songID +
                ", title='" + title + '\'' +
                ", num=" + num +
                ", page=" + page +
                ", songBookId=" + songBookId +
                '}';
    }

    public long getSongID() {
        return songID;
    }

    public void setSongID(long songID) {
        this.songID = songID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Long getSongBookId() {
        return songBookId;
    }

    public void setSongBookId(Long songBookId) {
        this.songBookId = songBookId;
    }
}
//researchSongTable