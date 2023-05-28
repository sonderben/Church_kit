package com.churchkit.churchkit.repository.song;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.song.SongDao;
import com.churchkit.churchkit.database.entity.song.Song;

import java.util.List;

public class SongRepository extends BaseRepository<SongDao, Song>{
    public SongRepository(SongDao songDao) {
        super(songDao);
    }


    public LiveData<List<Song>> songFullTextSearch(String query){
        return dao.songFullTextSearch(query);
    }


    public LiveData< List<Song> > getAllSongWithVerseById(String songBookId){
        return dao.getAllSongWithVerseById(songBookId);
    }

    public LiveData<Song> getSongById(String id){
        return dao.getSongById(id);
    }

    public LiveData<List<Song>> getAllSong(){
        return dao.getAllSong();
    }

}
