package com.churchkit.churchkit.modelview.song;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.CKSongDb;
import com.churchkit.churchkit.database.entity.song.SongInfo;
import com.churchkit.churchkit.repository.song.SongInfoRepository;

import java.util.List;

public class SongInfoViewModel extends AndroidViewModel {
    SongInfoRepository songInfoRepository;
    public SongInfoViewModel(@NonNull Application application) {
        super(application);
        songInfoRepository = new SongInfoRepository( CKSongDb.getInstance(application).songInfoDao() );// ;

    }



    public void  insert (List<SongInfo>bibleInfoList){
        songInfoRepository.insert(bibleInfoList);
    }
    public void insert(SongInfo bibleInfo){
        songInfoRepository.insert(bibleInfo);
    }

    public void delete(SongInfo songInfo){
        songInfoRepository.update(songInfo);
    }


    public LiveData<List<SongInfo>> getAllSongInfo(){
        return songInfoRepository.getAllSongInfo();
    }
}
