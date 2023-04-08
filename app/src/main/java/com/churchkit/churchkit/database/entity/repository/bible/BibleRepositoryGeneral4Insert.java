package com.churchkit.churchkit.database.entity.repository.bible;

import com.churchkit.churchkit.database.dao.bible.BibleDaoGeneral4Insert;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BibleRepositoryGeneral4Insert {

    BibleDaoGeneral4Insert bibleDaoGeneral4Insert;

    public BibleRepositoryGeneral4Insert(BibleDaoGeneral4Insert bibleDaoGeneral4Insert) {
        this.bibleDaoGeneral4Insert = bibleDaoGeneral4Insert;
    }
    public void insert(final BibleBook songBook, final List<BibleChapter>songs, final List<BibleVerse>verses){
        Flowable.fromAction(() -> {

                    bibleDaoGeneral4Insert.insertBibleBook(songBook);
                    bibleDaoGeneral4Insert.insertBibleChapter(new ArrayList<>(songs));
                    bibleDaoGeneral4Insert.insertBibleVerses(verses);

                    /*bibleDaoGeneral4Insert.insertBibleBook(songBook);
                    bibleDaoGeneral4Insert.insertBibleChapter(new ArrayList<>(songs));
                    bibleDaoGeneral4Insert.insertBibleVerses(new ArrayList<>(verses));*/


        })
                .subscribeOn(Schedulers.computation())
                .subscribe();

    }
}
