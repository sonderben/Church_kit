package com.churchkit.churchkit.modelview.song;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.CKSongDb;
import com.churchkit.churchkit.database.dao.song.SongBookDao;
import com.churchkit.churchkit.repository.song.SongBookRepository;
import com.churchkit.churchkit.database.entity.song.SongBook;

import java.util.List;

public class SongBookViewModel extends AndroidViewModel {
    SongBookRepository repository;
    public SongBookViewModel(@NonNull Application application) {
        super(application);
        SongBookDao dao = CKSongDb.getInstance( application.getApplicationContext() ).songBookDao();
        repository = new SongBookRepository(dao);
    }
    public LiveData<List<SongBook>> getAllSongBookBySongInfoId(String id){
        return repository.getAllSongBookBySongInfoId(id);
    }

    public void insert(SongBook songBook) {
        repository.insert(songBook);
    }
}
