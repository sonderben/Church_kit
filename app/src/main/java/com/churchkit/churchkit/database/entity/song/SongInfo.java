package com.churchkit.churchkit.database.entity.song;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.churchkit.churchkit.database.entity.base.BaseInfo;
import com.churchkit.churchkit.database.entity.bible.BibleInfo;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "song_info")
public class SongInfo extends BaseInfo {

    @Ignore
    private static List< SongInfo> infoList = new ArrayList<>(10);
    public SongInfo(String id, String language, String name, int testament, String url) {
        super(id, language, name, testament, url);
    }


    public static List<SongInfo>getAllBibleInfo(){
        if (infoList.size() == 0){
            infoList.add(new SongInfo("songbook","Français, Kreyòl","Chant d'esperance",8,"1"));

        }
        return  infoList;
    }
    public static String getBibleInfoNameById(String id){
        for (int i = 0; i < getAllBibleInfo().size(); i++) {
            if (infoList.get(i).getId().equals(id))
                return infoList.get(i).getName();
        }
        return null;
    }






}
