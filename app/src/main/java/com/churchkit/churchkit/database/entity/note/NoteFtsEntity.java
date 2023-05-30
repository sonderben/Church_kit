package com.churchkit.churchkit.database.entity.note;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

@Fts4(contentEntity = NoteEntity.class)
@Entity(tableName = "note_fts")
public class NoteFtsEntity {
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    private int id;
    private String title;
    private String noteText="";

    public String getTitle() {
        return title;
    }

    public String getNoteText() {
        return noteText;
    }

    public String getHashtag() {
        return hashtag;
    }

    private String hashtag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
