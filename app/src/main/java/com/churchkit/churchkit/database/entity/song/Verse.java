package com.churchkit.churchkit.database.entity.song;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
;

@Entity(tableName = "verse"/*,indices = {@Index(value = {"verse","song_id"},unique = true)}*/,
        foreignKeys = @ForeignKey(entity = Song.class,
                parentColumns = "song_id",
                childColumns = "song_id"))
public class Verse {
    @PrimaryKey()
    @NonNull
    //@ColumnInfo(name = "verse_id")
    private String verseId;
    private String verse;
    private int position;
    @ColumnInfo(name = "song_id")
    private String SongId;

    public Verse(){}
    public Verse(String verseId, String verse, int position, String songId) {
        this.verseId = verseId;
        this.verse = verse;
        this.position = position;
        SongId = songId;
    }

    @Override
    public String toString() {
        return "Verse{" +
                "verseId=" + verseId +
                ", verse='" + verse + '\'' +
                ", num=" + position +
                ", SongId=" + SongId +
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int num) {
        this.position = num;
    }

    public String getSongId() {
        return SongId;
    }

    public void setSongId(String songId) {
        SongId = songId;
    }
}
