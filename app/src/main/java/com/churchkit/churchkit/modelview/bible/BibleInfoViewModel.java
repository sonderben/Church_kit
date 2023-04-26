package com.churchkit.churchkit.modelview.bible;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.ListDataDownloaded;
import com.churchkit.churchkit.database.entity.bible.BibleInfo;
import com.churchkit.churchkit.repository.bible.BibleInfoRepository;

import java.util.List;

public class BibleInfoViewModel extends AndroidViewModel {
    BibleInfoRepository bibleInfoRepository;
    public BibleInfoViewModel(@NonNull Application application) {
        super(application);
        bibleInfoRepository = new BibleInfoRepository(ListDataDownloaded.getInstance(application).bibleInfoDao());// ;

    }



    public void  insert (List<BibleInfo>bibleInfoList){
        bibleInfoRepository.insert(bibleInfoList);
    }
    public void insert(BibleInfo bibleInfo){
        bibleInfoRepository.insert(bibleInfo);
    }

    public LiveData<List<BibleInfo>> getAllBibleInfo(){
        return bibleInfoRepository.getAllBibleInfo();
    }
}
