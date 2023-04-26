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

    public BibleInfo(String id, String language, String name, int testament, String url) {
        super(id, language, name, testament, url);
    }

    //"eng","English","English Standard Version® - Hear the Word Audio Bible","EN1ESV",58200000,"United Kingdom","2019-09-10",NULL,"2019-09-11"

    @Ignore
    public static List<BibleInfo>getAllBibleInfo(){
        if (infoList.size() == 0){
            infoList.add(new BibleInfo("FRNTLS","Français","1910 Louis Segond  (Tresorsonore recording)",2,"1"));
            //bibleInfoList.add(new BibleInfo("HATHCB","Kreyòl Ayisyen","Alliance Biblique Universelle","1"));
            infoList.add(new BibleInfo("HATBIV","Kreyòl Ayisyen","Alliance Biblique Universelle",1,"1"));
            infoList.add(new BibleInfo("ENGESV","English","English Standard Version® - Hear the Word Audio Bible",2,""));
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
