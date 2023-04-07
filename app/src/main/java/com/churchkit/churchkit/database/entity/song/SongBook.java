package com.churchkit.churchkit.database.entity.song;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.churchkit.churchkit.database.entity.base.Book;


@Entity(tableName = "song_book"/*,indices = {@Index(value = {"name"},unique = true)}*/)
public class SongBook extends Book {


    public SongBook(@NonNull String id, String abbreviation, String title, int position, int childAmount) {
        super(id, abbreviation, title, position, childAmount);
    }



}
