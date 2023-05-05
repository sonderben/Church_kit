package com.churchkit.churchkit.modelview.bible;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.dao.bible.BookMarkBibleDao;
import com.churchkit.churchkit.database.entity.bible.BookMarkChapter;
import com.churchkit.churchkit.repository.bible.BibleBookMarkRepository;

import java.util.List;

public class BibleBookMarkViewModel extends AndroidViewModel {
    BibleBookMarkRepository repository;
    BookMarkBibleDao dao;
    public BibleBookMarkViewModel(@NonNull Application application) {
        super(application);
        dao = CKBibleDb.getInstance( application.getApplicationContext() ).bookMarkBibleDao();
        repository = new BibleBookMarkRepository(dao);

    }

    public LiveData<List<BookMarkChapter>> getAllBookMark(String chapterId){
        return repository.getAllBookMark(chapterId);
    }
    public void insert(BookMarkChapter bookMarkChapter){
        repository.insert( bookMarkChapter );
    }
    public void delete (BookMarkChapter bookMarkChapter){
        repository.delete( bookMarkChapter );
    }
}
