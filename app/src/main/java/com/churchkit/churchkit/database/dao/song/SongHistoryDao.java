package com.churchkit.churchkit.database.dao.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongFavorite;
import com.churchkit.churchkit.database.entity.song.SongHistory;

import java.util.Map;

@Dao
public interface SongHistoryDao {
    @Query("Select * from song_history Join song ON song.song_id = song_history.song_id")
     LiveData< Map<SongHistory, Song> > loadHistorySong();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     long insert(SongHistory songHistory);


    @Query("SELECT  * FROM song_history WHERE song_id = :id")
    LiveData <SongHistory>  existed(String id);


    @Query("SELECT  * FROM song_history WHERE song_id = :id")
    SongHistory  isExisted(String id);

    @Delete
    void delete(SongHistory songHistory);





}
