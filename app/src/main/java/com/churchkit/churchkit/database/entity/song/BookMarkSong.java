package com.churchkit.churchkit.database.entity.song;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.churchkit.churchkit.database.entity.base.BaseBookMark;

import java.util.Objects;

@Entity(tableName = "book_mark_song",
        foreignKeys = @ForeignKey(entity = Song.class,
                parentColumns = "id",
                childColumns = "song_id",onDelete = ForeignKey.CASCADE),indices = {@Index(value = {"song_id"})})

public class BookMarkSong extends BaseBookMark {



    @ColumnInfo(name = "song_id")
    private String songId;


    public BookMarkSong( String title, String description, String color, int start, int end,String songId) {
        super( title, description, color, start, end);
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





    public String getSongId() {
        return songId;
    }


}
