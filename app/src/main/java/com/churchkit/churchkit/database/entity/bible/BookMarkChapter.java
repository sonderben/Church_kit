package com.churchkit.churchkit.database.entity.bible;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.churchkit.churchkit.database.entity.base.BaseBookMark;
import com.churchkit.churchkit.database.entity.song.BookMarkSong;
import com.churchkit.churchkit.database.entity.song.Song;

import java.util.Objects;

@Entity(tableName = "book_mark_chapter",
        foreignKeys = @ForeignKey(entity = BibleChapter.class,
                parentColumns = "id",
                childColumns = "bible_chapter_id"),indices = {@Index(value = {"bible_chapter_id"})})
public class BookMarkChapter extends BaseBookMark {


   @ColumnInfo(name = "bible_chapter_id")
    String bibleChapterId;

    public BookMarkChapter( String title, String description, String color, int start, int end,String bibleChapterId) {
        super( title, description, color, start, end);
        this.bibleChapterId = bibleChapterId;
        this.id = start+""+end+bibleChapterId;
    }

    public String getBibleChapterId() {
        return bibleChapterId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookMarkChapter that = (BookMarkChapter) o;
        return this.start == that.start && end == that.end && Objects.equals(bibleChapterId, that.bibleChapterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, bibleChapterId);
    }
}
