package com.churchkit.churchkit.database.dao.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;

import java.util.List;
@Dao
public interface BibleChapterDao extends BaseDao<BibleChapter> {
    @Query("SELECT * FROM bible_chapter WHERE book_id = :id ORDER BY position")
     LiveData<List<BibleChapter>> getAllChapterByBookId(String id);

    @Query("SELECT * FROM bible_chapter  ORDER BY position")
    LiveData<List<BibleChapter>> getAllChapter();


   /* @Query("Select Distinct * from bible_chapter " +
            "join bible_chapter_fts on bible_chapter.position = bible_chapter_fts.position " +
            "and bible_chapter.book_abbreviation = bible_chapter_fts.book_abbreviation " +
            "where ( (bible_chapter_fts match :query) and (bible_chapter.infoId =:bibleInfoId) )  " +
            "ORDER by bible_chapter.position limit 50")
    LiveData< List<BibleChapter> > bibleChapterFullTextSearch(String bibleInfoId,String query);*/


    @Query("SELECT * from bible_chapter WHERE id = :id")
    LiveData<BibleChapter> getChapterByVerseId(String id);



   /* @Insert
    public long insertChapter(BibleChapter bibleChapter);*/
}
