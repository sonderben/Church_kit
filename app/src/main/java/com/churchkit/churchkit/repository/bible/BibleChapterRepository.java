package com.churchkit.churchkit.repository.bible;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.bible.BibleChapterDao;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.repository.song.BaseRepository;

import java.util.List;

public class BibleChapterRepository extends BaseRepository<BibleChapterDao, BibleChapter> {
    public BibleChapterRepository(BibleChapterDao bibleChapterDao) {
        super(bibleChapterDao);
    }


    public LiveData<List<BibleChapter>> getAllChapterByBookId(String id){
        return dao.getAllChapterByBookId(id);
    }

    public LiveData<List<BibleChapter>> getAllChapter(){
        return dao.getAllChapter();
    }

    public LiveData< List<BibleChapter> > bibleChapterFullTextSearch(String query){
        return dao.bibleChapterFullTextSearch(query);
    }

    public LiveData<BibleChapter> getChapterByVerseId(String verseId){
        return dao.getChapterByVerseId(verseId);
    }



}
