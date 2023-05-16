package com.churchkit.churchkit.repository.song;

import com.churchkit.churchkit.database.dao.BaseDao;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class BaseRepository <T extends BaseDao<E>,E> {

    //abstract class BaseRepository<T extends BaseDao<ENTITY>, ENTITY>

    protected T dao;


    public BaseRepository(T  songHistoryDao) {
        this.dao = songHistoryDao;
    }
    public void insert(E entity){

        Completable.fromAction( ()-> dao.insert(entity) )
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
    public void insertAll(List<E> entity){

        Completable.fromAction( ()-> dao.insertAll(entity) )
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
    public void delete(E entity){
        Completable.fromAction(() -> dao.delete(entity))
                .subscribeOn(Schedulers.single())
                .subscribe();
    }
}
