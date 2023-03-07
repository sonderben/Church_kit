package com.churchkit.churchkit.database.entity.song;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SongHistoryWrapper {
   long date;
   Song song;
   String songBookName;

   public String getSongBookName() {
      return songBookName;
   }

   public long getDate() {
      return date;
   }

   public void setDate(long date) {
      this.date = date;
   }

   public Song getSong() {
      return song;
   }

   public void setSong(Song song) {
      this.song = song;
   }

   public static List<SongHistoryWrapper> fromMap(Map<SongHistory, Song> songFavoriteSongMap){
      List<SongHistoryWrapper> SongFavoriteWrapperList = new ArrayList<>();


      for (Map.Entry<SongHistory, Song> entry : songFavoriteSongMap.entrySet()) {
         SongHistoryWrapper songHistoryWrapper = new SongHistoryWrapper();

         songHistoryWrapper.setDate(entry.getKey().getDate());
         songHistoryWrapper.setSong(entry.getValue());
         songHistoryWrapper.songBookName = entry.getKey().bookName;


         SongFavoriteWrapperList.add(songHistoryWrapper);
      }

    return SongFavoriteWrapperList;
   }

}
