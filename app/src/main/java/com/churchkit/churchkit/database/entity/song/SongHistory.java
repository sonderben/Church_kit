package com.churchkit.churchkit.database.entity.song;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.churchkit.churchkit.database.entity.base.FavHis;

@Entity(tableName = "song_history",indices = {@Index(value = {"parent_id"},unique = true)},
        foreignKeys = @ForeignKey(entity = Song.class,
                parentColumns = "id",
                childColumns = "parent_id",onDelete = ForeignKey.CASCADE))
public class SongHistory extends FavHis {



    @Override
    public String toString() {
        return "SongHistory{" +
                "id=" + id +
                ", parentId='" + parentId + '\'' +
                ", date=" + date +
                ", abbreviation='" + abbreviation + '\'' +
                '}';
    }

    public SongHistory(String songId,String infoId, long date, String bookName) {
        super(songId,infoId,date,bookName);
    }

    public SongHistory(){}

}
