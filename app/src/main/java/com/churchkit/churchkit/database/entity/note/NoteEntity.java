package com.churchkit.churchkit.database.entity.note;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.churchkit.churchkit.database.entity.song.SongBook;

import java.io.Serializable;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;

@Entity(tableName = "note",indices = {@Index(value = {"note_directory_id"})},
        foreignKeys = @ForeignKey(entity = NoteDirectoryEntity.class,
                parentColumns = "id",
                childColumns = "note_directory_id"))
public class NoteEntity extends BaseNoteEntity implements Serializable {
    String noteText="";

    String hashtag;
    @ColumnInfo(name = "note_directory_id")
    @NonNull
    int noteDirectoryEntityId=1;//default

    public int getNoteDirectoryEntityId() {
        return noteDirectoryEntityId;
    }



    public void setNoteDirectoryEntityId(int noteDirectoryEntityId) {
        this.noteDirectoryEntityId = noteDirectoryEntityId;
    }





    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }


    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public NoteEntity(@NonNull String title) {
        super(title);
    }
}
