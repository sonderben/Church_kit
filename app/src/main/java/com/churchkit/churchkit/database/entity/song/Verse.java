package com.churchkit.churchkit.database.entity.song;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
;

@Entity(tableName = "verse",indices = {@Index(value = {"song_id"})},
        foreignKeys = @ForeignKey(entity = Song.class,
                parentColumns = "id",
                childColumns = "song_id",onDelete = ForeignKey.CASCADE))
public class Verse {
    @PrimaryKey()
    @NonNull
    //@ColumnInfo(name = "verse_id")
    private String verseId;
    private String verse;
    private int position;
    @ColumnInfo(name = "song_id")
    private String SongId;
    private String reference;
    private String infoId;
    private String songTitle;

    public Verse(){}

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public Verse(String infoId, String verseId, String verse, String reference, int position, String songId, String songTitle) {
        this.verseId = verseId;
        this.verse = verse;
        this.position = position;
        this.reference = reference;
        this.SongId = songId;
        this.infoId = infoId;
        this.songTitle = songTitle;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "Verse{" +
                "verseId='" + verseId + '\'' +
                ", verse='" + verse + '\'' +
                ", position=" + position +
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
