package com.churchkit.churchkit.database.dao.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.churchkit.churchkit.database.entity.bible.BibleChapter;

import java.util.List;
@Dao
public interface BibleChapterDao {
    @Query("SELECT * FROM bible_chapter WHERE bible_book_id = :id")
    public LiveData<List<BibleChapter>> getAllChapterByBookId(long id);
    @Insert
    public long insertChapter(BibleChapter bibleChapter);
}
