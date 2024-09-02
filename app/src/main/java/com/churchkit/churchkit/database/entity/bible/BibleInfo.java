package com.churchkit.churchkit.database.entity.bible;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.churchkit.churchkit.database.entity.base.BaseInfo;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "bible_info")
public class BibleInfo extends BaseInfo {
    @Ignore
    private static List< BibleInfo> infoList = new ArrayList<>(10);
    public int amountOldTestament;
    public int amountNewTestament;

    public BibleInfo(
            String id, String language, String name, String abbreviation, int testament, String url,
            String size,int amountOldTestament,int amountNewTestament) {
        super(id, language, name, abbreviation,testament, url,size);
        this.amountNewTestament = amountNewTestament;
        this.amountOldTestament = amountOldTestament;
    }



    @Ignore
    public static List<BibleInfo>getAllBibleInfo(){


        if (infoList.size() == 0){
            infoList.add(new BibleInfo("FRNTLS","Français","1910 Louis Segond  (Tresorsonore recording)","FRNTLS",2,"bible/FRNTLS-v1.json","6 MB",39,27));
            //bibleInfoList.add(new BibleInfo("HATHCB","Kreyòl Ayisyen","Alliance Biblique Universelle","1"));
            infoList.add(new BibleInfo("HATBIV","Kreyòl Ayisyen","Alliance Biblique Universelle","HATBIV",1,"bible/HATBIV-v1.json","1.14MB",0,27));
            infoList.add(new BibleInfo("ENGESV","English","English Standard Version® - Hear the Word Audio Bible","ENGESV",2,"bible/ENGESV-v1.json","5 MB",39,27));
        }
        return infoList;
    }
    public static String getBibleInfoNameById(String id){
        for (int i = 0; i < getAllBibleInfo().size(); i++) {
            if (infoList.get(i).getId().equals(id))
                return infoList.get(i).getName();
        }
        return null;
    }



}
