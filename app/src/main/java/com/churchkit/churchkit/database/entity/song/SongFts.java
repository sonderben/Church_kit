package com.churchkit.churchkit.database.entity.song;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;

@Entity(tableName = "Song_fts")
@Fts4(contentEntity = Song.class)
public class SongFts {
    private String title;
    @ColumnInfo(name = "book_title")
    private String bookTitle;
    @ColumnInfo(name = "book_abbreviation")
    private String bookAbbreviation;
    private String position;
    private String page;

    public String getTitle() {
        return title;
    }


    @Override
    public String toString() {
        return "SongFts{" +
                "title='" + title + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                ", bookAbbreviation='" + bookAbbreviation + '\'' +
                ", position='" + position + '\'' +
                ", page='" + page + '\'' +
                '}';
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAbbreviation() {
        return bookAbbreviation;
    }

    public void setBookAbbreviation(String bookAbbreviation) {
        this.bookAbbreviation = bookAbbreviation;
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
