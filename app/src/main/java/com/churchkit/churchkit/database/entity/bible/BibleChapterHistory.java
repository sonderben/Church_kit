package com.churchkit.churchkit.database.entity.bible;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.churchkit.churchkit.database.entity.base.FavHis;

@Entity(tableName = "chapter_history",indices = {@Index(value = {"parent_id"},unique = true)},
        foreignKeys = @ForeignKey(entity = BibleChapter.class,
                parentColumns = "id",
                childColumns = "parent_id"))
public class BibleChapterHistory extends FavHis {
    public BibleChapterHistory(String parentId, long date, String abbreviation) {
        super(parentId, date, abbreviation);
    }





    @Override
    public String toString() {
        return "BibleChapterHistory{" +
                "id=" + id +
                ", parentId='" + parentId + '\'' +
                ", date=" + date +
                ", abbreviation='" + abbreviation + '\'' +
                '}';
    }
}
