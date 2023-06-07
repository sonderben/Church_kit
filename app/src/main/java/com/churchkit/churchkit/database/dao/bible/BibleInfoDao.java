package com.churchkit.churchkit.database.dao.bible;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.bible.BibleInfo;

import java.util.List;

@Dao
public interface BibleInfoDao extends BaseDao<BibleInfo> {
    @Query("Select * from bible_info")
    LiveData<List<BibleInfo>>getAllBibleInfo();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<BibleInfo>bibleInfoList);

   /* @Update
    int update(BibleInfo bibleInfo);*/


}
