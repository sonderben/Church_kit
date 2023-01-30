package com.churchkit.churchkit.database.entity.bible;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bible_book")
public class BibleBook {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bible_book_id")
    private long bibleBookId;
    private String abbreviation;
    private String title;
    private short position; // for order
    private short testament;

}
