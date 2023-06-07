package com.churchkit.churchkit.database.entity.base;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public abstract class FavHis {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected long id;
    @ColumnInfo(name = "parent_id")
    protected  String parentId;
    protected long date;
    protected String abbreviation;
    protected String infoId;

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getInfoId() {
        return infoId;
    }

    public FavHis(String parentId, String infoId, long date, String abbreviation) {
        this.parentId = parentId;
        this.infoId = infoId;
        this.date = date;
        this.abbreviation = abbreviation;
    }

    public FavHis(){}
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
