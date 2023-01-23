package com.churchkit.churchkit.database.entity;

import androidx.room.PrimaryKey;

public class SongBook {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private short color;
    private short image;
    private short num;
}
