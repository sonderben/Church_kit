package com.churchkit.churchkit.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(T entity);


    @Delete
    void delete(T entity);
}
