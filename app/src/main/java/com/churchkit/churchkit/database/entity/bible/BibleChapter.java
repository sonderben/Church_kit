package com.churchkit.churchkit.database.entity.bible;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "bible_chapter",foreignKeys = @ForeignKey(entity = BibleBook.class, parentColumns = {"bible_book_id"}, childColumns = {"bible_book_id"}))
public class BibleChapter {
    @ColumnInfo(name = "bible_chapter_id")
    private long bibleChapterId;
    private String chapter;
    private short position;
    private String language;
    @ColumnInfo(name = "bible_book_id",index = true)
    private long bibleBookId;
}
