package com.churchkit.churchkit.database.entity.note;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import java.util.Objects;

public class DirectoryWithNote {
    @Embedded
    public NoteDirectoryEntity noteDirectory;

    @Relation(
            parentColumn = "id",
            entityColumn = "note_directory_id"
    )
    public List<NoteEntity>noteEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectoryWithNote that = (DirectoryWithNote) o;
        return noteDirectory.equals(that.noteDirectory) && Objects.equals(noteEntities, that.noteEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteDirectory, noteEntities);
    }
}
