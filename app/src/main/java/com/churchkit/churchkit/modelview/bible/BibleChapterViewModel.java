package com.churchkit.churchkit.modelview.bible;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.dao.bible.BibleChapterDao;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.repository.bible.BibleChapterRepository;

import java.util.List;

public class BibleChapterViewModel extends AndroidViewModel {
    BibleChapterRepository repository;
    public BibleChapterViewModel(@NonNull Application application) {
        super(application);
        BibleChapterDao dao = CKBibleDb.getInstance( application.getApplicationContext() ).bibleChapterDao();
        repository = new BibleChapterRepository(dao);


    }

    public LiveData<List<BibleChapter>> getAllChapterByBookId(String id){
        return repository.getAllChapterByBookId(id);
    }

    public LiveData<List<BibleChapter>> getAllChapter(){
        return repository.getAllChapter();
    }

    public LiveData< List<BibleChapter> > bibleChapterFullTextSearch(String query){
        return repository.bibleChapterFullTextSearch(query);
    }
    /*public void insert(BibleChapter bibleChapter){
        repository.insert(bibleChapter);
    }*/

    public LiveData<BibleChapter> getChapterByVerseId(String verseId){
        return repository.getChapterByVerseId(verseId);
    }

}
