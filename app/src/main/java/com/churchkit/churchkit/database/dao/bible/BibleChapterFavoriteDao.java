package com.churchkit.churchkit.database.dao.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleChapterFavorite;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongFavorite;

import java.util.Map;


@Dao
public interface BibleChapterFavoriteDao {
    @Query("Select * from chapter_favorite Join bible_chapter ON bible_chapter.bible_chapter_id = chapter_favorite.bible_chapter_id")
     LiveData< Map<BibleChapterFavorite, BibleChapter> > loadFavoritesChapter();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     long insert(BibleChapterFavorite bibleChapterFavorite);

    //////


    @Query("SELECT  * FROM chapter_favorite WHERE bible_chapter_id = :id")
    LiveData <BibleChapterFavorite>  existed(String id);


    @Query("SELECT  * FROM chapter_favorite WHERE bible_chapter_id = :id")
    BibleChapterFavorite  isExisted(String id);

    @Delete
    void delete(BibleChapterFavorite chapterFavorite);





}
