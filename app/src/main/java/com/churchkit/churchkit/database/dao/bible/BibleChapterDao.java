package com.churchkit.churchkit.database.dao.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.song.Song;

import java.util.List;
@Dao
public interface BibleChapterDao {
    @Query("SELECT * FROM bible_chapter WHERE bible_book_id = :id")
    public LiveData<List<BibleChapter>> getAllChapterByBookId(String id);


    @Query("Select * from bible_chapter " +
            "join bible_chapter_fts on bible_chapter.position = bible_chapter_fts.position " +
            "and bible_chapter.bible_book_abbr = bible_chapter_fts.bible_book_abbr " +
            "where bible_chapter_fts match :query")
    LiveData< List<BibleChapter> > bibleChapterFullTextSearch(String query);



    @Insert
    public long insertChapter(BibleChapter bibleChapter);
}
