package com.churchkit.churchkit.database.dao.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.churchkit.churchkit.database.dao.BaseDao;
import com.churchkit.churchkit.database.entity.song.BookMarkSong;

import java.util.List;

@Dao
public interface BookMarkSongDao extends BaseDao<BookMarkSong> {


    @Query("Select * from book_mark_song /*join song on book_mark_song.song_id = song.song_id*/ where song_id = :chapterId")
    LiveData<List<BookMarkSong>> getAllBookMark(String chapterId);


}
