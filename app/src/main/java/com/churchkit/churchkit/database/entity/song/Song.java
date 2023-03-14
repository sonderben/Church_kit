package com.churchkit.churchkit.database.entity.song;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "song",indices = {@Index(value = {"title"},unique = true)},
        foreignKeys = @ForeignKey(entity = SongBook.class,
                parentColumns = "song_book_id",
                childColumns = "song_book_id"))
public class Song {


    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "song_id")
    private String songID;
    @NonNull
    private String title;
    @ColumnInfo(name = "book_name_abbr")
    private String bookNameAbbr;
    @ColumnInfo(name = "book_name")
    private String bookName;
    private int position;
    private int page;
    @ColumnInfo(name = "song_book_id")
    private String songBookId;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Song(String songID, @NonNull String title, int position, int page, String songBookId, String bookNameAbbr) {
        this.songID = songID;
        this.title = title;
        this.bookNameAbbr = bookNameAbbr;
        this.position = position;
        this.page = page;
        this.songBookId = songBookId;
    }

    public String getBookNameAbbr() {
        return bookNameAbbr;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songID='" + songID + '\'' +
                ", title='" + title + '\'' +
                ", bookNameAbbr='" + bookNameAbbr + '\'' +
                ", position=" + position +
                ", page=" + page +
                ", songBookId='" + songBookId + '\'' +
                '}';
    }

    public void setBookNameAbbr(String bookNameAbbr) {
        this.bookNameAbbr = bookNameAbbr;
    }

    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int num) {
        this.position = num;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getSongBookId() {
        return songBookId;
    }

    public void setSongBookId(String songBookId) {
        this.songBookId = songBookId;
    }
}
//researchSongTable