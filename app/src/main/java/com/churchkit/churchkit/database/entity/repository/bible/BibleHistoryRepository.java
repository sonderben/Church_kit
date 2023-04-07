package com.churchkit.churchkit.database.entity.repository.bible;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.bible.BibleChapterHistoryDao;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleChapterHistory;
import com.churchkit.churchkit.database.entity.repository.song.BaseRepository;

import java.util.Map;

public class BibleHistoryRepository extends BaseRepository<BibleChapterHistoryDao, BibleChapterHistory> {


    public BibleHistoryRepository(BibleChapterHistoryDao bibleChapterDao) {
        super(bibleChapterDao);
    }



    public LiveData< Map<BibleChapterHistory, BibleChapter> > loadHistoriesChapter(){
        return dao.loadHistoriesChapter();
    }




}
