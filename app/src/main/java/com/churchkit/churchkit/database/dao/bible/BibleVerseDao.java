package com.churchkit.churchkit.database.dao.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;


import java.util.List;

@Dao
public interface BibleVerseDao extends BaseDao<BibleVerse> {

    @Query("SELECT * FROM bible_verse WHERE bible_chapter_id = :id")
    LiveData<List<BibleVerse>> getAllVerse(String id);
    @Query("SELECT * FROM bible_verse ")
    LiveData<List<BibleVerse>> getAllVerse();


    @Insert
    List<Long> insertAll(List<BibleVerse> bibleVerseList);
}
