package com.churchkit.churchkit.database.dao.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.bible.BookMarkChapter;

import java.util.List;

@Dao
public interface BookMarkBibleDao extends BaseDao<BookMarkChapter> {


    @Query("Select * from book_mark_chapter  where bible_chapter_id = :chapterId")
    LiveData<List<BookMarkChapter>> getAllBookMark(String chapterId);


}
