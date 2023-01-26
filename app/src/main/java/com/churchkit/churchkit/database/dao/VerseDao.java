package com.churchkit.churchkit.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.churchkit.churchkit.database.entity.Song;
import com.churchkit.churchkit.database.entity.Verse;

import java.util.List;

@Dao
public interface VerseDao {
    /*@Query("SELECT * FROM verse")
    LiveData<List<Verse>> getAllVerse();*/

    @Query("SELECT * FROM verse WHERE song_id = :idSong ORDER BY num")
    LiveData<List<Verse>> getAllVerseByIdSong(Long idSong);

    @Transaction
    @Insert
    long insert(Verse verse);
    @Transaction
    @Insert
    List<Long> insertAll(List<Verse> verses);

}
