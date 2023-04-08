package com.churchkit.churchkit.database.dao.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleChapterFavorite;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongFavorite;

import java.util.Map;


@Dao
public interface BibleChapterFavoriteDao extends BaseDao<BibleChapterFavorite> {
    @Query("Select * from chapter_favorite Join bible_chapter ON bible_chapter.id = chapter_favorite.parent_id")
     LiveData< Map<BibleChapterFavorite, BibleChapter> > loadFavoritesChapter();


    @Query("SELECT  * FROM chapter_favorite WHERE parent_id = :id")
    LiveData <BibleChapterFavorite>  existed(String id);


    @Query("SELECT  * FROM chapter_favorite WHERE parent_id = :id")
    BibleChapterFavorite  isExisted(String id);


    @Query("Select COUNT(*) FROM chapter_favorite")
    LiveData<Integer> getAmount();




}
