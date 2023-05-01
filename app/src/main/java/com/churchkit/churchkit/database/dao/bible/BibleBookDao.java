package com.churchkit.churchkit.database.dao.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
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

   @Query("Select Count(*) from bible_book Where testament <0")
    LiveData<Integer>getAmountBookOldTestament();

    @Query("Select Count(*) from bible_book Where testament >0")
    LiveData<Integer>getAmountBookNewTestament();
}
