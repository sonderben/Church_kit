package com.churchkit.churchkit.database.entity.repository.song;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.song.SongHistoryDao;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongHistory;

import java.util.Map;

public class SongHistoryRepository extends BaseRepository<SongHistoryDao,SongHistory>{


    public SongHistoryRepository(SongHistoryDao songHistoryDao) {
        super(songHistoryDao);
    }


    public LiveData<Map<SongHistory, Song>> getAllHistory(){
        return dao.loadHistorySong();
    }

    public LiveData<Integer> getAmount(){
        return dao.getAmount();
    }
}
