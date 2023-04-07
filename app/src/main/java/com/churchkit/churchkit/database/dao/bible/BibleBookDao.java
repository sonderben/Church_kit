package com.churchkit.churchkit.database.dao.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.bible.BibleBook;

import java.util.List;
@Dao
public interface BibleBookDao extends BaseDao<BibleBook> {
    @Query("SELECT * FROM BIBLE_BOOK ORDER BY testament, position ")
     LiveData<List<BibleBook>> getAllBibleBook();

    @Query("SELECT * FROM BIBLE_BOOK WHERE testament  > 0 ORDER BY position")
    LiveData<List<BibleBook>> getAllNewTestamentBibleBook();

    @Query("SELECT * FROM BIBLE_BOOK WHERE testament  < 0 ORDER BY position")
    LiveData<List<BibleBook>> getAllOldTestamentBibleBook();

    /*@Insert
     long insertBibleBook(BibleBook bibleBook);*/
}
