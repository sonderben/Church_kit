package com.churchkit.churchkit;

import static com.churchkit.churchkit.ui.aboutapp.Payment.startPayment;
import static com.churchkit.churchkit.util.Constant.NOTIFICATION_CHANNEL_ID;
import static com.churchkit.churchkit.util.Constant.NOTIFICATION_CHANNEL_NAME;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.splashscreen.SplashScreen;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.churchkit.churchkit.database.entity.bible.BibleInfo;
import com.churchkit.churchkit.database.entity.song.SongInfo;
import com.churchkit.churchkit.databinding.ActivityMainBinding;
import com.churchkit.churchkit.modelview.bible.BibleFavoriteViewModel;
import com.churchkit.churchkit.modelview.bible.BibleHistoryViewModel;
import com.churchkit.churchkit.modelview.bible.BibleInfoViewModel;
import com.churchkit.churchkit.modelview.song.SongFavoriteViewModel;
import com.churchkit.churchkit.modelview.song.SongHistoryViewModel;
import com.churchkit.churchkit.modelview.song.SongInfoViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PaymentResultListener {




    @Override
    public void onBackPressed() {
        if(drawerLayout.isOpen())
            drawerLayout.close();
        else
            super.onBackPressed();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //obligao que sea on the top
        ckPreferences = new CKPreferences(this.getApplicationContext());




        ////
        Util.setAppLanguage(MainActivity.this,ckPreferences.getLanguage());
        //AppCompatDelegate.setDefaultNightMode( ckPreferences.getDarkMode() );



        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);


        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding=ActivityMainBinding.inflate(getLayoutInflater());


        setContentView(activityMainBinding.getRoot());

        init(activityMainBinding);



        splashScreen.setKeepOnScreenCondition(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        });
        createNotificationChannel(this);



        bibleInfoViewModel.getAllBibleInfo().observe(this, bibleInfoList -> {

            if (bibleInfoList.size()==0){
                bibleInfoViewModel.insert( BibleInfo.getAllBibleInfo() );
            }
        });

        songInfoViewModel.getAllSongInfo().observe(this, songInfos -> {
            if (songInfos.size() == 0){
                songInfoViewModel.insert( SongInfo.getAllSongInfo() );
            }
        });

        info = findViewById(R.id.bible);

        info.setOnClickListener(v -> {
            finish();
            System.exit(0);
        });










        badgeCountDrawerLayout();


        navView.setNavigationItemSelectedListener(this::onNavigationItemSelected);




    }



    private void init(ActivityMainBinding activityMainBinding){
        toolbar=activityMainBinding.toolbar;
        setSupportActionBar(toolbar);
        bottomNavigationView = activityMainBinding.bottomNav;
        drawerLayout = activityMainBinding.drawerLayout;
        navView = activityMainBinding.navView;
        navView.setItemIconTintList(null);


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        mNavController = navHostFragment.getNavController();

        AppBarConfiguration.Builder builder = new AppBarConfiguration.Builder(mNavController.getGraph());
        AppBarConfiguration appBarConfiguration = builder.setOpenableLayout(drawerLayout).build();

        NavigationUI.setupWithNavController(bottomNavigationView, mNavController); // bottom navigation
        NavigationUI.setupWithNavController(navView, mNavController); // drawer
        NavigationUI.setupWithNavController(toolbar, mNavController, appBarConfiguration); // toolbar

        bibleFavoriteViewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getApplication()).create(BibleFavoriteViewModel.class);
        bibleHistoryViewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getApplication()).create(BibleHistoryViewModel.class);

        songFavoriteViewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getApplication()).create(SongFavoriteViewModel.class);
        songHistoryViewModel = ViewModelProvider.AndroidViewModelFactory.
                getInstance(getApplication()).create(SongHistoryViewModel.class);


        bibleInfoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(BibleInfoViewModel.class);
        songInfoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(SongInfoViewModel.class);



    }

    BibleInfoViewModel bibleInfoViewModel;
    SongInfoViewModel songInfoViewModel;



    public void badgeCountDrawerLayout(){
        TextView songHistory,songFavorite,bibleHistory,bibleFavorite;

        songHistory= (TextView) navView.getMenu().findItem(R.id.songHistory).getActionView();
        songHistory.setGravity(Gravity.CENTER);

        songFavorite= (TextView) navView.getMenu().findItem(R.id.songFavorite).getActionView();
        songFavorite.setGravity(Gravity.CENTER);

        bibleHistory= (TextView) navView.getMenu().findItem(R.id.bibleHistory).getActionView();
        bibleHistory.setGravity(Gravity.CENTER);

        bibleFavorite= (TextView) navView.getMenu().findItem(R.id.bibleFavorite).getActionView();
        bibleFavorite.setGravity(Gravity.CENTER);

        TextView developer = (TextView) navView.getMenu().findItem(R.id.developer).getActionView();
        developer.setGravity(Gravity.CENTER);

        TextView version = (TextView) navView.getMenu().findItem(R.id.version).getActionView();
        version.setGravity(Gravity.CENTER);
        version.setText("1.0.0");


        bibleInfoViewModel.getAllBibleInfo().observe(this, bibleInfos -> {
            bibleAmountHistoryLiveData = bibleHistoryViewModel.getAmount( ckPreferences.getBibleName() );
            bibleAmountFavoriteLiveData = bibleFavoriteViewModel.getAmount(ckPreferences.getBibleName());

            if (bibleAmountHistoryLiveData.hasActiveObservers())
                bibleAmountHistoryLiveData.removeObservers(MainActivity.this);
            bibleAmountHistoryLiveData.observe(MainActivity.this, integer -> bibleHistory.setText(integer>0? "+"+integer:"" ));

            if (bibleAmountFavoriteLiveData.hasActiveObservers())
                bibleAmountFavoriteLiveData.removeObservers(MainActivity.this);
            bibleAmountFavoriteLiveData.observe(MainActivity.this, integer -> bibleFavorite.setText(integer>0? "+"+integer:"" ));
        });

        songInfoViewModel.getAllSongInfo().observe(this, songInfos -> {

            songHistoryViewModel.getAmount( ckPreferences.getSongName() ).observe(MainActivity.this, integer -> songHistory.setText(integer>0? "+"+integer:"" ));
            songFavoriteViewModel.getAmount( ckPreferences.getSongName() ).observe(MainActivity.this, integer -> songFavorite.setText(integer>0? "+"+integer:"" ));

            //
            songAmountHistoryLiveData = songHistoryViewModel.getAmount( ckPreferences.getSongName() );
            songAmountFavoriteLiveData = songFavoriteViewModel.getAmount(ckPreferences.getSongName());

            if (songAmountHistoryLiveData.hasActiveObservers())
                songAmountHistoryLiveData.removeObservers(MainActivity.this);
            songAmountHistoryLiveData.observe(MainActivity.this, integer -> songHistory.setText(integer>0? "+"+integer:"" ));

            if (songAmountFavoriteLiveData.hasActiveObservers())
                songAmountFavoriteLiveData.removeObservers(MainActivity.this);
            songAmountFavoriteLiveData.observe(MainActivity.this, integer -> songFavorite.setText(integer>0? "+"+integer:"" ));


        });



        developer.setText( Html.fromHtml(
                " By <br> <a a href=\"https://www.bendersonphanor.site/\">Bennderson</a>  and <a href=\"https://johnyoute.com/\">John Ersen</a>")
        );


        developer.setMovementMethod(LinkMovementMethod.getInstance());


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
                mNavController.getGraph().findNode(R.id.listSongsFragment).setLabel(getString(R.string.song_history));
                mNavController.navigate(R.id.listSongsFragment,bundle);
                drawerLayout.close();
                return true;
            case R.id.songFavorite:
                bundle.putString("FROM",Util.FROM_SONG_FAVORITE);
                drawerLayout.close();
                mNavController.getGraph().findNode(R.id.listSongsFragment).setLabel(getString(R.string.song_favorite));
                mNavController.navigate(R.id.listSongsFragment,bundle);
                return true;
            case R.id.bibleFavorite:
                bundle.putString("FROM",Util.FROM_BIBLE_FAVORITE);
                mNavController.getGraph().findNode(R.id.listChapterFragment).setLabel(getString(R.string.chapter_favorite));
                mNavController.navigate(R.id.listChapterFragment,bundle);
                drawerLayout.close();
                return true;
            case R.id.bibleHistory:
                bundle.putString("FROM",Util.FROM_BIBLE_HISTORY);
                mNavController.getGraph().findNode(R.id.listChapterFragment).setLabel(getString(R.string.chapter_history));
                mNavController.navigate(R.id.listChapterFragment,bundle);
                drawerLayout.close();
                return true;
            case R.id.aboutFragment:
                mNavController.getGraph().findNode(R.id.aboutFragment).setLabel(getString(R.string.about));
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    private MaterialToolbar toolbar;
    private CKPreferences ckPreferences;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private NavController mNavController;

    private NavigationView navView;
    private BibleFavoriteViewModel bibleFavoriteViewModel;
    private BibleHistoryViewModel bibleHistoryViewModel;
    private SongFavoriteViewModel songFavoriteViewModel;
    private SongHistoryViewModel songHistoryViewModel;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_SETTINGS = 12432;
    TextView info;
    LiveData<Integer> bibleAmountHistoryLiveData;
    LiveData<Integer> songAmountHistoryLiveData;

    LiveData<Integer> bibleAmountFavoriteLiveData;
    LiveData<Integer> songAmountFavoriteLiveData;
    public static void createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);//channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
