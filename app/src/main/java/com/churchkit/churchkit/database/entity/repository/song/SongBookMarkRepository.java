package com.churchkit.churchkit.database.entity.repository.song;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.song.BookMarkSongDao;
import com.churchkit.churchkit.database.entity.song.BookMarkSong;

import java.util.List;

public class SongBookMarkRepository extends BaseRepository<BookMarkSongDao, BookMarkSong>{
    public SongBookMarkRepository(BookMarkSongDao bookMarkSongDao) {
        super(bookMarkSongDao);
    }

    LiveData<List<BookMarkSong>> getAllBookMark(String songId){
        return dao.getAllBookMark(songId);
    }
}
