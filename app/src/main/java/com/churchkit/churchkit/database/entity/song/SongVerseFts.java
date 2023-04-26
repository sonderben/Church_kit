package com.churchkit.churchkit.database.entity.song;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;

@Fts4(contentEntity = Verse.class)
@Entity(tableName = "song_verse_fts")
public class SongVerseFts {

    private String verseId;
    private String verse;
    @ColumnInfo(name = "song_id")
    private String SongId;
    private String reference;

    public SongVerseFts(){}

    public SongVerseFts(String verseId, String verse, String songId, String reference) {
        this.verseId = verseId;
        this.verse = verse;
        SongId = songId;
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "SongVerseFts{" +
                "verseId='" + verseId + '\'' +
                ", verse='" + verse + '\'' +
                ", SongId='" + SongId + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }

    public String getVerseId() {
        return verseId;
    }

    public void setVerseId(String verseId) {
        this.verseId = verseId;
    }

    public String getVerse() {
        return verse;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }

    public String getSongId() {
        return SongId;
    }

    public void setSongId(String songId) {
        SongId = songId;
    }
}
