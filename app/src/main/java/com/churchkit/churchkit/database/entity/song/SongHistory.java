package com.churchkit.churchkit.database.entity.song;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "song_history",indices = {@Index(value = {"song_id"},unique = true)},
        foreignKeys = @ForeignKey(entity = Song.class,
                parentColumns = "song_id",
                childColumns = "song_id"))
public class SongHistory {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "song_history_id")
    long songHistoryId;
    @ColumnInfo(name = "song_id")
    String songId;
    long date;
    String bookName;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return "SongFavorite{" +
                "songHistoryId='" + songHistoryId + '\'' +
                ", songId='" + songId + '\'' +
                ", date=" + date +
                '}';
    }

    public SongHistory(String songId, long date, String bookName) {
        this.songId = songId;
        this.date = date;
        this.bookName = bookName;
    }

    @NonNull
    public long getSongHistoryId() {
        return songHistoryId;
    }

    public void setSongHistoryId(@NonNull long songHistoryId) {
        this.songHistoryId = songHistoryId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
