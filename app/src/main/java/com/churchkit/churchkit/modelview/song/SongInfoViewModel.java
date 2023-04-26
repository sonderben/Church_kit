package com.churchkit.churchkit.modelview.song;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.ListDataDownloaded;
import com.churchkit.churchkit.database.entity.bible.BibleInfo;
import com.churchkit.churchkit.database.entity.song.SongInfo;
import com.churchkit.churchkit.repository.bible.BibleInfoRepository;
import com.churchkit.churchkit.repository.song.SongInfoRepository;

import java.util.List;

public class SongInfoViewModel extends AndroidViewModel {
    SongInfoRepository songInfoRepository;
    public SongInfoViewModel(@NonNull Application application) {
        super(application);
        songInfoRepository = new SongInfoRepository( ListDataDownloaded.getInstance(application).songInfoDao() );// ;

    }



    public void  insert (List<SongInfo>bibleInfoList){
        songInfoRepository.insert(bibleInfoList);
    }
    public void insert(SongInfo bibleInfo){
        songInfoRepository.insert(bibleInfo);
    }

    public LiveData<List<SongInfo>> getAllBibleInfo(){
        return songInfoRepository.getAllSongInfo();
    }
}
