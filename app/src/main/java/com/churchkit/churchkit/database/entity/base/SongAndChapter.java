package com.churchkit.churchkit.database.entity.base;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public abstract class SongAndChapter {
    @PrimaryKey
    @NonNull
    protected String id;
    @ColumnInfo(name = "book_title")
    protected String bookTitle;
    protected int position;
    @ColumnInfo(name = "book_id")
    protected String bookId;
    @ColumnInfo(name = "book_abbreviation")
    protected String bookAbbreviation;

    public SongAndChapter(@NonNull String id, String bookTitle, int position, String bookId,
                          String bookAbbreviation) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.position = position;
        this.bookId = bookId;
        this.bookAbbreviation = bookAbbreviation;
    }

    @Override
    public String toString() {
        return "SongAndChapter{" +
                "id='" + id + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                ", position=" + position +
                ", bookId='" + bookId + '\'' +
                ", bookAbbreviation='" + bookAbbreviation + '\'' +
                '}';
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookAbbreviation() {
        return bookAbbreviation;
    }

    public void setBookAbbreviation(String bookAbbreviation) {
        this.bookAbbreviation = bookAbbreviation;
    }
}
