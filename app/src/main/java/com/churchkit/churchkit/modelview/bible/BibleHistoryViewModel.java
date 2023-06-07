package com.churchkit.churchkit.modelview.bible;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleChapterHistory;
import com.churchkit.churchkit.repository.bible.BibleHistoryRepository;

import java.util.Map;

public class BibleHistoryViewModel extends AndroidViewModel {
    //LiveData<Map<BibleChapterHistory, BibleChapter>> liveData ;
    BibleHistoryRepository shr;
    public BibleHistoryViewModel(@NonNull Application application) {
        super(application);
         shr = new BibleHistoryRepository( CKBibleDb.getInstance( application.getApplicationContext() ).bibleChapterHistoryDao() );

    }
    public LiveData<Integer> getAmount(String bibleInfoId){
        return shr.getAmount(bibleInfoId);
    }

    public LiveData< Map<BibleChapterHistory, BibleChapter> > loadHistoriesChapter(String bibleInfoId){
        return shr.loadHistoriesChapter(bibleInfoId);
    }
    public void insert(BibleChapterHistory bibleChapterHistory){
        shr.insert(bibleChapterHistory);
    }
}
