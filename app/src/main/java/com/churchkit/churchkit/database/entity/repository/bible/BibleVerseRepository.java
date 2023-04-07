package com.churchkit.churchkit.database.entity.repository.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.bible.BibleVerseDao;
import com.churchkit.churchkit.database.dao.song.VerseDao;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.database.entity.repository.song.BaseRepository;
import com.churchkit.churchkit.database.entity.song.Verse;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BibleVerseRepository extends BaseRepository<BibleVerseDao, BibleVerse> {
    public BibleVerseRepository(BibleVerseDao verseDao) {
        super(verseDao);
    }

    public void insertAll(List<BibleVerse> verses){
        Completable.fromAction(()->dao.insertAll(verses))
                .subscribeOn(Schedulers.computation())
                .subscribe();
    }

    public LiveData<List<BibleVerse>> getAllVerse(String id){
        return dao.getAllVerse(id);
    }

    public LiveData<List<BibleVerse>> getAllVerse(){
        return dao.getAllVerse();
    }
}
