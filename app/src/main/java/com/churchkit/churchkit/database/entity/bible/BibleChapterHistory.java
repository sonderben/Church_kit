package com.churchkit.churchkit.database.entity.bible;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "chapter_history",indices = {@Index(value = {"bible_chapter_id"},unique = true)},
        foreignKeys = @ForeignKey(entity = BibleChapter.class,
                parentColumns = "bible_chapter_id",
                childColumns = "bible_chapter_id"))
public class BibleChapterHistory {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "chapter_history_id")
    long chapterHistoryId;
    @ColumnInfo(name = "bible_chapter_id")
    private String bibleChapterId;
    long date;
    String abbreviation;

    public BibleChapterHistory(String bibleChapterId, long date, String abbreviation) {
        this.bibleChapterId = bibleChapterId;
        this.date = date;
        this.abbreviation = abbreviation;
    }

    public long getChapterHistoryId() {
        return chapterHistoryId;
    }

    public void setChapterHistoryId(long chapterHistoryId) {
        this.chapterHistoryId = chapterHistoryId;
    }

    public String getBibleChapterId() {
        return bibleChapterId;
    }

    public void setBibleChapterId(String bibleChapterId) {
        this.bibleChapterId = bibleChapterId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
