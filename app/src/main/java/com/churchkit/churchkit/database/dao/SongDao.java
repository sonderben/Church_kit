package com.churchkit.churchkit.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.churchkit.churchkit.Model.song.SongWithVerse;
import com.churchkit.churchkit.database.entity.Song;


import java.util.List;
@Dao
public interface SongDao {
    @Transaction
    @Query("SELECT * FROM song")
    LiveData< List<SongWithVerse> > getAllSongWithVerse();

    @Transaction
    @Insert
    long insert(Song song);
    @Transaction
    @Insert
    List<Long> insertAll(List<Song> songs);
}
