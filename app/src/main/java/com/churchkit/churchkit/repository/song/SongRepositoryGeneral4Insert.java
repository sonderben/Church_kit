package com.churchkit.churchkit.repository.song;

import com.churchkit.churchkit.database.dao.song.SongDaoGeneral4Insert;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.Verse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SongRepositoryGeneral4Insert {

    SongDaoGeneral4Insert songDaoGeneral4Insert;

    public SongRepositoryGeneral4Insert(SongDaoGeneral4Insert songDaoGeneral4Insert) {
        this.songDaoGeneral4Insert = songDaoGeneral4Insert;
    }
    public void insert(final SongBook songBook, final Song songs, final List<Verse>verses){
        Flowable.fromAction(() -> {
           songDaoGeneral4Insert.insertSongBook(songBook);
           songDaoGeneral4Insert.insertSong( songs );
           songDaoGeneral4Insert.insertSongVerses(verses);
        })
                .subscribeOn(Schedulers.computation())
                .subscribe();
    }

}
