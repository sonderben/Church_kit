package com.churchkit.churchkit.database.entity.bible;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.churchkit.churchkit.database.entity.base.SongAndChapter;

@Entity(tableName = "bible_chapter",
        foreignKeys = @ForeignKey(entity = BibleBook.class,
                parentColumns = {"id"}, childColumns = {"book_id"},onDelete = ForeignKey.CASCADE),indices = {@Index("book_id")})
public class BibleChapter extends SongAndChapter {

    public BibleChapter(String infoId,@NonNull String id, String bookTitle, float position, String bookId, String bookAbbreviation) {
        super(infoId,id, bookTitle, position, bookId, bookAbbreviation);

    }




    @Override
    public String toString() {
        return "BibleChapter{" +
                "id='" + id + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                ", position=" + position +
                ", bookId='" + bookId + '\'' +
                ", bookAbbreviation='" + bookAbbreviation + '\'' +
                '}';
    }
}
