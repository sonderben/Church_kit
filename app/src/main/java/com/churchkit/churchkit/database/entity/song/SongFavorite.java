package com.churchkit.churchkit.database.entity.song;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.churchkit.churchkit.database.entity.base.FavHis;

@Entity(tableName = "song_favorite",indices = {@Index(value = {"parent_id"},unique = true)},
        foreignKeys = @ForeignKey(entity = Song.class,
                parentColumns = "id",
                childColumns = "parent_id",onDelete = ForeignKey.CASCADE))
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

    public SongFavorite(String songId,String infoId, long date, String bookName) {
        super(songId,infoId,date,bookName);
    }
    public SongFavorite(){}


}
