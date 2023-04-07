package com.churchkit.churchkit.database.entity.bible;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.churchkit.churchkit.database.entity.base.FavHis;
import com.churchkit.churchkit.database.entity.song.Song;

@Entity(tableName = "chapter_favorite",indices = {@Index(value = {"parent_id"},unique = true)},
        foreignKeys = @ForeignKey(entity = BibleChapter.class,
                parentColumns = "id",
                childColumns = "parent_id"))
public class BibleChapterFavorite extends FavHis {


    public BibleChapterFavorite(String bibleChapterId, long date, String abbreviation) {
        super(bibleChapterId,date,abbreviation);
    }
public BibleChapterFavorite(){}
    @Override
    public String toString() {
        return "BibleChapterFavorite{" +
                "id=" + id +
                ", parentId='" + parentId + '\'' +
                ", date=" + date +
                ", abbreviation='" + abbreviation + '\'' +
                '}';
    }
}
