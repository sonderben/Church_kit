package com.churchkit.churchkit.database.entity.bible;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.churchkit.churchkit.database.entity.base.Book;

@Entity(tableName = "bible_book"/*,foreignKeys = @ForeignKey(entity = BibleInfo.class,
        parentColumns = {"id"}, childColumns = {"bible_info_id"},onDelete = ForeignKey.CASCADE)*/,indices = {@Index("bible_info_id")})
public class BibleBook extends Book {

    private int testament;

    @ColumnInfo(name = "bible_info_id")
    private String bibleInfoId;

    public String getBibleInfoId() {
        return bibleInfoId;
    }

    public void setBibleInfoId(String bibleInfoId) {
        this.bibleInfoId = bibleInfoId;
    }

    public BibleBook(String bibleInfoId, @NonNull String id, String abbreviation, String title, int position, int testament, int childAmount, String color, String image) {
        super(id, abbreviation, title, position, childAmount,color,image);
        this.testament = testament;
        this.bibleInfoId = bibleInfoId;



    }

    public int getTestament() {
        return testament;
    }

    public void setTestament(int testament) {
        this.testament = testament;
    }
}
