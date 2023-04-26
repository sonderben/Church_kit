package com.churchkit.churchkit.repository.bible;

import androidx.annotation.MainThread;

import com.churchkit.churchkit.database.dao.bible.BibleDaoGeneral4Insert;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BibleRepositoryGeneral4Insert {

  /*  BibleDaoGeneral4Insert bibleDaoGeneral4Insert;

    public BibleRepositoryGeneral4Insert(BibleDaoGeneral4Insert bibleDaoGeneral4Insert) {
        this.bibleDaoGeneral4Insert = bibleDaoGeneral4Insert;
    }

    public void insert(final BibleBook songBook, final List<BibleChapter> songs, final List<BibleVerse> verses) {
        Flowable.fromAction(() -> {

                    bibleDaoGeneral4Insert.insertBibleBook(songBook);
                    bibleDaoGeneral4Insert.insertBibleChapter(new ArrayList<>(songs));
                    bibleDaoGeneral4Insert.insertBibleVerses(verses);
                })
                .subscribeOn(Schedulers.computation())
                .subscribe(new FlowableSubscriber<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Subscription s) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                        System.out.println("onComplete: " + verses);
                    }
                });


    }*/


}
