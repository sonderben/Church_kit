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
    public LiveData<SongFavorite> getSongFavoriteWithSongId(String songInfoId,String id){
       return dao.getSongFavoriteWithSongId(songInfoId,id);
    }
    /*public SongFavorite isExisted(String id){
        return songHistoryDao.isExisted(id);
    }*/
    public LiveData<Integer> getAmount(String songInfoId){
        return dao.getAmount(songInfoId);
    }
    public LiveData<Map<SongFavorite, Song>> loadUserAndBookNames(String songInfoId){
        return dao.getAllSongFavoriteWithSong(songInfoId);
    }
}
