package com.churchkit.churchkit.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Song {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private int num;
    @ColumnInfo(name = "id_song_book")
    private int idSongBook;
    private int page;
}
//researchSongTable