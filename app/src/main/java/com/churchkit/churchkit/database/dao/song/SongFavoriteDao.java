package com.churchkit.churchkit.database.dao.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongFavorite;

import java.util.Map;


@Dao
public interface SongFavoriteDao extends BaseDao<SongFavorite> {
    @Query("Select * from song_favorite Join song ON song.id = song_favorite.parent_id")
     LiveData< Map<SongFavorite, Song> > getAllSongFavoriteWithSong();


    @Query("SELECT  * FROM song_favorite WHERE parent_id = :id")
    LiveData <SongFavorite> getSongFavoriteWithSongId(String id);


    /*@Query("SELECT  * FROM song_favorite WHERE parent_id = :id")
    SongFavorite  isExisted(String id);*/



}
