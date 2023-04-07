package com.churchkit.churchkit.database.dao.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.song.BookMarkSong;

import java.util.List;

@Dao
public interface BookMarkSongDao extends BaseDao<BookMarkSong> {
    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
     long insertBookMark(BookMarkSong bookMark);*/

    @Query("Select * from book_mark_song /*join song on book_mark_song.song_id = song.song_id*/ where song_id = :chapterId")
    LiveData<List<BookMarkSong>> getAllBookMark(String chapterId);

    /*@Delete
     void deleteBookMark(BookMarkSong bookMark);*/
}
