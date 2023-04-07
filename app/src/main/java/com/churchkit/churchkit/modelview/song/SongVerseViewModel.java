package com.churchkit.churchkit.modelview.song;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.dao.song.VerseDao;
import com.churchkit.churchkit.database.entity.repository.song.SongVerseRepository;
import com.churchkit.churchkit.database.entity.song.Verse;

import java.util.List;

public class SongVerseViewModel extends AndroidViewModel {
    SongVerseRepository repository;
    public SongVerseViewModel(@NonNull Application application) {
        super(application);
        VerseDao verseDao = ChurchKitDb.getInstance(application.getApplicationContext()).verseDao();
        repository = new SongVerseRepository(verseDao);
    }



    public void insertAll(List<Verse> verses){
         repository.insertAll(verses);
    }
    public LiveData<List<Verse>> getAllVerseByIdSong(String idSong){
        return repository.getAllVerseByIdSong(idSong);
    }
}
