package com.churchkit.churchkit.database.entity.song;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SongFavoriteWrapper {
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

   public static List<SongFavoriteWrapper> fromMap(Map<SongFavorite, Song> songFavoriteSongMap){
      List<SongFavoriteWrapper> SongFavoriteWrapperList = new ArrayList<>();


      for (Map.Entry<SongFavorite, Song> entry : songFavoriteSongMap.entrySet()) {
         SongFavoriteWrapper songFavoriteWrapper = new SongFavoriteWrapper();

         songFavoriteWrapper.setDate(entry.getKey().getDate());
         songFavoriteWrapper.setSong(entry.getValue());
         songFavoriteWrapper.songBookName = entry.getKey().bookName;


         SongFavoriteWrapperList.add(songFavoriteWrapper);
      }

    return SongFavoriteWrapperList;
   }

}
