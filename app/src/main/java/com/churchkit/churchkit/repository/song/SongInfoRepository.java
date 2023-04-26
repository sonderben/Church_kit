package com.churchkit.churchkit.repository.song;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.bible.BibleInfoDao;
import com.churchkit.churchkit.database.dao.song.SongInfoDao;
import com.churchkit.churchkit.database.entity.bible.BibleInfo;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongInfo;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SongInfoRepository extends BaseRepository <SongInfoDao, SongInfo> {
    public SongInfoRepository(SongInfoDao songInfoDao) {
        super(songInfoDao);
    }


    public void insert(List<SongInfo> songInfoList) {
        Completable.fromAction( ()-> dao.insert(songInfoList) )
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public LiveData<List<SongInfo>> getAllSongInfo() {
        return dao.getAllSongInfo();
    }
}
