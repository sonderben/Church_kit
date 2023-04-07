package com.churchkit.churchkit.modelview.bible;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.dao.bible.BibleChapterDao;
import com.churchkit.churchkit.database.dao.song.SongDao;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.repository.bible.BibleChapterRepository;
import com.churchkit.churchkit.database.entity.repository.song.SongRepository;
import com.churchkit.churchkit.database.entity.song.Song;

import java.util.List;

public class BibleChapterViewModel extends AndroidViewModel {
    BibleChapterRepository repository;
    public BibleChapterViewModel(@NonNull Application application) {
        super(application);
        BibleChapterDao dao = ChurchKitDb.getInstance(application.getApplicationContext()).bibleChapterDao();
        repository = new BibleChapterRepository(dao);


    }

    public LiveData<List<BibleChapter>> getAllChapterByBookId(String id){
        return repository.getAllChapterByBookId(id);
    }

    public LiveData<List<BibleChapter>> getAllChapter(){
        return repository.getAllChapter();
    }

    public LiveData< List<BibleChapter> > bibleChapterFullTextSearch(String query){
        return repository.bibleChapterFullTextSearch(query);
    }
    /*public void insert(BibleChapter bibleChapter){
        repository.insert(bibleChapter);
    }*/

}
