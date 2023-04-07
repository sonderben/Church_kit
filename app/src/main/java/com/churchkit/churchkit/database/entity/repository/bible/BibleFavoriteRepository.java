package com.churchkit.churchkit.database.entity.repository.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.bible.BibleChapterFavoriteDao;
import com.churchkit.churchkit.database.dao.song.SongFavoriteDao;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleChapterFavorite;
import com.churchkit.churchkit.database.entity.repository.song.BaseRepository;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongFavorite;

import java.util.Map;

public class BibleFavoriteRepository extends BaseRepository<BibleChapterFavoriteDao, BibleChapterFavorite> {
    public BibleFavoriteRepository(BibleChapterFavoriteDao bibleChapterFavoriteDao) {
        super(bibleChapterFavoriteDao);

    }

    public LiveData< Map<BibleChapterFavorite, BibleChapter> > loadFavoritesChapter(){
        return dao.loadFavoritesChapter();
    }


    public LiveData <BibleChapterFavorite>  existed(String id){
        return dao.existed(id);
    }



    public BibleChapterFavorite  isExisted(String id){
        return dao.isExisted(id);
    }
}
