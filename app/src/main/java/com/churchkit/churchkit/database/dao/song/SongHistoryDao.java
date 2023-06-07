package com.churchkit.churchkit.database.dao.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongHistory;

import java.util.Map;

@Dao
public interface SongHistoryDao extends BaseDao<SongHistory> {
    @Query("Select * from song_history Join song ON song.id = song_history.parent_id " +
            "where song_history.infoId = :songInfoId")
     LiveData< Map<SongHistory, Song> > loadHistorySong(String songInfoId);


    @Query("Select COUNT(*) FROM song_history where infoId = :songInfoId")
    LiveData<Integer> getAmount(String songInfoId);


    @Query("SELECT  * FROM song_history WHERE parent_id = :id")
    LiveData <SongHistory>  existed(String id);


    @Query("SELECT  * FROM song_history WHERE parent_id = :id")
    SongHistory  isExisted(String id);







}
