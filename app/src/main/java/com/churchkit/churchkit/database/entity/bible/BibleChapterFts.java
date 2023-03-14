package com.churchkit.churchkit.database.entity.bible;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;

@Fts4(contentEntity = BibleChapter.class)
@Entity(tableName = "bible_chapter_fts")
public class BibleChapterFts {
    @ColumnInfo(name = "bible_book_abbr")
    private String bibleBookAbbr;
    private int position;

    public String getBibleBookAbbr() {
        return bibleBookAbbr;
    }

    public void setBibleBookAbbr(String bibleBookAbbr) {
        this.bibleBookAbbr = bibleBookAbbr;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
