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
    LiveData<Integer> amountBookOldTestament;
    LiveData<Integer> amountBookNewTestament;

    public BibleBookViewModel(@NonNull Application application) {
        super(application);
        BibleBookDao dao = CKBibleDb.getInstance( application.getApplicationContext() ).bibleBookDao();
        repository = new BibleBookRepository(dao);
        amountBookOldTestament = repository.getAmountBookOldTestament();
        amountBookNewTestament = repository.getAmountBookNewTestament();
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
    public LiveData<Integer>getAmountBookOldTestament()  {
            return amountBookOldTestament;
    }
    public LiveData<Integer>getAmountBookNewTestament()  {
        return amountBookNewTestament;
    }


    public int getAmountBookOldTest()  {
        return amountBookOldTestament.getValue();
    }
    public int getAmountBookNewTest()  {
        return amountBookNewTestament.getValue();
    }
}
