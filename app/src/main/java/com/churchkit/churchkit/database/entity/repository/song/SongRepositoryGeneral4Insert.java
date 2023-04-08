package com.churchkit.churchkit.database.entity.repository.song;

import androidx.annotation.NonNull;

import com.churchkit.churchkit.database.dao.song.SongDaoGeneral4Insert;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.Verse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SongRepositoryGeneral4Insert {

    SongDaoGeneral4Insert songDaoGeneral4Insert;

    public SongRepositoryGeneral4Insert(SongDaoGeneral4Insert songDaoGeneral4Insert) {
        this.songDaoGeneral4Insert = songDaoGeneral4Insert;
    }
    public void insert(final SongBook songBook, final List<Song>songs, final List<Verse>verses){
        Flowable.fromAction(() -> {
           songDaoGeneral4Insert.insertSongBook(songBook);
           songDaoGeneral4Insert.insertSong(new ArrayList<>(songs));
           songDaoGeneral4Insert.insertSongVerses(verses);
        })
                .subscribeOn(Schedulers.computation())
                .subscribe();
    }

}
