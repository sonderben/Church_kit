package com.churchkit.churchkit.repository.bible;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.bible.BibleBookDao;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.repository.song.BaseRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BibleBookRepository extends BaseRepository<BibleBookDao, BibleBook> {
    public BibleBookRepository(BibleBookDao bibleBookDao) {
        super(bibleBookDao);
    }


    public LiveData<List<BibleBook>> getAllBibleBookByIdBibleInfo(String bibleInfoId){
        return dao.getAllBibleBookByIdBibleInfo(bibleInfoId);
    }


    public LiveData<List<BibleBook>> getAllNewTestamentBibleBook(String bibleInfoId){
        return dao.getAllNewTestamentBibleBook(bibleInfoId);
    }


    public LiveData<List<BibleBook>> getAllOldTestamentBibleBook(String bibleInfoId){
        return dao.getAllOldTestamentBibleBook(bibleInfoId);
    }

    public void deleteAll(String bibleInfoId){

        Completable.fromAction( ()-> dao.deleteAll( bibleInfoId ) )
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }



    public LiveData<Integer>getAmountBookOldTestament(String bibleInfoId)  {
            return dao.getAmountBookOldTestament(bibleInfoId);
    }
    public LiveData<Integer>getAmountBookNewTestament(String bibleInfoId)  {
        return dao.getAmountBookNewTestament(bibleInfoId);
    }
}
