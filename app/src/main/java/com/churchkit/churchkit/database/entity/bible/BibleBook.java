package com.churchkit.churchkit.database.entity.bible;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.churchkit.churchkit.database.entity.base.Book;

@Entity(tableName = "bible_book")
public class BibleBook extends Book {

    private int testament;


    public BibleBook(@NonNull String id, String abbreviation, String title, int position,int testament, int childAmount) {
        super(id, abbreviation, title, position, childAmount);
        this.testament = testament;
    }

    public int getTestament() {
        return testament;
    }

    public void setTestament(int testament) {
        this.testament = testament;
    }
}
