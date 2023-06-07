package com.churchkit.churchkit.modelview.song;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.CKSongDb;
import com.churchkit.churchkit.database.dao.song.SongDao;
import com.churchkit.churchkit.repository.song.SongRepository;
import com.churchkit.churchkit.database.entity.song.Song;

import java.util.List;

public class SongViewModel extends AndroidViewModel {
    SongRepository repository;
    public SongViewModel(@NonNull Application application) {
        super(application);
        SongDao songDao = CKSongDb.getInstance( application.getApplicationContext() ).songDao();
        repository = new SongRepository(songDao);


    }

   /* public LiveData<List<Song>> songFullTextSearch(String songInfoId,String query){
        return repository.songFullTextSearch(songInfoId,query);
    }*/

    public void insert(Song song){
        repository.insert(song);
    }


    public LiveData< List<Song> > getAllSongWithVerseById(String songInfoId,String songBookId){
        return repository.getAllSongWithVerseById(songInfoId,songBookId);
    }
    public LiveData<Song> getSongById(String id){
        return repository.getSongById(id);
    }

    public LiveData<List<Song>> getAllSong(String songInfoId){
        return repository.getAllSong(songInfoId);
    }
}
