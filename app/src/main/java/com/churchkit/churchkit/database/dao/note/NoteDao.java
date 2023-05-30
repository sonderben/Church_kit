package com.churchkit.churchkit.database.dao.note;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.note.NoteEntity;

import java.util.List;
@Dao
public interface NoteDao extends BaseDao<NoteEntity> {
    @Query("Select * from note where note_directory_id =:id")
    LiveData<List<NoteEntity>> getAllNoteByNoteDirectory(int id);

    @Query("Select * from note  join note_fts on note.id = note_fts.rowid where note_fts match :textSearch")
    LiveData<List<NoteEntity>> search(String textSearch);

}
