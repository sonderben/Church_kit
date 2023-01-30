package com.churchkit.churchkit;

import android.app.ActivityManager;
import android.graphics.Color;
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
import com.churchkit.churchkit.database.entity.song.SongBook;
import com.churchkit.churchkit.databinding.ActivityMainBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    public void onBackPressed() {
        if(drawerLayout.isOpen())
            drawerLayout.close();
       /* else if (isBottomFragment(mNavController)){
            super.onBackPressed();
            super.onBackPressed();
        }*/
        else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding=ActivityMainBinding.inflate(getLayoutInflater());


        setContentView(activityMainBinding.getRoot());

        ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();
        System.out.println("memoryInfo: "+memoryInfo.availMem+"memoryInfo: "+memoryInfo.threshold);

        //1 138 233 344 memoryInfo: 226 492 416
churchKitDb = ChurchKitDb.getInstance(this);



        churchKitDb.songBookDao().getAllSongBook().observe(this, new Observer<List<SongBook>>() {
            @Override
            public void onChanged(List<SongBook> songBook) {
                if (songBook == null || songBook.size() == 0){
                    try {
                        Util.prepopulateDatabaseFromJsonFile(getApplicationContext());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
        /*songHistory=(TextView) MenuItemCompat.getActionView(navView.getMenu().
                findItem(R.id.song_history));
        gallery.setGravity(Gravity.CENTER);
        gallery.setText("+12");*/
        songHistory=
                (TextView) navView.getMenu().findItem(R.id.song_history).getActionView();
        songHistory.setGravity(Gravity.CENTER);
        songHistory.setText("+12");
//

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

    MaterialToolbar toolbar;
    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    NavController mNavController;
    ChurchKitDb churchKitDb;



    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }


}