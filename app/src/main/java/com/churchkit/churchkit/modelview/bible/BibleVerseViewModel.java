package com.churchkit.churchkit.modelview.bible;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.dao.bible.BibleVerseDao;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.repository.bible.BibleVerseRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BibleVerseViewModel extends AndroidViewModel {
    BibleVerseRepository repository;
    public BibleVerseViewModel(@NonNull Application application) {
        super(application);
        BibleVerseDao verseDao = CKBibleDb.getInstance( application.getApplicationContext() ).bibleVerseDao();
        repository = new BibleVerseRepository(verseDao);
    }



    public void insertAll(List<BibleVerse> verses){
        Completable.fromAction(()->repository.insertAll(verses))
                .subscribeOn(Schedulers.computation())
                .subscribe();
    }

    public LiveData<List<BibleVerse>> getAllVerse(String id){
        return repository.getAllVerse(id);
    }

    public LiveData<List<BibleVerse>> getAllVerse(){
        return repository.getAllVerse();
    }
}
