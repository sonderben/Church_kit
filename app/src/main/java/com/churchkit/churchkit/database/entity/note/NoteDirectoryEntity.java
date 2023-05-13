package com.churchkit.churchkit.database.entity.note;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.io.Serializable;
import java.util.Objects;



@Entity(tableName = "note_directory")
public class NoteDirectoryEntity extends BaseNoteEntity  {

    @ColumnInfo(name = "note_amount")
    int noteAmount;
    String color;
    String password;

    /*@Ignore
    public NoteDirectoryEntity(@NonNull String title, int noteAmount, String color, String password) {
        super(title);
        this.noteAmount = noteAmount;
        this.color = color;
        this.password = password;
    }*/

    public NoteDirectoryEntity(@NonNull String title) {
        super(title);
        this.color = "#00f";

    }

    public boolean isLock(){
        return password != null && !password.isEmpty();
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





    public int getNoteAmount() {
        return noteAmount;
    }

    public void setNoteAmount(int noteAmount) {
        this.noteAmount = noteAmount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
