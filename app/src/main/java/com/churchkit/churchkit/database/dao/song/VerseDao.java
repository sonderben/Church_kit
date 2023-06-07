package com.churchkit.churchkit.database.dao.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.song.Verse;

import java.util.List;

@Dao
public interface VerseDao extends BaseDao<Verse> {

    @Query("SELECT * FROM verse WHERE song_id = :idSong ORDER BY position")
    LiveData<List<Verse>> getAllVerseByIdSong(String idSong);

    @Query("SELECT * FROM verse  ORDER BY position")
    LiveData<List<Verse>> getAllVerses();

    @Query("Select * from verse join song_verse_fts on verse.verseId = " +
            "song_verse_fts.verseId " +
            "WHERE infoId = :songInfoId and song_verse_fts match :textSearch limit 50" /*+*/
            /*"order by song_verse_fts.reference"*/)
    LiveData<List<Verse>> fullTextSearch(String songInfoId, String textSearch);


    @Query("Select * from verse " +
            "WHERE infoId = :songInfoId and (verse LIKE :textSearch OR reference LIKE :textSearch)  " +
            "order by reference LIMIT 50")
    LiveData<List<Verse>> normalTextSearch(String songInfoId, String textSearch);
    @Insert
    void insertAll(List<Verse> verses);

}













