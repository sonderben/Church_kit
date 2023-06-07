package com.churchkit.churchkit.repository.bible;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.bible.BibleInfoDao;
import com.churchkit.churchkit.database.entity.bible.BibleInfo;
import com.churchkit.churchkit.repository.song.BaseRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BibleInfoRepository extends BaseRepository <BibleInfoDao, BibleInfo> {
    public BibleInfoRepository(BibleInfoDao songHistoryDao) {
        super(songHistoryDao);
    }


    public void insert(List<BibleInfo> bibleInfoList) {
        //dao.insert(bibleInfoList);
        Completable.fromAction( ()-> dao.insert(bibleInfoList) )
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public LiveData<List<BibleInfo>> getAllBibleInfo() {
        return dao.getAllBibleInfo();
    }
    public void update(BibleInfo bibleInfo){
        dao.update(bibleInfo);
    }
}
