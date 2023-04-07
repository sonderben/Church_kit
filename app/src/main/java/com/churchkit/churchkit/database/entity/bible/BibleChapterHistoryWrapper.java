package com.churchkit.churchkit.database.entity.bible;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BibleChapterHistoryWrapper {
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

   public static List<BibleChapterHistoryWrapper> fromMap(Map<BibleChapterHistory, BibleChapter> songFavoriteSongMap){
      List<BibleChapterHistoryWrapper> bibleChapterFavoriteWrappers = new ArrayList<>();


      for (Map.Entry<BibleChapterHistory, BibleChapter> entry : songFavoriteSongMap.entrySet()) {
         BibleChapterHistoryWrapper bibleChapterHistoryWrapper = new BibleChapterHistoryWrapper();

         bibleChapterHistoryWrapper.date= entry.getKey().getDate() ;


         bibleChapterHistoryWrapper.bibleChapter = entry.getValue();
         bibleChapterHistoryWrapper.abbreviation = entry.getKey().getAbbreviation();


         bibleChapterFavoriteWrappers.add(bibleChapterHistoryWrapper);
      }

    return bibleChapterFavoriteWrappers;
   }

}
