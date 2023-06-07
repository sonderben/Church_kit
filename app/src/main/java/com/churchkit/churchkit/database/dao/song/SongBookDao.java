package com.churchkit.churchkit.database.dao.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.song.SongBook;

import java.util.List;

@Dao
public interface SongBookDao extends BaseDao<SongBook> {
    @Query("SELECT * FROM song_book where song_info_id =:id  ORDER BY position")
    LiveData<List<SongBook>> getAllSongBookBySongInfoId(String id);

    @Query("Delete from song_book where song_info_id = :songInfoId")
    void deleteAll(String songInfoId);
}
