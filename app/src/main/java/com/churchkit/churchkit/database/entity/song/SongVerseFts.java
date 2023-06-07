package com.churchkit.churchkit.database.entity.song;

import androidx.room.Entity;
import androidx.room.Fts4;

@Fts4(contentEntity = Verse.class)
@Entity(tableName = "song_verse_fts")
public class SongVerseFts {

    private String verseId;
    private String verse;
    private String reference;
    private String songTitle;

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public SongVerseFts(){}



    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "SongVerseFts{" +
                ", verse='" + verse + '\'' +
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

}
