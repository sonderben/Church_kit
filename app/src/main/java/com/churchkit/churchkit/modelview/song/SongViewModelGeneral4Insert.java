package com.churchkit.churchkit.modelview.song;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.churchkit.churchkit.database.CKBibleDb;
import com.churchkit.churchkit.database.CKSongDb;
import com.churchkit.churchkit.database.dao.song.SongDaoGeneral4Insert;
import com.churchkit.churchkit.repository.song.SongRepositoryGeneral4Insert;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.Verse;

import java.util.List;

public class SongViewModelGeneral4Insert extends AndroidViewModel {
    SongRepositoryGeneral4Insert srg4i;
    public SongViewModelGeneral4Insert(@NonNull Application application) {
        super(application);
        SongDaoGeneral4Insert dao = CKSongDb.getInstance( application.getApplicationContext() ).songDaoGeneral4Insert();
        srg4i = new SongRepositoryGeneral4Insert(dao);
    }
    public void insert(SongBook songBook, Song song, List<Verse>verses){
       srg4i.insert(songBook,song,verses);
    }
}
