package com.churchkit.churchkit.modelview.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.churchkit.churchkit.database.dao.note.NoteDao;
import com.churchkit.churchkit.database.dao.note.NoteDirectoryDao;
import com.churchkit.churchkit.database.entity.note.NoteDirectoryEntity;
import com.churchkit.churchkit.database.entity.note.NoteEntity;
import com.churchkit.churchkit.repository.note.NoteDirectoryRepository;
import com.churchkit.churchkit.repository.note.NoteRepository;

import java.util.List;
import java.util.Map;

public class NoteViewModel extends ViewModel {
    NoteRepository repository;
    public NoteViewModel(NoteDao dao) {
        repository = new NoteRepository(dao);
    }

    public void insert(NoteEntity note){
        repository.insert(note);
    }
    public void delete(NoteEntity note){
        repository.delete(note);
    }

    public LiveData<List<NoteEntity>> getAllNoteDirectory(int noteDirectoryId) {
        return repository.getAllNoteByNoteDirectory(noteDirectoryId);
    }



}
