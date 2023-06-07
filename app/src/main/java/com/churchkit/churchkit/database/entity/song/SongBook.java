package com.churchkit.churchkit.database.entity.song;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.churchkit.churchkit.database.entity.base.Book;


@Entity(tableName = "song_book"/*,
        foreignKeys = @ForeignKey(entity = SongInfo.class,
                parentColumns = "id",
                childColumns = "song_info_id")*/,indices = {@Index(value = {"song_info_id"})})
public class SongBook extends Book {

    @ColumnInfo(name = "song_info_id")
    private String songInfoId;

    public SongBook(String songInfoId,@NonNull String id, String abbreviation, String title, int position, int childAmount,String color,String image) {
        super(id, abbreviation, title, position, childAmount,color,image);
        this.songInfoId = songInfoId;
    }


    public String getSongInfoId() {
        return songInfoId;
    }

    public void setSongInfoId(String songInfoId) {
        this.songInfoId = songInfoId;
    }

    @Override
    public String toString() {
        return "SongBook{" +
                "songInfoId='" + songInfoId + '\'' +
                ", id='" + id + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", title='" + title + '\'' +
                ", position=" + position +
                ", childAmount=" + childAmount +
                ", color='" + color + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
