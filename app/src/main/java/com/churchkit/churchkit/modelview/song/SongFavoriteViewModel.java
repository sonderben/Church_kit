package com.churchkit.churchkit.modelview.song;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.CKSongDb;
import com.churchkit.churchkit.repository.song.SongFavoriteRepository;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongFavorite;

import java.util.Map;

public class SongFavoriteViewModel extends AndroidViewModel {
    //LiveData<Map<SongFavorite, Song>> songWithHistory;
    SongFavoriteRepository shr ;
    public SongFavoriteViewModel(@NonNull Application application) {
        super(application);
        shr = new SongFavoriteRepository( CKSongDb.getInstance( application.getApplicationContext() ).songFavoriteDao() );
        //songWithHistory = shr.loadUserAndBookNames();
    }

    public LiveData<Map<SongFavorite, Song>> getSongFavorite(String songInfoId) {
        return shr.loadUserAndBookNames(songInfoId);
    }
    public LiveData<SongFavorite> getSongFavoriteWithSongId(String songInfoId,String id){
        return shr.getSongFavoriteWithSongId(songInfoId,id);
    }
    public void insert(SongFavorite sf){
        shr.insert(sf);
    }
    public void delete(SongFavorite sf){
        shr.delete(sf);
    }
    public LiveData<Integer> getAmount(String songInfoId){
       return shr.getAmount(songInfoId);
    }
}
