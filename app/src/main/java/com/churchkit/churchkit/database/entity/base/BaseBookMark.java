package com.churchkit.churchkit.database.entity.base;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public abstract class BaseBookMark {
    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public String description;
    public String color;
    @NonNull
    public int start;
    @NonNull
    public int end;

    public BaseBookMark( String title, String description, String color, int start, int end) {
        this.id = start+title+end;
        this.title = title;
        this.description = description;
        this.color = color;
        this.start = start;
        this.end = end;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
