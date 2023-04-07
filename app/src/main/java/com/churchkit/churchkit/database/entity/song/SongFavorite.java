package com.churchkit.churchkit.database.entity.song;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.churchkit.churchkit.database.entity.base.FavHis;

@Entity(tableName = "song_favorite",indices = {@Index(value = {"parent_id"},unique = true)},
        foreignKeys = @ForeignKey(entity = Song.class,
                parentColumns = "id",
                childColumns = "parent_id"))
public class SongFavorite extends FavHis {


    @Override
    public String toString() {
        return "SongFavorite{" +
                "id=" + id +
                ", parentId='" + parentId + '\'' +
                ", date=" + date +
                ", abbreviation='" + abbreviation + '\'' +
                '}';
    }

    public SongFavorite(String songId, long date, String bookName) {
        super(songId,date,bookName);
    }
    public SongFavorite(){}


}
