package com.churchkit.churchkit.repository.bible;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.bible.BibleVerseDao;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.repository.song.BaseRepository;

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

    public LiveData<List<BibleVerse>> fullTextSearch(String bibleInfoId, String textSearch){
        return dao.fullTextSearch(bibleInfoId,textSearch);
    }
    public LiveData<List<BibleVerse>> normalSearch(String bibleInfoId, String textSearch){
        return dao.normalSearch(bibleInfoId,textSearch);
    }
}
