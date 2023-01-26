package com.churchkit.churchkit;

import android.content.Context;
import android.content.res.AssetManager;

import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.Song;
import com.churchkit.churchkit.database.entity.SongBook;
import com.churchkit.churchkit.database.entity.Verse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Util {
    private static JSONObject getJsonObjectFromAsset(Context context) throws IOException, JSONException {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("data.json");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        String jsonString = stringBuilder.toString();
        JSONObject jsonObject = new JSONObject(jsonString);

        return jsonObject;
    }

    public static void prepopulateDatabaseFromJsonFile (Context context) throws JSONException, IOException {
        ChurchKitDb database = ChurchKitDb.getInstance(context);
        JSONObject jsonObject = getJsonObjectFromAsset(context);

       JSONArray book_songArray= jsonObject.getJSONArray("book_song");
        SongBook songBook;
        Song song;
        Verse verseEntity;
       for (int i = 0;i<book_songArray.length();i++){
          JSONObject bookSong = book_songArray.getJSONObject(i);
          //int songBookId = bookSong.getInt("songBookId");
          String name = bookSong.getString("name");
           String abbreviation = bookSong.getString("abbreviation");
          int color = bookSong.getInt("color");
          int songAmount = bookSong.getInt("songAmount");
          int image = bookSong.getInt("image");
          int num = bookSong.getInt("num");
           JSONArray songsArray = bookSong.getJSONArray("songs");

           songBook = new SongBook(name,abbreviation, (short) color,songAmount, (short) image, (short) num);
           long songBookId = database.songBookDao().insert(songBook);

           for (int j = 0; j<songsArray.length();j++){
               JSONObject songObj = songsArray.getJSONObject(j);
              //int songId = songObj.getInt("songId");
               String title = songObj.getString("title");
               int numSong = songObj.getInt("num");
               int page = songObj.getInt("page");

               song = new Song(title,numSong,page,songBookId);
               long songId = database.songDao().insert(song);

               JSONArray versesArray = songObj.getJSONArray("verses");
               for (int k = 0;k<versesArray.length();k++){
                   JSONObject verseObj = versesArray.getJSONObject(k);
                   long verseId = verseObj.getLong("verseId");
                   String verse = verseObj.getString("verse");
                   int numVerse = verseObj.getInt("num");

                   verseEntity = new Verse(/* verseId,*/verse, (short) numVerse,songId);
                   database.verseDao().insert(verseEntity);
               }
           }
       }
    }

    public static String formatNumberToString(int number){
        if(number >99)
            return number+" ";
        if (number>9)
            return "0"+number+" ";

        return "00"+number+" ";

    }
}
