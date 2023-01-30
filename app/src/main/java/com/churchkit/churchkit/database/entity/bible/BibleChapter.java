package com.churchkit.churchkit.database.entity.bible;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "bible_chapter",foreignKeys = @ForeignKey(entity = BibleBook.class, parentColumns = {"bible_book_id"}, childColumns = {"bible_book_id"}))
public class BibleChapter {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bible_chapter_id")
    private long bibleChapterId;
    private String chapter;
    private short position;
    private String language;
    @ColumnInfo(name = "bible_book_id")
    private long bibleBookId;

    public BibleChapter(String chapter, short position, String language, long bibleBookId) {
        this.chapter = chapter;
        this.position = position;
        this.language = language;
        this.bibleBookId = bibleBookId;
    }

    public long getBibleChapterId() {
        return bibleChapterId;
    }

    public void setBibleChapterId(long bibleChapterId) {
        this.bibleChapterId = bibleChapterId;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public short getPosition() {
        return position;
    }

    public void setPosition(short position) {
        this.position = position;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getBibleBookId() {
        return bibleBookId;
    }

    public void setBibleBookId(long bibleBookId) {
        this.bibleBookId = bibleBookId;
    }

    @Override
    public String toString() {
        return "BibleChapter{" +
                "bibleChapterId=" + bibleChapterId +
                ", chapter='" + chapter + '\'' +
                ", position=" + position +
                ", language='" + language + '\'' +
                ", bibleBookId=" + bibleBookId +
                '}';
    }
}
