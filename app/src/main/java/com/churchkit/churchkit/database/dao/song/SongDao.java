package com.churchkit.churchkit.database.dao.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.churchkit.churchkit.database.entity.song.Song;


import java.util.List;
@Dao
public interface SongDao {


    @Query("SELECT * FROM song where song_book_id = :songBookId ORDER BY position")
    LiveData< List<Song> > getAllSongWithVerseById(String songBookId);



    //@Transaction
    @Insert
    long insert(Song song);
    @Transaction
    @Insert
    List<Long> insertAll(List<Song> songs);
}
