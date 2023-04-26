package com.churchkit.churchkit.database.entity.bible;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
@Fts4(contentEntity = BibleVerse.class)
@Entity(tableName = "bible_verse_fts")
public class BibleVerseFts {
    private String bibleVerseId;
    private String reference;
    @ColumnInfo(name = "verse_text")
    private String verseText;
    @ColumnInfo(name = "bible_chapter_id")
    private String bibleChapterId;

    public BibleVerseFts(@NonNull String bibleVerseId, String reference, String verseText, String bibleChapterId) {
        this.bibleVerseId = bibleVerseId;
        this.reference = reference;
        this.verseText = verseText;
        this.bibleChapterId = bibleChapterId;
    }

    public String getBibleVerseId() {
        return bibleVerseId;
    }

    public void setBibleVerseId(String bibleVerseId) {
        this.bibleVerseId = bibleVerseId;
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
