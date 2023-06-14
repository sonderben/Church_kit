package com.churchkit.churchkit.database.entity.song;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.churchkit.churchkit.database.entity.base.BaseInfo;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "song_info")
public class SongInfo extends BaseInfo {

    private int amountSong;

    public int getAmountSong() {
        return amountSong;
    }

    public void setAmountSong(int amountSong) {
        this.amountSong = amountSong;
    }

    @Ignore
    private static List< SongInfo> infoList = new ArrayList<>(10);
    public SongInfo(String id, String language, String name,String abbreviation, int testament, String url,int amountSong,String size) {
        super(id, language, name,abbreviation, testament, url,size);
        this.amountSong = amountSong;
    }

    public static List<SongInfo> getAllSongInfo(){
        if (infoList.size() == 0){
            infoList.add(new SongInfo("songbook","Français, Kreyòl","Chant d'esperance","HTICDE",14,"song/songbook-v1.json",0,"1.43 MB"));
                infoList.add(new SongInfo("himnario pentecostal","Español","Himanario evangelico pentecostal","ESPHEP",1,"song/himnario pentecostal-v1.json",444,"580.13 KB"));

        }
        return  infoList;
    }
    public static String getBibleInfoNameById(String id){
        for (int i = 0; i < getAllSongInfo().size(); i++) {
            if (infoList.get(i).getId().equals(id))
                return infoList.get(i).getName();
        }
        return null;
    }

    public boolean isSinglePart(){
        return testament<2;
    }






}
