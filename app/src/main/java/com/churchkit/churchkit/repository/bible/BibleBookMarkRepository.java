package com.churchkit.churchkit.repository.bible;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.bible.BookMarkBibleDao;
import com.churchkit.churchkit.database.entity.bible.BookMarkChapter;
import com.churchkit.churchkit.repository.song.BaseRepository;

import java.util.List;

public class BibleBookMarkRepository extends BaseRepository<BookMarkBibleDao, BookMarkChapter> {
    public BibleBookMarkRepository(BookMarkBibleDao bookMarkSongDao) {
        super(bookMarkSongDao);
    }


    public LiveData<List<BookMarkChapter>> getAllBookMark(String chapterId){
        return dao.getAllBookMark(chapterId);
    }

}
