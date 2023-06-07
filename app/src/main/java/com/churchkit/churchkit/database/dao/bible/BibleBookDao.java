package com.churchkit.churchkit.database.dao.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.bible.BibleBook;

import java.util.List;
@Dao
public interface BibleBookDao extends BaseDao<BibleBook> {
    @Query("SELECT * FROM BIBLE_BOOK where bible_info_id = :bibleInfoId ORDER BY testament, position ")
     LiveData<List<BibleBook>> getAllBibleBookByIdBibleInfo(String bibleInfoId);

    @Query("SELECT * FROM BIBLE_BOOK WHERE bible_info_id = :bibleInfoId and  testament  > 0 ORDER BY position")
    LiveData<List<BibleBook>> getAllNewTestamentBibleBook(String bibleInfoId);

    @Query("SELECT * FROM BIBLE_BOOK  WHERE bible_info_id = :bibleInfoId and testament  < 0 ORDER BY position")
    LiveData<List<BibleBook>> getAllOldTestamentBibleBook(String bibleInfoId);

   @Query("Select Count(*) from bible_book Where bible_info_id = :bibleInfoId and testament <0")
    LiveData<Integer>getAmountBookOldTestament(String bibleInfoId);

    @Query("Select Count(*) from bible_book Where bible_info_id = :bibleInfoId and testament >0")
    LiveData<Integer>getAmountBookNewTestament(String bibleInfoId);

    @Query("Delete  from BIBLE_BOOK where BIBLE_BOOK.bible_info_id = :bibleInfoId")
    void deleteAll(String bibleInfoId);
}
