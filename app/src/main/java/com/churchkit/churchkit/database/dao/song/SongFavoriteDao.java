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
    @Query("Select * from song_favorite Join song ON song.id = song_favorite.parent_id " +
            "where song_favorite.infoId = :songInfoId")
     LiveData< Map<SongFavorite, Song> > getAllSongFavoriteWithSong(String songInfoId);


    @Query("SELECT  * FROM song_favorite WHERE parent_id = :id and infoId = :songInfoId")
    LiveData <SongFavorite> getSongFavoriteWithSongId(String songInfoId,String id);

    @Query("Select COUNT(*) FROM song_favorite where infoId = :songInfoId")
    LiveData<Integer> getAmount(String songInfoId);


    /*@Query("SELECT  * FROM song_favorite WHERE parent_id = :id")
    SongFavorite  isExisted(String id);*/



}
