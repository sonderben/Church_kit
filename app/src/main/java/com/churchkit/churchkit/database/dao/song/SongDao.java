package com.churchkit.churchkit.database.dao.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.song.Song;


import java.util.List;
@Dao
public interface SongDao extends BaseDao<Song> {

    @Query("Select * from song join song_fts on song.title = song_fts.title where song_fts match :query limit 50")
    LiveData< List<Song> > songFullTextSearch(String query);


    @Query("SELECT * FROM song where song.book_id = :songBookId ORDER BY position")
    LiveData< List<Song> > getAllSongWithVerseById(String songBookId);

    @Query("SELECT * FROM song WHERE id =:id")
    LiveData<Song> getSongById(String id);




}
