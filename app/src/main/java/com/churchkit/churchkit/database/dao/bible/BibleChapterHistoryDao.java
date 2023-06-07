package com.churchkit.churchkit.database.dao.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleChapterHistory;

import java.util.Map;


@Dao
public interface BibleChapterHistoryDao extends BaseDao<BibleChapterHistory> {
    @Query("Select * from chapter_history " +
            "Join bible_chapter ON bible_chapter.id = chapter_history.parent_id " +
            "where chapter_history.infoId = :bibleInfoId")

     LiveData< Map<BibleChapterHistory, BibleChapter> > loadHistoriesChapter(String bibleInfoId);

    @Query("Select COUNT(*) FROM chapter_history where chapter_history.infoId = :bibleInfoId")
    LiveData<Integer> getAmount(String bibleInfoId);



    @Query("SELECT  * FROM chapter_history WHERE parent_id = :id")
    LiveData <BibleChapterHistory>  existed(String id);


    @Query("SELECT  * FROM chapter_history WHERE parent_id = :id")
    BibleChapterHistory  isExisted(String id);







}
