package com.churchkit.churchkit;

import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.Verse;
import com.churchkit.churchkit.modelview.bible.BibleViewModelGeneral4Insert;
import com.churchkit.churchkit.modelview.song.SongViewModelGeneral4Insert;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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


    public static void  prepopulateBibleFromJSonFile(Activity activity, String bibleName){

        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new BibleViewModelGeneral4Insert(activity.getApplication(),bibleName);
            }
        };


        bibleViewModelGeneral4Insert = new ViewModelProvider((ViewModelStoreOwner) activity,factory).
                get(BibleViewModelGeneral4Insert.class);




        FirebaseStorage fs=FirebaseStorage.getInstance();
        StorageReference storageRef = fs.getReference().child("bible/"+bibleName+"-v1"+".json");
        storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    String jsonStr = new String(bytes, "UTF-8");
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray data = jsonObject.getJSONArray("data");

                    for (int a =0;a<data.length();a++){
                        JSONObject songBookJson =data.getJSONObject(a);

                        int testament = songBookJson.getString("testament").equalsIgnoreCase("OT")?-1:1;

                        BibleBook bibleBook = new BibleBook(
                                songBookJson.getString("name")+songBookJson.getInt("position"),
                                songBookJson.getString("abbr"),
                                songBookJson.getString("name"),
                                songBookJson.getInt("position"),
                                testament,
                                songBookJson.getInt("amountChapter"));

                        JSONArray bibleChapterList =songBookJson.getJSONArray("bibleChapterList");
                        List<BibleChapter> bibleChapters = new ArrayList<>(50);

                        for (int jj=0;jj<bibleChapterList.length();jj++) {
                            JSONObject chapterJson = bibleChapterList.getJSONObject(jj);


                            BibleChapter bibleChapter = new BibleChapter(
                                    chapterJson.getString("bookName") + chapterJson.getInt("position"),
                                    songBookJson.getString("name"),
                                    chapterJson.getInt("position"),
                                    songBookJson.getString("name")+songBookJson.getInt("position"),
                                    songBookJson.getString("abbr")
                            );
                            bibleChapters.add(bibleChapter);
                            JSONArray bibleVerseArray =chapterJson.getJSONArray("bibleVerses");

                            List<BibleVerse> bibleVerseList = new ArrayList<>(30);
                            for (int i = 0; i < bibleVerseArray.length(); i++) {
                                JSONObject verseJson = bibleVerseArray.getJSONObject(i);

                                String reference = songBookJson.getString("abbr")+" "+chapterJson.getInt("position")+":"+verseJson.getInt("position");

                                bibleVerseList.add(
                                        new BibleVerse(
                                                /*verseJson.getString("reference")*/reference+verseJson.getInt("position")
                                                ,verseJson.getInt("position")
                                                ,/*verseJson.getString("reference")*/ reference
                                                ,verseJson.getString("verseText")
                                                ,chapterJson.getString("bookName") + chapterJson.getInt("position"))

                                );
                            }

                           // bibleViewModelGeneral4Insert.insert(bibleBook,bibleChapters,bibleVerseList  );
                        }

                    }



                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        });
    }
    public static void prepopulateSongFromJSonFile(Application application){
        songViewModelGeneral4Insert = ViewModelProvider.AndroidViewModelFactory.
                getInstance(application).create(SongViewModelGeneral4Insert.class);

        FirebaseStorage fs=FirebaseStorage.getInstance();
        StorageReference storageRef = fs.getReference().child("song/songbook-v2.json");
        storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    Gson gson = new Gson();
                    String jsonStr = new String(bytes, "UTF-8");
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject songBookJson = dataArray.getJSONObject(i);

                        SongBook songBook = gson.fromJson(songBookJson.toString(),SongBook.class);
                        songBook.setId( songBookJson.getString("id") );
                        songBook.setChildAmount(songBookJson.getInt("songAmount"));
                        songBook.setTitle( songBookJson.getString("name") );



                        JSONArray songArrayJson = songBookJson.getJSONArray("songList");
                        List<Song>songList = new ArrayList<>();
                        for (int j = 0; j < songArrayJson.length(); j++) {

                            JSONObject songObj = songArrayJson.getJSONObject(j);
                            Song song = gson.fromJson(songObj.toString(),Song.class);
                            song.setId( songObj.getString("id") );
                            song.setBookId( songBookJson.getString("id") );

                            song.setBookTitle( songBook.getTitle() );
                            song.setBookAbbreviation( songBook.getAbbreviation() );
                            songList.add(song);



                            JSONArray verseArray = songObj.getJSONArray("verseList");
                            List<Verse>verseList = new ArrayList<>();
                            for (int k = 0; k < verseArray.length(); k++) {
                                JSONObject verseObj = verseArray.getJSONObject(k);
                                Verse verse = gson.fromJson(verseObj.toString(),Verse.class);
                                verse.setVerseId( verseObj.getInt("position")+songObj.getString("id") );
                                verse.setSongId( songObj.getString("id") );
                                verse.setReference(song.getPosition()+" "+ songBook.getAbbreviation()+" "+verseObj.getInt("position") );
                                verseList.add( verse );
                            }

                            songViewModelGeneral4Insert.insert(songBook,songList,verseList);

                        }
                    }





                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        });
    }
    private static SongViewModelGeneral4Insert songViewModelGeneral4Insert;
    private static BibleViewModelGeneral4Insert bibleViewModelGeneral4Insert;

}
