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

    @Query("Select * from bible_verse join bible_verse_fts on bible_verse.bibleVerseId = " +
            "bible_verse_fts.bibleVerseId WHERE bible_verse_fts.verse_text match :textSearch and " +
            "bibleInfoId = :bibleInfoId " +
            "order by bible_verse_fts.reference limit 50")
    LiveData<List<BibleVerse>> fullTextSearch(String bibleInfoId, String textSearch);

    @Query("Select * from bible_verse  WHERE " +
            "bibleInfoId = :bibleInfoId AND (verse_text LIKE :textSearch OR reference LIKE :textSearch )" +
            " order by position limit 50")
    LiveData<List<BibleVerse>> normalSearch(String bibleInfoId, String textSearch);


    @Insert
    public void insertAll(List<BibleVerse> bibleVerseList);
}
