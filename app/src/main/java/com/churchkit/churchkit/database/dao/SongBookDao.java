package com.churchkit.churchkit.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.churchkit.churchkit.database.entity.SongBook;

import java.util.List;

@Dao
public interface SongBookDao {
    @Query("SELECT * FROM song_book ORDER BY num")
    LiveData<List<SongBook>> getAllSongBook();

    @Transaction
    @Insert
    long insert(SongBook songBook);
    @Transaction
    @Insert
    List<Long> insertAll(List<SongBook> songBooks);
}
