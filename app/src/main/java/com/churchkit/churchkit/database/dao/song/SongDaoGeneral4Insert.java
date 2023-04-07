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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSongBook(SongBook entity);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertSong(List<Song> songs);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertSongVerses(List<Verse> verses);
}
