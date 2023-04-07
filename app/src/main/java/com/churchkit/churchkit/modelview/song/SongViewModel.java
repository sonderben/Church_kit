package com.churchkit.churchkit.modelview.song;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.dao.song.SongDao;
import com.churchkit.churchkit.database.entity.repository.song.SongRepository;
import com.churchkit.churchkit.database.entity.song.Song;

import java.util.List;

public class SongViewModel extends AndroidViewModel {
    SongRepository repository;
    public SongViewModel(@NonNull Application application) {
        super(application);
        SongDao songDao = ChurchKitDb.getInstance(application.getApplicationContext()).songDao();
        repository = new SongRepository(songDao);


    }

    public LiveData<List<Song>> songFullTextSearch(String query){
        return repository.songFullTextSearch(query);
    }

    public void insert(Song song){
        repository.insert(song);
    }


    public LiveData< List<Song> > getAllSongWithVerseById(String songBookId){
        return repository.getAllSongWithVerseById(songBookId);
    }
}
