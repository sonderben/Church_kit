package com.churchkit.churchkit.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
;

@Entity(tableName = "verse",indices = {@Index(value = {"verse"},unique = true)},
        foreignKeys = @ForeignKey(entity = Song.class,
                parentColumns = "song_id",
                childColumns = "song_id"))
public class Verse {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "verse_id")
    private long verseId;
    private String verse;
    private short num;
    @ColumnInfo(name = "song_id")
    private long SongId;

    public Verse(){}
    public Verse(/*long verseId,*/ String verse, short num, long songId) {
        //this.verseId = verseId;
        this.verse = verse;
        this.num = num;
        SongId = songId;
    }

    @Override
    public String toString() {
        return "Verse{" +
                "verseId=" + verseId +
                ", verse='" + verse + '\'' +
                ", num=" + num +
                ", SongId=" + SongId +
                '}';
    }

    public long getVerseId() {
        return verseId;
    }

    public void setVerseId(long verseId) {
        this.verseId = verseId;
    }

    public String getVerse() {
        return verse;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }

    public short getNum() {
        return num;
    }

    public void setNum(short num) {
        this.num = num;
    }

    public long getSongId() {
        return SongId;
    }

    public void setSongId(long songId) {
        SongId = songId;
    }
}
