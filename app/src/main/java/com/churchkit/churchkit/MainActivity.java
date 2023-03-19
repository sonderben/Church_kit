package com.churchkit.churchkit;

import static com.churchkit.churchkit.ui.aboutapp.Payment.startPayment;

import android.Manifest;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PaymentResultListener {


    private static final int MY_PERMISSIONS_REQUEST_WRITE_SETTINGS = 12432;

    @Override
    public void onBackPressed() {
        if(drawerLayout.isOpen())
            drawerLayout.close();
        else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         ckPreferences = new CKPreferences(MainActivity.this);
        Util.setAppLanguage(MainActivity.this,ckPreferences.getLanguage());


        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding=ActivityMainBinding.inflate(getLayoutInflater());


        AppCompatDelegate.setDefaultNightMode( ckPreferences.getDarkMode() );


        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_SETTINGS},
                    MY_PERMISSIONS_REQUEST_WRITE_SETTINGS);
        }else {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 128);
        }*/





//Songs and scripture to nourish your soul.
        setContentView(activityMainBinding.getRoot());



        churchKitDb = ChurchKitDb.getInstance(this);

        init(activityMainBinding);
        navView.setNavigationItemSelectedListener(this::onNavigationItemSelected);




    }
    MaterialToolbar toolbar;
    CKPreferences ckPreferences;
    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    NavController mNavController;
    ChurchKitDb churchKitDb;
    private FirebaseFirestore db;
    NavigationView navView;
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
                                bibleChapter.setBibleBookAbbr( songBookJson.getString("abbr") );

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

                                Util.prepopulateBibleVerse(MainActivity.this,bibleVerseList);
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
                       songBook.setSongBookId( songBookJson.getString("id") );



                       Util.prepopulateSonBook(MainActivity.this,songBook);

                      JSONArray songArrayJson = songBookJson.getJSONArray("songList");
                        for (int j = 0; j < songArrayJson.length(); j++) {
                            JSONObject songObj = songArrayJson.getJSONObject(j);
                            Song song = gson.fromJson(songObj.toString(),Song.class);
                            song.setSongID( songObj.getString("id") );
                            song.setSongBookId( songBookJson.getString("id") );

                            song.setBookName( songBook.getName() );
                            song.setBookNameAbbr( songBook.getAbbreviation() );

                            Util.prepopulateSong(MainActivity.this,song);

                            JSONArray verseArray = songObj.getJSONArray("verseList");
                            List<Verse>verseList = new ArrayList<>();
                            for (int k = 0; k < verseArray.length(); k++) {
                                JSONObject verseObj = verseArray.getJSONObject(k);
                                Verse verse = gson.fromJson(verseObj.toString(),Verse.class);
                                verse.setVerseId( verseObj.getInt("position")+songObj.getString("id") );
                                verse.setSongId( songObj.getString("id") );
                                verseList.add( verse );
                            }
                            Util.prepopulateSongVerse(MainActivity.this,verseList);
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

        PhoneInfo phoneInfo = new PhoneInfo();
        System.out.println("phone info: "+phoneInfo);

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
         navView = activityMainBinding.navView;
        navView.setItemIconTintList(null);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        mNavController = navHostFragment.getNavController();

        AppBarConfiguration.Builder builder = new AppBarConfiguration.Builder(mNavController.getGraph());
        AppBarConfiguration appBarConfiguration ;


        NavigationUI.setupWithNavController(bottomNavigationView, mNavController);
        appBarConfiguration = builder.setOpenableLayout(drawerLayout).build();


        TextView songHistory;

        songHistory=
                (TextView) navView.getMenu().findItem(R.id.songHistory).getActionView();
        songHistory.setGravity(Gravity.CENTER);
        songHistory.setText("+12");



        NavigationUI.setupWithNavController(navView, mNavController);
        NavigationUI.setupWithNavController(toolbar, mNavController, appBarConfiguration);

        mNavController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if ( isBottomFragment(navController1) ){
                toolbar.setNavigationIcon(R.drawable.menu_24);
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
      return   navController.getCurrentDestination().getLabel().equals(getResources().getString(R.string.app_name));
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Bundle bundle = new Bundle();

        switch (item.getItemId()){
            case R.id.songHistory:
                bundle.putString("FROM",Util.FROM_SONG_HISTORY);
                mNavController.getGraph().findNode(R.id.listSongsFragment).setLabel("Song Histories");
                mNavController.navigate(R.id.listSongsFragment,bundle);
                drawerLayout.close();
                return true;
            case R.id.songFavorite:
                bundle.putString("FROM",Util.FROM_SONG_FAVORITE);
                drawerLayout.close();
                mNavController.getGraph().findNode(R.id.listSongsFragment).setLabel("Song Favorites");
                mNavController.navigate(R.id.listSongsFragment,bundle);
            case R.id.bibleFavorite:
                bundle.putString("FROM",Util.FROM_BIBLE_FAVORITE);
                mNavController.getGraph().findNode(R.id.listChapterFragment).setLabel("Chapter Favorites");
                mNavController.navigate(R.id.listChapterFragment,bundle);
                drawerLayout.close();
                return true;
            case R.id.bibleHistory:
                bundle.putString("FROM",Util.FROM_BIBLE_HISTORY);
                mNavController.getGraph().findNode(R.id.listChapterFragment).setLabel("Chapter Histories");
                mNavController.navigate(R.id.listChapterFragment,bundle);
                drawerLayout.close();
                return true;
            case R.id.aboutFragment:
                mNavController.getGraph().findNode(R.id.aboutFragment).setLabel("About");
                mNavController.navigate(R.id.aboutFragment);
                drawerLayout.close();
                return true;
            case R.id.donateFragment:
                Checkout.preload( getApplicationContext() );
                startPayment(MainActivity.this);
                drawerLayout.close();
                return true;
            case R.id.faqFragment:
                mNavController.getGraph().findNode(R.id.faqFragment).setLabel("Faq");
                mNavController.navigate(R.id.faqFragment);
                drawerLayout.close();
                return true;
        }
        return false;

    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(MainActivity.this,"Thank you for your support.",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPaymentError(int i, String s) {
       // Toast.makeText(MainActivity.this,"onPaymentError",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_SETTINGS) {
            // Si el usuario concedió el permiso, continuar con la operación
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 128);
            } else {
                Toast.makeText(MainActivity.this,"No vas a utilizar este funcion hasta que da este permiso",Toast.LENGTH_LONG).show();
            }
        }
    }



}







