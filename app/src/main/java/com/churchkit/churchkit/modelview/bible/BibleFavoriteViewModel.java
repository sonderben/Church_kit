package com.churchkit.churchkit.modelview.bible;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleChapterFavorite;
import com.churchkit.churchkit.database.entity.repository.bible.BibleChapterRepository;
import com.churchkit.churchkit.database.entity.repository.bible.BibleFavoriteRepository;
import com.churchkit.churchkit.database.entity.repository.song.SongFavoriteRepository;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongFavorite;

import java.util.List;
import java.util.Map;

public class BibleFavoriteViewModel extends AndroidViewModel {
    LiveData<Map<BibleChapterFavorite, BibleChapter>> songWithHistory;
    BibleFavoriteRepository shr ;
    public BibleFavoriteViewModel(@NonNull Application application) {
        super(application);
        shr = new BibleFavoriteRepository( ChurchKitDb.getInstance(application.getApplicationContext()).bibleChapterFavoriteDao() );
        songWithHistory = shr.loadFavoritesChapter();
    }

    public LiveData< Map<BibleChapterFavorite, BibleChapter> > loadFavoritesChapter(){
        return songWithHistory;
    }


    public LiveData <BibleChapterFavorite>  existed(String id){
        return shr.existed(id);
    }



    public BibleChapterFavorite  isExisted(String id){
        return shr.isExisted(id);
    }

    public void insert(BibleChapterFavorite bibleChapterFavorite){
        shr.insert(bibleChapterFavorite);
    }
    public void delete(BibleChapterFavorite bibleChapterFavorite){
        shr.delete(bibleChapterFavorite);
    }
    public LiveData<Integer> getAmount(){
        return shr.getAmount();
    }

}
