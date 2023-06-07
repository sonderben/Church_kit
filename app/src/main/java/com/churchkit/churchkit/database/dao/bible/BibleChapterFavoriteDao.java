package com.churchkit.churchkit.database.dao.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleChapterFavorite;

import java.util.Map;


@Dao
public interface BibleChapterFavoriteDao extends BaseDao<BibleChapterFavorite> {
    @Query("Select * from chapter_favorite Join bible_chapter ON bible_chapter.id = chapter_favorite.parent_id where chapter_favorite.infoId =:bibleInfoId")
     LiveData< Map<BibleChapterFavorite, BibleChapter> > loadFavoritesChapter(String bibleInfoId);


    @Query("SELECT  * FROM chapter_favorite WHERE parent_id = :id and infoId = :bibleInfoId")
    LiveData <BibleChapterFavorite>  existed(String id,String bibleInfoId);


    @Query("SELECT  * FROM chapter_favorite WHERE parent_id = :id and infoId = :bibleInfoId")
    BibleChapterFavorite  isExisted(String id,String bibleInfoId);


    @Query("Select COUNT(*) FROM chapter_favorite where infoId = :bibleInfoId")
    LiveData<Integer> getAmount(String bibleInfoId);




}
