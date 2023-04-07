package com.churchkit.churchkit.modelview.song;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.dao.song.SongBookDao;
import com.churchkit.churchkit.database.entity.repository.song.SongBookRepository;
import com.churchkit.churchkit.database.entity.song.SongBook;

import java.util.List;

public class SongBookViewModel extends AndroidViewModel {
    SongBookRepository repository;
    public SongBookViewModel(@NonNull Application application) {
        super(application);
        SongBookDao dao = ChurchKitDb.getInstance(application.getApplicationContext()).songBookDao();
        repository = new SongBookRepository(dao);
    }
    public LiveData<List<SongBook>> getAllSongBook(){
        return repository.getAllSongBook();
    }

    public void insert(SongBook songBook) {
        repository.insert(songBook);
    }
}
