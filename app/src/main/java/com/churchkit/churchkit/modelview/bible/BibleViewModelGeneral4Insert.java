package com.churchkit.churchkit.modelview.bible;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.dao.bible.BibleDaoGeneral4Insert;
import com.churchkit.churchkit.database.dao.song.SongDaoGeneral4Insert;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.database.entity.repository.bible.BibleRepositoryGeneral4Insert;
import com.churchkit.churchkit.database.entity.repository.song.SongRepositoryGeneral4Insert;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.Verse;

import java.util.List;

public class BibleViewModelGeneral4Insert extends AndroidViewModel {
    BibleRepositoryGeneral4Insert srg4i;
    public BibleViewModelGeneral4Insert(@NonNull Application application) {
        super(application);
        BibleDaoGeneral4Insert dao = ChurchKitDb.getInstance(application.getApplicationContext()).bibleDaoGeneral4Insert();
        srg4i = new BibleRepositoryGeneral4Insert(dao);
    }
    public void insert(BibleBook bibleBook, List<BibleChapter> bibleChapters, List<BibleVerse>bibleVerses){
       srg4i.insert(bibleBook,bibleChapters,bibleVerses);
    }
}
