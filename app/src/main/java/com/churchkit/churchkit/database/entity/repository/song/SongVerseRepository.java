package com.churchkit.churchkit.database.entity.repository.song;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.song.VerseDao;
import com.churchkit.churchkit.database.entity.song.Verse;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SongVerseRepository extends BaseRepository<VerseDao, Verse>{
    public SongVerseRepository(VerseDao verseDao) {
        super(verseDao);
    }

    public void insertAll(List<Verse> verses){
        Completable.fromAction(()->dao.insertAll(verses))
                .subscribeOn(Schedulers.computation())
                .subscribe();
        //return dao.insertAll(verses);
    }
    public LiveData<List<Verse>> getAllVerseByIdSong(String idSong){
        return dao.getAllVerseByIdSong(idSong);
    }
}
