package com.churchkit.churchkit.database.entity.bible;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bible_book")
public class BibleBook {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bible_book_id")
    private long bibleBookId;
    private String abbreviation;
    private String title;
    private short position; // for order
    private short testament;

    public BibleBook(String abbreviation, String title, short position, short testament) {
        this.abbreviation = abbreviation;
        this.title = title;
        this.position = position;
        this.testament = testament;
    }

    public long getBibleBookId() {
        return bibleBookId;
    }

    public void setBibleBookId(long bibleBookId) {
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

    public short getPosition() {
        return position;
    }

    public void setPosition(short position) {
        this.position = position;
    }

    public short getTestament() {
        return testament;
    }

    public void setTestament(short testament) {
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
