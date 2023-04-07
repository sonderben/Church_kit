package com.churchkit.churchkit.database.entity.repository.bible;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.bible.BibleBookDao;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.repository.song.BaseRepository;

import java.util.List;

public class BibleBookRepository extends BaseRepository<BibleBookDao, BibleBook> {
    public BibleBookRepository(BibleBookDao bibleBookDao) {
        super(bibleBookDao);
    }


    public LiveData<List<BibleBook>> getAllBibleBook(){
        return dao.getAllBibleBook();
    }


    public LiveData<List<BibleBook>> getAllNewTestamentBibleBook(){
        return dao.getAllNewTestamentBibleBook();
    }


    public LiveData<List<BibleBook>> getAllOldTestamentBibleBook(){
        return dao.getAllOldTestamentBibleBook();
    }
}
