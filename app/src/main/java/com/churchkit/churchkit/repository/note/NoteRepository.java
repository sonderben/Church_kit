package com.churchkit.churchkit.repository.note;

import androidx.lifecycle.LiveData;

import com.churchkit.churchkit.database.dao.note.NoteDao;
import com.churchkit.churchkit.database.entity.note.NoteEntity;
import com.churchkit.churchkit.repository.song.BaseRepository;

import java.util.List;

public class NoteRepository extends BaseRepository<NoteDao, NoteEntity> {
    public NoteRepository(NoteDao songHistoryDao) {
        super(songHistoryDao);
    }

    public LiveData<List<NoteEntity>> getAllNoteByNoteDirectory(int id){
        return dao.getAllNoteByNoteDirectory(id);
    }
}
