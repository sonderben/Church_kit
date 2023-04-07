package com.churchkit.churchkit.database.dao.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.song.Verse;

import java.util.List;

@Dao
public interface VerseDao extends BaseDao<Verse> {

    @Query("SELECT * FROM verse WHERE song_id = :idSong ORDER BY position")
    LiveData<List<Verse>> getAllVerseByIdSong(String idSong);

    @Query("SELECT * FROM verse  ORDER BY position")
    LiveData<List<Verse>> getAllVerses();

    /*@Transaction
    @Insert
    long insert(Verse verse);*/
   // @Transaction
    @Insert
    List<Long> insertAll(List<Verse> verses);

}
