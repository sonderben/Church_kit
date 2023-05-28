package com.churchkit.churchkit.database.entity.note;

import androidx.room.Entity;
import androidx.room.Fts4;

@Fts4(contentEntity = NoteEntity.class)
@Entity(tableName = "note_fts")
public class NoteFtsEntity {
    private String title;
    private String noteText="";
    private String hashtag;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public NoteFtsEntity(String title, String noteText, String hashtag) {
        this.title = title;
        this.noteText = noteText;
        this.hashtag = hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }
}
