package com.churchkit.churchkit.modelview.bible;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.dao.bible.BookMarkBibleDao;
import com.churchkit.churchkit.database.dao.song.BookMarkSongDao;
import com.churchkit.churchkit.database.entity.bible.BookMarkChapter;
import com.churchkit.churchkit.database.entity.repository.bible.BibleBookMarkRepository;
import com.churchkit.churchkit.database.entity.repository.song.SongBookMarkRepository;
import com.churchkit.churchkit.database.entity.song.BookMarkSong;

import java.util.List;

public class BibleBookMarkViewModel extends AndroidViewModel {
    BibleBookMarkRepository repository;
    BookMarkBibleDao dao;
    public BibleBookMarkViewModel(@NonNull Application application) {
        super(application);
        dao = ChurchKitDb.getInstance(application.getApplicationContext()).bookMarkBibleDao();
        repository = new BibleBookMarkRepository(dao);

    }

    public LiveData<List<BookMarkChapter>> getAllBookMark(String chapterId){
        return repository.getAllBookMark(chapterId);
    }
}
