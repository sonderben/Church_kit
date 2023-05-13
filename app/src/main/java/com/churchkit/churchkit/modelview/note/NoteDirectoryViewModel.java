package com.churchkit.churchkit.modelview.note;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.churchkit.churchkit.database.dao.note.NoteDirectoryDao;
import com.churchkit.churchkit.database.entity.note.DirectoryWithNote;
import com.churchkit.churchkit.database.entity.note.NoteDirectoryEntity;
import com.churchkit.churchkit.database.entity.note.NoteEntity;
import com.churchkit.churchkit.repository.note.NoteDirectoryRepository;

import java.util.List;
import java.util.Map;

public class NoteDirectoryViewModel extends ViewModel {
    NoteDirectoryRepository repository;
    public NoteDirectoryViewModel(NoteDirectoryDao dao) {
        repository = new NoteDirectoryRepository(dao);
    }

    public void insert(NoteDirectoryEntity noteDirectory){
        repository.insert(noteDirectory);
    }
    public void delete(NoteDirectoryEntity noteDirectory){
        repository.delete(noteDirectory);
    }

    public LiveData<List<NoteDirectoryEntity>> getAllNoteDirectory() {
        return repository.getAllNoteDirectory();
    }


    public LiveData<  List<DirectoryWithNote>  > getAllDirectoryWithNote(){
        return repository.getAllDirectoryWithNote();
    }

    public void incrementAmountDefaultDirectory(int id){
        repository.incrementAmountDefaultDirectory(id);
    }
    public void decreaseAmountDefaultDirectory(int id){
        repository.decreaseAmountDefaultDirectory(id);
    }
}
