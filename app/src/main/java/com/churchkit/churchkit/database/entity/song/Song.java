package com.churchkit.churchkit.database.entity.song;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.churchkit.churchkit.database.entity.base.SongAndChapter;

@Entity(tableName = "song",indices = {/*@Index(value = {"title"},unique = true),*/@Index(value = {"book_id"})},
        foreignKeys = @ForeignKey(entity = SongBook.class,
                parentColumns = "id",
                childColumns = "book_id",onDelete = ForeignKey.CASCADE))
public class Song extends SongAndChapter {

    @NonNull
    private String title;
    private int page;

    public Song(String infoId,@NonNull String id,String title, String bookTitle, float position, int page, String bookAbbreviation,String bookId) {
        super(infoId,id, bookTitle, position, bookId, bookAbbreviation);
        //super("","1","",1.2f,"","","");
        this.title = title;
        this.page = page;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public int getPage() {
        return page;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                ", position=" + position +
                ", bookId='" + bookId + '\'' +
                ", bookAbbreviation='" + bookAbbreviation + '\'' +
                ", title='" + title + '\'' +
                ", page=" + page +
                '}';
    }

    public void setPage(int page) {
        this.page = page;
    }
}
//researchSongTable