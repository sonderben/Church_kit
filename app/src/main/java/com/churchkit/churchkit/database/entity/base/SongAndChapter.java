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
    protected float position;
    @ColumnInfo(name = "book_id")
    protected String bookId;
    @ColumnInfo(name = "book_abbreviation")
    protected String bookAbbreviation;
    protected String infoId;







    public void setPosition(float position) {
        this.position = position;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }



    public SongAndChapter(String infoId,@NonNull String id, String bookTitle, float position, String bookId,
                          String bookAbbreviation) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.position = position;
        this.bookId = bookId;
        this.bookAbbreviation = bookAbbreviation;
        this.infoId = infoId;
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

    public float getPosition() {
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
