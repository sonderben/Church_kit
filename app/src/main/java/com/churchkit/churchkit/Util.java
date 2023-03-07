package com.churchkit.churchkit;

import android.content.Context;
import android.content.res.AssetManager;

import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.Verse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Util {
    public final static String FROM_SONG_FAVORITE= "FROM_SONG_FAVORITE";
    public final static String FROM_SONG_HISTORY = "FROM_SONG_HISTORY";
    public final static String FROM_SONG_HOPE = "FROM_SONG_HOPE";

    public final static String FROM_BIBLE_FAVORITE= "FROM_BIBLE_FAVORITE";
    public final static String FROM_BIBLE_HISTORY = "FROM_BIBLE_HISTORY";
    public final static String FROM_BIBLE = "FROM_BIBLE";

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

    public static String getJsonFromAssetForFireStore(Context context) throws IOException, JSONException {
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
        //JSONObject jsonObject = new JSONObject(jsonString);

        return jsonString;
    }

    public static long prepopulateBible(Context context,BibleBook bibleBook){
        ChurchKitDb database = ChurchKitDb.getInstance(context);
        return database.bibleBookDao().insertBibleBook(bibleBook);
    }
    public static long prepopulateSonBook(Context context,SongBook songBook){
        ChurchKitDb database = ChurchKitDb.getInstance(context);
        return database.songBookDao().insert(songBook);
    }
    public static long prepopulateSong(Context context,Song song){
        ChurchKitDb database = ChurchKitDb.getInstance(context);
        return database.songDao().insert(song);

    }
    public static long prepopulateBibleChapter(Context context,BibleChapter bibleChapter){
        ChurchKitDb database = ChurchKitDb.getInstance(context);
        return database.bibleChapterDao().insertChapter(bibleChapter);

    }
    public static List<Long> prepopulateBibleVerse(Context context, List<BibleVerse> bibleVerses){
        if (bibleVerses.size()>0){
            ChurchKitDb database = ChurchKitDb.getInstance(context);
           return database.bibleVerseDao().insertAll(bibleVerses);
        }
        return null;
    }
    public static List<Long> prepopulateSongVerse(Context context, List<Verse> verses){
        if (verses.size()>0){
            ChurchKitDb database = ChurchKitDb.getInstance(context);
            return database.verseDao().insertAll(verses);
        }
        return null;
    }


    /*public static void prepopulateDatabaseFromJsonFile (Context context,JSONObject jsonObject) throws JSONException, IOException {
        ChurchKitDb database = ChurchKitDb.getInstance(context);
       // JSONObject jsonObject = getJsonObjectFromAsset(context);
        addStuff(context);

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
          int num = bookSong.getInt("position");
           JSONArray songsArray = bookSong.getJSONArray("songs");

           songBook = new SongBook(name,abbreviation, (short) color,songAmount, (short) image, (short) num);
           long songBookId = database.songBookDao().insert(songBook);

           for (int j = 0; j<songsArray.length();j++){
               JSONObject songObj = songsArray.getJSONObject(j);
              //int songId = songObj.getInt("songId");
               String title = songObj.getString("title");
               int numSong = songObj.getInt("position");
               int page = songObj.getInt("page");

               song = new Song(title,numSong,page,songBookId);
               long songId = database.songDao().insert(song);

               JSONArray versesArray = songObj.getJSONArray("verses");
               for (int k = 0;k<versesArray.length();k++){
                   JSONObject verseObj = versesArray.getJSONObject(k);
                   long verseId = verseObj.getLong("verseId");
                   String verse = verseObj.getString("verse");
                   int numVerse = verseObj.getInt("position");

                   verseEntity = new Verse(*//* verseId,*//*verse, (short) numVerse,songId);
                   database.verseDao().insert(verseEntity);
               }
           }
       }
    }
    public static void prepopulateDatabaseFromJsonFile (Context context) throws JSONException, IOException {
        ChurchKitDb database = ChurchKitDb.getInstance(context);
        JSONObject jsonObject = getJsonObjectFromAsset(context);
        addStuff(context);

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
            int num = bookSong.getInt("position");
            JSONArray songsArray = bookSong.getJSONArray("songs");

            songBook = new SongBook(name,abbreviation, (short) color,songAmount, (short) image, (short) num);
            long songBookId = database.songBookDao().insert(songBook);

            for (int j = 0; j<songsArray.length();j++){
                JSONObject songObj = songsArray.getJSONObject(j);
                //int songId = songObj.getInt("songId");
                String title = songObj.getString("title");
                int numSong = songObj.getInt("position");
                int page = songObj.getInt("page");

                song = new Song(title,numSong,page,songBookId);
                long songId = database.songDao().insert(song);

                JSONArray versesArray = songObj.getJSONArray("verses");
                for (int k = 0;k<versesArray.length();k++){
                    JSONObject verseObj = versesArray.getJSONObject(k);
                    long verseId = verseObj.getLong("verseId");
                    String verse = verseObj.getString("verse");
                    int numVerse = verseObj.getInt("position");

                    verseEntity = new Verse(*//* verseId,*//*verse, (short) numVerse,songId);
                    database.verseDao().insert(verseEntity);
                }
            }
        }
    }

    public static void addStuff(Context context){
        ChurchKitDb db= ChurchKitDb.getInstance(context);
        long bibleBookId=db.bibleBookDao().insertBibleBook(new BibleBook("EX","EXOD", (short) 1,(short)2));
        System.out.println("Phanor vole: "+bibleBookId);
        db.bibleChapterDao().insertChapter(new BibleChapter("Notre pere", (short) 1,"fr",bibleBookId));
        db.bibleChapterDao().insertChapter(new BibleChapter("gbhhhjrhrhrhhb", (short) 2,"fr",bibleBookId));
        db.bibleChapterDao().insertChapter(new BibleChapter("grgjjhfghjfjfghjnf", (short) 3,"fr",bibleBookId));
    }*/

    public static String formatNumberToString(int number){
        if(number >99)
            return number+" ";
        if (number>9)
            return "0"+number+" ";

        return "00"+number+" ";

    }
}
