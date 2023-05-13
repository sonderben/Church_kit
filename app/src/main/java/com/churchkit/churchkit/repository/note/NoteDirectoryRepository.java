package com.churchkit.churchkit.repository.note;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.note.NoteDirectoryDao;
import com.churchkit.churchkit.database.entity.note.DirectoryWithNote;
import com.churchkit.churchkit.database.entity.note.NoteDirectoryEntity;
import com.churchkit.churchkit.database.entity.note.NoteEntity;
import com.churchkit.churchkit.repository.song.BaseRepository;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NoteDirectoryRepository extends BaseRepository<NoteDirectoryDao, NoteDirectoryEntity> {
    public NoteDirectoryRepository(NoteDirectoryDao songHistoryDao) {
        super(songHistoryDao);
    }

    public LiveData<List<NoteDirectoryEntity>> getAllNoteDirectory() {
       return dao.getAllNoteDirectory();
    }


    public LiveData<  List<DirectoryWithNote>  > getAllDirectoryWithNote(){

        return dao.getAllDirectoryWithNote();
    }

    public void incrementAmountDefaultDirectory(int id){
        Completable.fromAction( ()-> dao.incrementAmountDefaultDirectory(id) )
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }
    public void decreaseAmountDefaultDirectory(int id){

        Completable.fromAction( ()-> dao.decreaseAmountDefaultDirectory(id) )
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
