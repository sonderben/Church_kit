package com.churchkit.churchkit.modelview.bible;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.churchkit.churchkit.repository.bible.BibleRepositoryGeneral4Insert;

public class BibleViewModelGeneral4Insert extends AndroidViewModel {
    BibleRepositoryGeneral4Insert srg4i;
    public BibleViewModelGeneral4Insert(@NonNull Application application,String bibleName) {
        super(application);
       // BibleDaoGeneral4Insert dao = CKBibleDb.getInstance( application.getApplicationContext(),bibleName ).bibleDaoGeneral4Insert();
       // srg4i = new BibleRepositoryGeneral4Insert(dao);
    }
   /* public void insert(BibleBook bibleBook, List<BibleChapter> bibleChapters, List<BibleVerse>bibleVerses){
       srg4i.insert(bibleBook,bibleChapters,bibleVerses);
    }*/
}
