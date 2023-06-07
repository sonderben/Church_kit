package com.churchkit.churchkit.repository.song;

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

    }
    public LiveData<List<Verse>> getAllVerseByIdSong(String idSong){
        return dao.getAllVerseByIdSong(idSong);
    }

    public LiveData<List<Verse>> fullTextSearch(String songInfoId, String textSearch){
        return dao.fullTextSearch(songInfoId,textSearch);
    }

    public LiveData<List<Verse>> normalTextSearch (String songInfoId,String textSearch){
        return dao.normalTextSearch(songInfoId,textSearch);
    }
}


















