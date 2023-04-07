package com.churchkit.churchkit.database.dao.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongFavorite;
import com.churchkit.churchkit.database.entity.song.SongHistory;

import java.util.Map;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface SongHistoryDao extends BaseDao<SongHistory> {
    @Query("Select * from song_history Join song ON song.id = song_history.parent_id")
     LiveData< Map<SongHistory, Song> > loadHistorySong();




    @Query("SELECT  * FROM song_history WHERE parent_id = :id")
    LiveData <SongHistory>  existed(String id);


    @Query("SELECT  * FROM song_history WHERE parent_id = :id")
    SongHistory  isExisted(String id);







}
