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

   /* @Query("""
  SELECT *
  FROM launches
  JOIN launches_fts ON launches.name = launches_fts.name
  WHERE launches_fts MATCH :query
""")
    suspend fun search(query: String): List<Launc
    /////////

    @Query("Select * from song_favorite Join song ON song.song_id = song_favorite.song_id")
    public LiveData< Map<SongFavorite, Song> > loadUserAndBookNames();

    h>*/



    @Query("Select * from song join song_fts on song.title = song_fts.title where song_fts match :query")
    LiveData< List<Song> > songFullTextSearch(String query);


//    LiveData< List<Song> > songAndSongBookFullTextSearch(String query);



    @Query("SELECT * FROM song where song_book_id = :songBookId ORDER BY position")
    LiveData< List<Song> > getAllSongWithVerseById(String songBookId);



    //@Transaction
    @Insert
    long insert(Song song);
    @Transaction
    @Insert
    List<Long> insertAll(List<Song> songs);
}
