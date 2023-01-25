package com.churchkit.churchkit.Model.song;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.churchkit.churchkit.database.entity.Song;
import com.churchkit.churchkit.database.entity.Verse;

import java.util.List;

public class SongWithVerse {
    @Embedded private Song song;
    @Relation(
            parentColumn = "song_id",
            entityColumn = "song_id",
            entity = Verse.class
    )
    private List<Verse>verseList;

    @Override
    public String toString() {
        return "SongWithVerse{" +
                "song=" + song +
                ", verseList=" + verseList +
                '}';
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public List<Verse> getVerseList() {
        return verseList;
    }

    public void setVerseList(List<Verse> verseList) {
        this.verseList = verseList;
    }
}
