package com.churchkit.churchkit.modelview.song;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.CKSongDb;
import com.churchkit.churchkit.repository.song.SongHistoryRepository;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongHistory;

import java.util.Map;

public class SongHistoryViewModel extends AndroidViewModel {
    //LiveData<Map<SongHistory, Song>> songWithHistory;
    SongHistoryRepository shr ;

    public SongHistoryViewModel(@NonNull Application application) {
        super(application);
        shr = new SongHistoryRepository( CKSongDb.getInstance( application.getApplicationContext() ).songHistoryDao() );
        //songWithHistory = shr.getAllHistory();
    }


    public LiveData<Map<SongHistory, Song>> getSongWithHistory(String songInfoId) {
        return shr.getAllHistory(songInfoId);
    }
    public void insert(SongHistory songHistory){
        shr.insert(songHistory);
    }
    public void delete(SongHistory songHistory){
        shr.delete(songHistory);
    }
    public LiveData<Integer> getAmount(String songInfoId){
        return shr.getAmount(songInfoId);
    }
}
