package com.churchkit.churchkit.modelview.bible;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.dao.bible.BibleBookDao;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.repository.bible.BibleBookRepository;

import java.util.List;

public class BibleBookViewModel extends AndroidViewModel {
    BibleBookRepository repository;

    public BibleBookViewModel(@NonNull Application application) {
        super(application);
        BibleBookDao dao = CKBibleDb.getInstance( application.getApplicationContext() ).bibleBookDao();
        repository = new BibleBookRepository(dao);
    }
    public void delete(BibleBook bibleBook){
        repository.delete(bibleBook);
    }
    public void deleteAll(String bibleInfoId){
        repository.deleteAll(bibleInfoId);
    }
    public LiveData<List<BibleBook>> getAllBibleBook(String bibleInfoId){
        return repository.getAllBibleBookByIdBibleInfo(bibleInfoId);
    }


    public LiveData<List<BibleBook>> getAllNewTestamentBibleBook(String bibleInfoId){
        return repository.getAllNewTestamentBibleBook(bibleInfoId);
    }


    public LiveData<List<BibleBook>> getAllOldTestamentBibleBook(String bibleInfoId){
        return repository.getAllOldTestamentBibleBook(bibleInfoId);
    }






}
