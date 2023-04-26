package com.churchkit.churchkit.database.dao.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.bible.BibleInfo;
import com.churchkit.churchkit.database.entity.song.SongInfo;

import java.util.List;

@Dao
public interface SongInfoDao extends BaseDao<SongInfo> {
    @Query("Select * from song_info")
    LiveData<List<SongInfo>>getAllSongInfo();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<SongInfo>songInfoList);
}
