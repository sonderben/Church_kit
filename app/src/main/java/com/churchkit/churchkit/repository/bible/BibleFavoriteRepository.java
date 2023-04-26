package com.churchkit.churchkit.repository.bible;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.bible.BibleChapterFavoriteDao;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleChapterFavorite;
import com.churchkit.churchkit.repository.song.BaseRepository;

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


    public LiveData<Integer> getAmount(){
        return dao.getAmount();
    }

    public BibleChapterFavorite  isExisted(String id){
        return dao.isExisted(id);
    }
}
