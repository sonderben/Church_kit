package com.churchkit.churchkit.database.entity.bible;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BibleChapterFavoriteWrapper {
   long date;
   BibleChapter bibleChapter;
   String abbreviation;

   public long getDate() {
      return date;
   }

   public BibleChapter getBibleChapter() {
      return bibleChapter;
   }

   public String getAbbreviation() {
      return abbreviation;
   }

   public static List<BibleChapterFavoriteWrapper> fromMap(Map<BibleChapterFavorite, BibleChapter> songFavoriteSongMap){
      List<BibleChapterFavoriteWrapper> bibleChapterFavoriteWrappers = new ArrayList<>();


      for (Map.Entry<BibleChapterFavorite, BibleChapter> entry : songFavoriteSongMap.entrySet()) {
         BibleChapterFavoriteWrapper bibleChapterFavoriteWrapper = new BibleChapterFavoriteWrapper();

         bibleChapterFavoriteWrapper.date= entry.getKey().getDate() ;


         bibleChapterFavoriteWrapper.bibleChapter = entry.getValue();
         bibleChapterFavoriteWrapper.abbreviation = entry.getKey().getAbbreviation();


         bibleChapterFavoriteWrappers.add(bibleChapterFavoriteWrapper);
      }

    return bibleChapterFavoriteWrappers;
   }

}
