package com.churchkit.churchkit.modelview.bible;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.dao.bible.BibleBookDao;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.repository.bible.BibleBookRepository;

import java.util.List;

public class BibleBookViewModel extends AndroidViewModel {
    BibleBookRepository repository;
    public BibleBookViewModel(@NonNull Application application) {
        super(application);
        BibleBookDao dao = ChurchKitDb.getInstance(application.getApplicationContext()).bibleBookDao();
        repository = new BibleBookRepository(dao);
    }

    public LiveData<List<BibleBook>> getAllBibleBook(){
        return repository.getAllBibleBook();
    }


    public LiveData<List<BibleBook>> getAllNewTestamentBibleBook(){
        return repository.getAllNewTestamentBibleBook();
    }


    public LiveData<List<BibleBook>> getAllOldTestamentBibleBook(){
        return repository.getAllOldTestamentBibleBook();
    }
}
