package com.churchkit.churchkit.database.dao.note;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.note.DirectoryWithNote;
import com.churchkit.churchkit.database.entity.note.NoteDirectoryEntity;
import com.churchkit.churchkit.database.entity.note.NoteEntity;

import java.util.List;
import java.util.Map;
@Dao
public interface NoteDirectoryDao extends BaseDao<NoteDirectoryEntity> {
    @Query("Select * from note_directory")
    LiveData<List<NoteDirectoryEntity>> getAllNoteDirectory();



    @Query("UPDATE note_directory SET note_amount = note_amount + 1 WHERE id = :id")
    void incrementAmountDefaultDirectory(int id);

    @Query("UPDATE note_directory SET note_amount = note_amount - 1 WHERE id = :id")
    void decreaseAmountDefaultDirectory(int id);








    @Transaction
    @Query("Select * from note_directory")
    LiveData<  List<DirectoryWithNote>  > getAllDirectoryWithNote();
}
