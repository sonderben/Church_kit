package com.churchkit.churchkit.database.entity.bible;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "bible_chapter",
        foreignKeys = @ForeignKey(entity = BibleBook.class,
                parentColumns = {"bible_book_id"}, childColumns = {"bible_book_id"}))
public class BibleChapter {
    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "bible_chapter_id")
    private String bibleChapterId;
    private int position;
    @ColumnInfo(name = "bible_book_id")
    private String bibleBookId;

    public BibleChapter(String bibleChapterId, int position,  String bibleBookId) {
        this.bibleChapterId = bibleChapterId;
        this.position = position;
        this.bibleBookId = bibleBookId;
    }

    public String getBibleChapterId() {
        return bibleChapterId;
    }

    public void setBibleChapterId(String bibleChapterId) {
        this.bibleChapterId = bibleChapterId;
    }



    public int getPosition() {
        return position;
    }

    public void setPosition(short position) {
        this.position = position;
    }





    public String getBibleBookId() {
        return bibleBookId;
    }

    public void setBibleBookId(String bibleBookId) {
        this.bibleBookId = bibleBookId;
    }

    @Override
    public String toString() {
        return "BibleChapter{" +
                "bibleChapterId=" + bibleChapterId +
                ", position=" + position +
                ", bibleBookId=" + bibleBookId +
                '}';
    }
}
