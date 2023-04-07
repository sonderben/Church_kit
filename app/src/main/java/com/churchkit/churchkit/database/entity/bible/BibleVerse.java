package com.churchkit.churchkit.database.entity.bible;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "bible_verse",indices = {@Index(value = {"bible_chapter_id"})},
foreignKeys = @ForeignKey(entity = BibleChapter.class,parentColumns = "id",childColumns = "bible_chapter_id"))
public class BibleVerse {
    @PrimaryKey
    @NonNull
    private String bibleVerseId;
    private int position;
    private String reference;
    @ColumnInfo(name = "verse_text")
    private String verseText;
    @ColumnInfo(name = "bible_chapter_id")
    private String bibleChapterId;

    public BibleVerse(@NonNull String bibleVerseId, int position, String reference, String verseText, String bibleChapterId) {
        this.bibleVerseId = bibleVerseId;
        this.position = position;
        this.reference = reference;
        this.verseText = verseText;
        this.bibleChapterId = bibleChapterId;
    }

    @NonNull
    public String getBibleVerseId() {
        return bibleVerseId;
    }

    public void setBibleVerseId(@NonNull String bibleVerseId) {
        this.bibleVerseId = bibleVerseId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "BibleVerse{" +
                "bibleVerseId='" + bibleVerseId + '\'' +
                ", position=" + position +
                ", reference='" + reference + '\'' +
                ", verseText='" + verseText + '\'' +
                ", bibleChapterId='" + bibleChapterId + '\'' +
                '}';
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getVerseText() {
        return verseText;
    }

    public void setVerseText(String verseText) {
        this.verseText = verseText;
    }

    public String getBibleChapterId() {
        return bibleChapterId;
    }

    public void setBibleChapterId(String bibleChapterId) {
        this.bibleChapterId = bibleChapterId;
    }
}
