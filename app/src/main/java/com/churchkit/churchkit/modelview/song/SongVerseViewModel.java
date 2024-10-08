package com.churchkit.churchkit.modelview.song;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.CKSongDb;
import com.churchkit.churchkit.database.dao.song.VerseDao;
import com.churchkit.churchkit.repository.song.SongVerseRepository;
import com.churchkit.churchkit.database.entity.song.Verse;

import java.util.List;

public class SongVerseViewModel extends AndroidViewModel {
    SongVerseRepository repository;
    public SongVerseViewModel(@NonNull Application application) {
        super(application);
        VerseDao verseDao = CKSongDb.getInstance( application.getApplicationContext() ).verseDao();
        repository = new SongVerseRepository(verseDao);
    }



    public void insertAll(List<Verse> verses){
         repository.insertAll(verses);
    }
    public LiveData<List<Verse>> getAllVerseByIdSong(String idSong){
        return repository.getAllVerseByIdSong(idSong);
    }
    public LiveData<List<Verse>> fullTextSearch(String songInfoId, String textSearch){
        return repository.fullTextSearch(songInfoId,textSearch);
    }

    public LiveData<List<Verse>> normalTextSearch(String songInfoId, String textSearch){
        return repository.normalTextSearch(songInfoId,textSearch);
    }
}










