package com.churchkit.churchkit.database.dao.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongFavorite;

import java.util.Map;
/*
"SELECT * FROM user" +
    "JOIN book ON user.id = book.user_id"

 */

@Dao
public interface SongFavoriteDao {
    @Query("Select * from song_favorite Join song ON song.song_id = song_favorite.song_id")
    public LiveData< Map<SongFavorite, Song> > loadUserAndBookNames();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public long insert(SongFavorite songFavorite);

    //////


    @Query("SELECT  * FROM song_favorite WHERE song_id = :id")
    LiveData <SongFavorite>  existed(String id);


    @Query("SELECT  * FROM song_favorite WHERE song_id = :id")
    SongFavorite  isExisted(String id);

    @Delete
    void delete(SongFavorite songFavorite);





}
