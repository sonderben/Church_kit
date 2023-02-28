package com.churchkit.churchkit;

import android.app.ActivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.churchkit.churchkit.database.ChurchKitDb;
import com.churchkit.churchkit.database.entity.bible.BibleBook;
import com.churchkit.churchkit.database.entity.bible.BibleChapter;
import com.churchkit.churchkit.database.entity.bible.BibleVerse;
import com.churchkit.churchkit.database.entity.song.Song;
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.database.entity.song.Verse;
import com.churchkit.churchkit.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    @Override
    public void onBackPressed() {
        if(drawerLayout.isOpen())
            drawerLayout.close();
        else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding=ActivityMainBinding.inflate(getLayoutInflater());


        setContentView(activityMainBinding.getRoot());



        churchKitDb = ChurchKitDb.getInstance(this);

        init(activityMainBinding);



    }
    MaterialToolbar toolbar;
    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    NavController mNavController;
    ChurchKitDb churchKitDb;
    private FirebaseFirestore db;
    public static String generateRandomString(int length) {
        String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }


    private void prepopulateBibleFromJSonFile(){
        FirebaseStorage fs=FirebaseStorage.getInstance();
        StorageReference storageRef = fs.getReference().child("bible/HATBIV-v1.json");
        storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    String jsonStr = new String(bytes, "UTF-8");
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject data = jsonObject.getJSONObject("data");
                    Iterator<String> keys = data.keys();
                    while (keys.hasNext()){
                        JSONObject songBookJson =data.getJSONObject(keys.next());

                        int testament = songBookJson.getString("testament").equalsIgnoreCase("OT")?-1:1;

                        BibleBook bibleBook = new BibleBook(
                                songBookJson.getString("name")+songBookJson.getInt("position"),
                                songBookJson.getString("abbr"),
                                songBookJson.getString("name"),
                                songBookJson.getInt("position"),
                                testament,
                                songBookJson.getInt("amountChapter"));
                        Util.prepopulateBible(MainActivity.this,bibleBook);

                        JSONObject col = songBookJson.getJSONObject("__collections__");
                        boolean hasNext = col.keys().hasNext();
                        if (hasNext){
                            JSONObject listChapterJson = col.getJSONObject(col.keys().next());
                            Iterator<String> keys1 =listChapterJson.keys();
                            while (keys1.hasNext()) {
                                JSONObject chapterJson = listChapterJson.getJSONObject(keys1.next());

                                BibleChapter bibleChapter = new BibleChapter(
                                        chapterJson.getString("bookName") + chapterJson.getInt("position")
                                        ,chapterJson.getInt("position")
                                        ,songBookJson.getString("name")+songBookJson.getInt("position"));

                                Util.prepopulateBibleChapter(MainActivity.this,bibleChapter);

                                JSONArray bibleVerseArray =chapterJson.getJSONArray("bibleVerses");

                                List<BibleVerse> bibleVerseList = new ArrayList<>(30);
                                for (int i = 0; i < bibleVerseArray.length(); i++) {
                                    JSONObject verseJson = bibleVerseArray.getJSONObject(i);
                                    bibleVerseList.add(
                                            new BibleVerse(
                                                    verseJson.getString("reference")+verseJson.getInt("position")
                                                    ,verseJson.getInt("position")
                                                    ,verseJson.getString("reference")
                                                    ,verseJson.getString("verseText")
                                                    ,chapterJson.getString("bookName") + chapterJson.getInt("position"))
                                    );
                                }
                                System.out.println("bibleVerseList: "+Util.prepopulateBibleVerse(MainActivity.this,bibleVerseList));
                                //Util.prepopulateBibleVerse(MainActivity.this,bibleVerseList);
                            }
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
    private void prepopulateSongFromJSonFile(){
        FirebaseStorage fs=FirebaseStorage.getInstance();
        StorageReference storageRef = fs.getReference().child("song/songbook-v1.json");
        storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    String jsonStr = new String(bytes, "UTF-8");
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject data = jsonObject.getJSONObject("data");
                    Iterator<String> keys = data.keys();
                    while (keys.hasNext()){
                        JSONObject songBookJson =data.getJSONObject(keys.next());
                        System.out.println("konbye fwa");



                        String songBookId =songBookJson.getString("name")+songBookJson.getInt("position");
                        SongBook  songBook = new SongBook(
                                songBookId,
                                songBookJson.getString("name"),
                                songBookJson.getString("abbreviation"),
                                songBookJson.getInt("songAmount"),
                                songBookJson.getInt("position")
                        );

                        Util.prepopulateSonBook(MainActivity.this,songBook);

                        JSONObject col = songBookJson.getJSONObject("__collections__");
                        boolean hasNext = col.keys().hasNext();

                        if (hasNext){
                            JSONObject listChapterJson = col.getJSONObject(col.keys().next());
                            Iterator<String> keys1 =listChapterJson.keys();

                            while (keys1.hasNext()) {
                                JSONObject chapterJson = listChapterJson.getJSONObject(keys1.next());

                                String songId = chapterJson.getString("id");
                                //System.out.println("cooooooooool: "+songId);
                                if (songId != null){
                                    Song song = new Song(
                                            songId
                                            ,chapterJson.getString("title")
                                            ,chapterJson.getInt("position")
                                            ,chapterJson.getInt("page")
                                            , songBookId);


                                    Util.prepopulateSong(MainActivity.this,song);

                                    JSONArray bibleVerseArray =chapterJson.getJSONArray("verseList");

                                    List<Verse> bibleVerseList = new ArrayList<>(30);
                                    for (int i = 0; i < bibleVerseArray.length(); i++) {
                                        JSONObject verseJson = bibleVerseArray.getJSONObject(i);

                                        bibleVerseList.add(
                                                new Verse(verseJson.getInt("position")+songId
                                                        ,verseJson.getString("verse")
                                                        ,verseJson.getInt("position")
                                                        ,songId)
                                        );

                                    }
                                    System.out.println("bibleVerseList: "+Util.prepopulateSongVerse(MainActivity.this,bibleVerseList));
                                }
                            }
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








    private void init(ActivityMainBinding activityMainBinding){



       // getJsonFromFirebaseStorage();


        db = FirebaseFirestore.getInstance();
        churchKitDb.bibleBookDao().getAllBibleBook().observe(this, new Observer<List<BibleBook>>() {
            @Override
            public void onChanged(List<BibleBook> bibleBooks) {
                if(bibleBooks.size() == 0){
                    prepopulateBibleFromJSonFile();
                }
            }
        });

        churchKitDb.songBookDao().getAllSongBook().observe(this, new Observer<List<SongBook>>() {
            @Override
            public void onChanged(List<SongBook> songBooks) {
                if(songBooks.size() == 0) {
                    prepopulateSongFromJSonFile();
                }
            }
        });



/*
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
        startActivity(intent);
*/

        toolbar=activityMainBinding.toolbar;
        bottomNavigationView = activityMainBinding.bottomNav;
        drawerLayout = activityMainBinding.drawerLayout;
        NavigationView navView = activityMainBinding.navView;
        navView.setItemIconTintList(null);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        mNavController = navHostFragment.getNavController();

        AppBarConfiguration.Builder builder = new AppBarConfiguration.Builder(mNavController.getGraph());
        AppBarConfiguration appBarConfiguration ;


        NavigationUI.setupWithNavController(bottomNavigationView, mNavController);
        appBarConfiguration = builder.setOpenableLayout(drawerLayout).build();


        TextView songHistory;

        songHistory=
                (TextView) navView.getMenu().findItem(R.id.song_history).getActionView();
        songHistory.setGravity(Gravity.CENTER);
        songHistory.setText("+12");


        NavigationUI.setupWithNavController(navView, mNavController);
        NavigationUI.setupWithNavController(toolbar, mNavController, appBarConfiguration);

        mNavController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if ( isBottomFragment(navController1) ){
                toolbar.setNavigationIcon(R.drawable.more_24);
            }else
                toolbar.setNavigationIcon(R.drawable.arrow_back_24);
        });

        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(v -> {
            if (!drawerLayout.isOpen() && isBottomFragment(mNavController) ){
                drawerLayout.open();
            }
            else
                onBackPressed();
        });

    }

    private boolean isBottomFragment(@NonNull NavController navController) {
      return   navController.getCurrentDestination().getLabel().equals(getResources().getString(R.string.song_hope)) ||
                navController.getCurrentDestination().getLabel().equals(getResources().getString(R.string.bible)) ||
                navController.getCurrentDestination().getLabel().equals(getResources().getString(R.string.more));
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }


}