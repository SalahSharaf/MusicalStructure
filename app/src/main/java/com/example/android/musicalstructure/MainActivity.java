package com.example.android.musicalstructure;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ArrayList<File> media;
    public static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ///////////////////////////////////////
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(this.getResources().getDrawable(R.color.transparent));
        bar.setDisplayShowHomeEnabled(true);
        ///////////////////////////////////////
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        goToActivityHome();
                        break;
                    case R.id.menu_choose_folder:
                        goToActivityChooseFolder();
                        break;
                    case R.id.menu_information:
                        goToActivityInformation();
                        break;
                    case R.id.menu_preferences:
                        goToActivityPrefernces();
                        break;
                    case R.id.menu_recently_played:
                        goToActivityRecentlyPlayed();
                        break;
                }
                return true;
            }
        });
        ////////////////////////////////////////GridView section
        filesSynce();
        String names[] = new String[media.size()];
        for (int i = 0; i < media.size(); i++) {
            names[i] = media.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
        /////////////////////////////////////////MediaPlayer section

    }

    private void filesSynce() {
        String internalStoragePathName = System.getenv("EXTERNAL_STORAGE");
        String externalStoragePathName = System.getenv("SECONDARY_STORAGE");
        File internalStorage = new File(internalStoragePathName);
        File externalStorage = new File(externalStoragePathName);
        media = new ArrayList<>();
        if (internalStorage.exists() && internalStorage.isDirectory()) {
            File files[] = internalStorage.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".mp3") || name.endsWith(".wav");
                }
            });
            media.addAll(Arrays.asList(files));
        }
        if (externalStorage.exists() && externalStorage.isDirectory()) {
            File files[] = externalStorage.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".mp3") || name.endsWith(".wav");
                }
            });
            media.addAll(Arrays.asList(files));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void goToActivityChooseFolder() {
    }

    private void goToActivityHome() {
    }

    private void goToActivityInformation() {
    }

    private void goToActivityPrefernces() {
    }

    private void goToActivityRecentlyPlayed() {
    }

}
