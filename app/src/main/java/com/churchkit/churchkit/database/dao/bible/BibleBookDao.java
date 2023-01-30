package com.churchkit.churchkit.database.dao.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.churchkit.churchkit.database.entity.bible.BibleBook;

import java.util.List;
@Dao
public interface BibleBookDao {
    @Query("SELECT * FROM BIBLE_BOOK ORDER BY position")
    public LiveData<List<BibleBook>> getAllBibleBook();
    @Insert
    public long insertBibleBook(BibleBook bibleBook);
}
