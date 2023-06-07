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

    public LiveData< Map<BibleChapterFavorite, BibleChapter> > loadFavoritesChapter(String bibleInfoId){
        return dao.loadFavoritesChapter(bibleInfoId);
    }


    public LiveData <BibleChapterFavorite>  existed(String id,String bibleInfoId){
        return dao.existed(id,bibleInfoId);
    }


    public LiveData<Integer> getAmount(String bibleInfoId){
        return dao.getAmount(bibleInfoId);
    }

    public BibleChapterFavorite  isExisted(String id,String bibleInfoId){
        return dao.isExisted(id,bibleInfoId);
    }
}
