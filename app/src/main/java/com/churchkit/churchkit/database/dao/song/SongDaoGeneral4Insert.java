package com.churchkit.churchkit.database.dao.song;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.Verse;

import java.util.List;

@Dao
public interface SongDaoGeneral4Insert {
    @Insert
    long insertSongBook(SongBook entity);
    @Insert
    long insertSong(Song songs);
    @Insert
    List<Long> insertSongVerses(List<Verse> verses);
}
