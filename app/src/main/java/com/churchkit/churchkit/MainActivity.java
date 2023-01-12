package com.churchkit.churchkit;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.churchkit.churchkit.databinding.ActivityMainBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding=ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_main);

        setContentView(activityMainBinding.getRoot());


        toolbar=activityMainBinding.toolbar;//findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = activityMainBinding.drawerLayout;//findViewById(R.id.drawer_layout);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        //the above code gets a reference to the navigation controller fromthe navigation host
        AppBarConfiguration.Builder builder = new AppBarConfiguration.Builder(navController.getGraph());
        AppBarConfiguration appBarConfiguration ;//= builder.build();  =>  is redundant => li init nn lign 43 a
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        appBarConfiguration = builder.setOpenableLayout(drawerLayout).build();


        //builder.setOpenableLayout(drawerLayout);   // is redundant => li fet deja nn lign 43

       // builder.setOpenableLayout(drawerLayout);   // is redundant
        NavigationView navView = activityMainBinding.navView; //findViewById(R.id.nav_view);
        //builder.setOpenableLayout(drawerLayout);   // is redundant
        NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);


        // pemet u met diferant menu pou chak fragment
        // li vin fe button drawer a pa mache, button back la tou <-
        // m gen yn ide poum ranje sa
        setSupportActionBar(toolbar);

       /* toolbar.setNavigationIcon(R.drawable.bookmark); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });*/


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item,
                navController) || super.onOptionsItemSelected(item);
    }
}