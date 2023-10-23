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

    /*UPDATE table
    SET column_1 = new_value_1,
            column_2 = new_value_2
    WHERE
            search_condition*/

    @Query("Update bible_info set isDownloaded =:isDownloaded where id =:id")
    int updateIsDownloaded(boolean isDownloaded,String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<BibleInfo>bibleInfoList);

   /* @Update
    int update(BibleInfo bibleInfo);*/


}
