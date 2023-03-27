package com.churchkit.churchkit;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.widget.TextView;

import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.Verse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Util {
    public final static String FROM_SONG_FAVORITE= "FROM_SONG_FAVORITE";
    public final static String FROM_SONG_HISTORY = "FROM_SONG_HISTORY";
    public final static String FROM_SONG_HOPE = "FROM_SONG_HOPE";

    public final static String FROM_BIBLE_FAVORITE= "FROM_BIBLE_FAVORITE";
    public final static String FROM_BIBLE_HISTORY = "FROM_BIBLE_HISTORY";
    public final static String FROM_BIBLE = "FROM_BIBLE";

    public static final int IMAGE =1;
     public static final int BOOK_MARK =2;

    public static String getSelectedText(TextView tv) {
        String selectedText = null;

        int start = tv.getSelectionStart();
        int end = tv.getSelectionEnd();

        if (start != -1 && end != -1) {
            selectedText = tv.getText().toString().substring(start, end);
        }

        return selectedText;
    }
    public static void copyText(Context context,String textToCopy,String label){

        textToCopy += "\n https://www.church-kit.com/download";

        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText(label, textToCopy);
        clipboard.setPrimaryClip(clip);

    }
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

    public static List<Integer>getAllFont(){
        return Arrays.asList(R.font.fasthand,R.font.great_vibes,R.font.indie_flower,R.font.robotolight
                ,R.font.roboto_thin,R.font.roboto_bold,R.font.sono_regular,R.font.sono_bold,R.font.tangerine_regular,
                R.font.tangerine_bold);
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


    public static void setAppLanguage(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static String formatNumberToString(int number){
        if(number >99)
            return number+" ";
        if (number>9)
            return "0"+number+" ";

        return "00"+number+" ";

    }
}
