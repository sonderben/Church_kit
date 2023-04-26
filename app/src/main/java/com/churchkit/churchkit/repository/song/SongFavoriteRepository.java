package com.churchkit.churchkit.repository.song;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.song.SongFavoriteDao;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongFavorite;

import java.util.Map;

public class SongFavoriteRepository extends BaseRepository<SongFavoriteDao, SongFavorite>{
    public SongFavoriteRepository(SongFavoriteDao songHistoryDao) {
        super(songHistoryDao);

    }
    public LiveData<SongFavorite> getSongFavoriteWithSongId(String id){
       return dao.getSongFavoriteWithSongId(id);
    }
    /*public SongFavorite isExisted(String id){
        return songHistoryDao.isExisted(id);
    }*/
    public LiveData<Integer> getAmount(){
        return dao.getAmount();
    }
    public LiveData<Map<SongFavorite, Song>> loadUserAndBookNames(){
        return dao.getAllSongFavoriteWithSong();
    }
}
