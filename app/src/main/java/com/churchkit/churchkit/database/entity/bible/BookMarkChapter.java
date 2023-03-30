package com.churchkit.churchkit.database.entity.bible;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.churchkit.churchkit.database.entity.song.BookMarkSong;
import com.churchkit.churchkit.database.entity.song.Song;

import java.util.Objects;

@Entity(tableName = "book_mark_chapter",
        foreignKeys = @ForeignKey(entity = BibleChapter.class,
                parentColumns = "bible_chapter_id",
                childColumns = "bible_chapter_id"))
public class BookMarkChapter {
    @NonNull
    @PrimaryKey//(autoGenerate = true)
    private String id;
    private String title;
    private String description;
    private String color;
    private int start;
    private int end;
    @ColumnInfo(name = "bible_chapter_id")
    private String bibleChapterId;

    public BookMarkChapter(String title, String description, String color, int start, int end, String bibleChapterId) {
        this.title = title;
        this.description = description;
        this.color = color;
        this.start = start;
        this.end = end;
        this.bibleChapterId = bibleChapterId;
        this.id = start+""+end+bibleChapterId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getBibleChapterId() {
        return bibleChapterId;
    }

    public void setBibleChapterId(String bibleChapterId) {
        this.bibleChapterId = bibleChapterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookMarkChapter that = (BookMarkChapter) o;
        return start == that.start && end == that.end && Objects.equals(bibleChapterId, that.bibleChapterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, bibleChapterId);
    }
}
