package com.churchkit.churchkit.modelview.song;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.CKSongDb;
import com.churchkit.churchkit.database.dao.song.BookMarkSongDao;
import com.churchkit.churchkit.repository.song.SongBookMarkRepository;
import com.churchkit.churchkit.database.entity.song.BookMarkSong;

import java.util.List;

public class SongBookMarkViewModel extends AndroidViewModel {
    SongBookMarkRepository repository;
    BookMarkSongDao dao;
    //LiveData<List<BookMarkSong>> bookMarkLiveData;
    public SongBookMarkViewModel(@NonNull Application application) {
        super(application);
        dao = CKSongDb.getInstance( application.getApplicationContext() ).bookMarkSongDao();
        repository = new SongBookMarkRepository(dao);
        //bookMarkLiveData = dao.getAllBookMark();
    }

    public LiveData<List<BookMarkSong>> getAllBookMark(String songId){
        return dao.getAllBookMark(songId);
    }

    public void insert(BookMarkSong bookMarkSong){
        repository.insert(bookMarkSong);
    }
    public void delete(BookMarkSong bookMarkSong){
        repository.delete(bookMarkSong);
    }
}
