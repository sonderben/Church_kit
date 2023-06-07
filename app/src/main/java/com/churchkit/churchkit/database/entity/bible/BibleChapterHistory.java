package com.churchkit.churchkit.database.entity.bible;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.churchkit.churchkit.database.entity.base.FavHis;

@Entity(tableName = "chapter_history",indices = {@Index(value = {"parent_id"},unique = true)},
        foreignKeys = @ForeignKey(entity = BibleChapter.class,
                parentColumns = "id",
                childColumns = "parent_id",onDelete = ForeignKey.CASCADE))
public class BibleChapterHistory extends FavHis {
    public BibleChapterHistory(String parentId,String infoId, long date, String abbreviation) {
        super(parentId,infoId, date, abbreviation);
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
