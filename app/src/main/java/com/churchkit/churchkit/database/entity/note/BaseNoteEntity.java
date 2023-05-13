package com.churchkit.churchkit.database.entity.note;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;


@Entity
public abstract class  BaseNoteEntity implements Serializable {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseNoteEntity that = (BaseNoteEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @PrimaryKey(autoGenerate = true)
    int id;
    @NonNull
    String title;
    @NonNull
    Calendar date;

    public BaseNoteEntity(@NonNull String title){
        this.title = title;
        date = Calendar.getInstance();
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

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
