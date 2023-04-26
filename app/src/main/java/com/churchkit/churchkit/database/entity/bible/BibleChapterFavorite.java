package com.churchkit.churchkit.database.entity.bible;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.churchkit.churchkit.database.entity.base.FavHis;

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
