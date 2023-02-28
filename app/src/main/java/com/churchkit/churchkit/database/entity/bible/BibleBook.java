package com.churchkit.churchkit.database.entity.bible;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bible_book")
public class BibleBook {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "bible_book_id")
    private String bibleBookId;
    private String abbreviation;
    private String title;
    private int position; // for order
    private int testament;
    private int amountChapter;

    public int getAmountChapter() {
        return amountChapter;
    }

    public void setAmountChapter(int amountChapter) {
        this.amountChapter = amountChapter;
    }

    public BibleBook(String bibleBookId, String abbreviation, String title, int position, int testament, int amountChapter) {
        this.bibleBookId=bibleBookId;
        this.abbreviation = abbreviation;
        this.title = title;
        this.position = position;
        this.testament = testament;
        this.amountChapter = amountChapter;
    }

    public String getBibleBookId() {
        return bibleBookId;
    }

    public void setBibleBookId(String bibleBookId) {
        this.bibleBookId = bibleBookId;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
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

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTestament() {
        return testament;
    }

    public void setTestament(int testament) {
        this.testament = testament;
    }

    @Override
    public String toString() {
        return "BibleBook{" +
                "bibleBookId=" + bibleBookId +
                ", abbreviation='" + abbreviation + '\'' +
                ", title='" + title + '\'' +
                ", position=" + position +
                ", testament=" + testament +
                '}';
    }
}
