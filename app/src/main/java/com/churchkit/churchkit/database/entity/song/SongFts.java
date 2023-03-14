package com.churchkit.churchkit.database.entity.song;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;

@Entity(tableName = "Song_fts")
@Fts4(contentEntity = Song.class)
public class SongFts {
    private String title;
    @ColumnInfo(name = "book_name")
    private String bookName;
    @ColumnInfo(name = "book_name_abbr")
    private String bookNameAbbr;
    private String position;
    private String page;

    public String getTitle() {
        return title;
    }

    public String getBookNameAbbr() {
        return bookNameAbbr;
    }

    public void setBookNameAbbr(String bookNameAbbr) {
        this.bookNameAbbr = bookNameAbbr;
    }

    @Override
    public String toString() {
        return "SongFts{" +
                "title='" + title + '\'' +
                ", bookName='" + bookName + '\'' +
                ", position='" + position + '\'' +
                ", page='" + page + '\'' +
                '}';
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
