package com.churchkit.churchkit.repository.song;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.song.SongHistoryDao;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongHistory;

import java.util.Map;

public class SongHistoryRepository extends BaseRepository<SongHistoryDao,SongHistory>{


    public SongHistoryRepository(SongHistoryDao songHistoryDao) {
        super(songHistoryDao);
    }


    public LiveData<Map<SongHistory, Song>> getAllHistory(String songInfoId){
        return dao.loadHistorySong(songInfoId);
    }

    public LiveData<Integer> getAmount(String songInfoId){
        return dao.getAmount(songInfoId);
    }
}
