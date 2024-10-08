package com.churchkit.churchkit.repository.song;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.song.SongBookDao;
import com.churchkit.churchkit.database.entity.song.SongBook;

import java.util.List;

public class SongBookRepository extends BaseRepository<SongBookDao, SongBook>{
    public SongBookRepository(SongBookDao songBookDao) {
        super(songBookDao);
    }
    public LiveData<List<SongBook>> getAllSongBookBySongInfoId(String id){
        return dao.getAllSongBookBySongInfoId(id);
    }
}
