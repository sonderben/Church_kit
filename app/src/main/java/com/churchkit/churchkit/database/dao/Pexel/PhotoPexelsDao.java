package com.churchkit.churchkit.database.dao.Pexel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.PexelsPhoto;

import java.util.List;

@Dao
public interface PhotoPexelsDao extends BaseDao<PexelsPhoto> {
    @Query("Select * from pexels_photo")
    LiveData<List<PexelsPhoto>> getAllPhotoPexels();
}
