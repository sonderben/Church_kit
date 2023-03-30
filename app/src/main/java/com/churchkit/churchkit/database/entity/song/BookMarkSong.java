package com.churchkit.churchkit.database.entity.song;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.churchkit.churchkit.database.entity.song.Song;

import java.util.Objects;

@Entity(tableName = "book_mark_song",
        foreignKeys = @ForeignKey(entity = Song.class,
                parentColumns = "song_id",
                childColumns = "song_id"))
//@Entity(primaryKeys = {"firstName", "lastName"})
public class BookMarkSong {
    @PrimaryKey//(autoGenerate = true)
    @NonNull
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    private String title;
    @NonNull
    private String description;
    private String color;
    private int start;
    private int end;
    @ColumnInfo(name = "song_id")
    private String songId;

    public BookMarkSong(String title, String description, String color, int start, int end, String songId) {
        this.title = title;
        this.description = description;
        this.color = color;
        this.start = start;
        this.end = end;
        this.songId = songId;
        this.id = start+""+end+songId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookMarkSong that = (BookMarkSong) o;
        return start == that.start && end == that.end && Objects.equals(songId, that.songId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, songId);
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

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
