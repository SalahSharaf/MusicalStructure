package com.example.android.musicalstructure;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AppVisibilityDetector.AppVisibilityCallback {

    private static final int READ_STORAGE = 7;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    public static ArrayList<MVideo> videos;
    public static ArrayList<MAudio> audios;
    ActionBar bar;
    VideosAdapter videosAdapter;
    MusicAdapter musicAdapter;
    ImageView sliderLayout;
    TextView autoSliderText;
    ListView gridView;
    //////////////////////////////////////////////
    boolean menuItemSwitch = true;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ////////////////////////////////////////ActionBar support
        bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Videos");
        //////////////////////////////////////////////////grid view section
        videos = new ArrayList<>();
        audios = new ArrayList<>();
        gridView = findViewById(R.id.gridViewSongsList);
        gridView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        androidRuntimePermission();
        videosAdapter = new VideosAdapter(this, videos);
        musicAdapter = new MusicAdapter(this, audios);
        gridView.setAdapter(videosAdapter);
        //////////////////////////////////////// Navigation Drawer Section
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.musicVideo:
                        menuItemSwitch = !menuItemSwitch;
                        if (menuItemSwitch) {
                            getAllMediaMp4Files();
                            gridView.setAdapter(videosAdapter);
                            Toast.makeText(getBaseContext(), "Videos", Toast.LENGTH_SHORT).show();
                            item.setIcon(R.drawable.ic_audiotrack_black_24dp);
                            sliderLayout.setVisibility(View.VISIBLE);
                            autoSliderText.setVisibility(View.VISIBLE);
                            item.setTitle("Music");
                            bar.setTitle("Videos");
                        } else if (!menuItemSwitch) {
                            getAllMediaMp3Files();
                            gridView.setAdapter(musicAdapter);
                            Toast.makeText(getBaseContext(), "Music", Toast.LENGTH_SHORT).show();
                            item.setIcon(R.drawable.ic_video_library_black_24dp);
                            sliderLayout.setVisibility(View.GONE);
                            autoSliderText.setVisibility(View.GONE);
                            item.setTitle("Videos");
                            bar.setTitle("Music");
                        }
                        break;
                    case R.id.sync:
                        if (!menuItemSwitch) {
                            getAllMediaMp3Files();
                            Toast.makeText(MainActivity.this, " Music synced", Toast.LENGTH_SHORT).show();
                        } else if (menuItemSwitch) {
                            getAllMediaMp4Files();
                            Toast.makeText(MainActivity.this, " Videos synced", Toast.LENGTH_SHORT).show();
                        }
                        break;
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
                        goToActivityPreferences();
                        break;
                    case R.id.menu_recently_played:
                        goToActivityRecentlyPlayed();
                        break;
                }
                return true;
            }
        });
        ////////////////////////////////////////////////////Video slider section
        sliderLayout = findViewById(R.id.autoSlider);
        autoSliderText = findViewById(R.id.autoSliderText);
        sliderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < videos.size(); i++) {
                    if (autoSliderText.getText() == videos.get(i).getTitle()) {
                        Intent intent = new Intent(getBaseContext(), DisplayVideoActivity.class);
                        DisplayVideoActivity.position = videos.get(i).getId();
                        startActivity(intent);
                    }
                }
            }
        });
        //////////////////////////////////////////////////////update views
        final Handler handler2 = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                try {
                    sliderLayout.setImageBitmap(videos.get(i).getThumbonial());
                    autoSliderText.setText(videos.get(i).getTitle());
                    i++;
                    if (i > videos.size() - 1) {
                        i = 0;
                    }
                }catch (Exception e){

                }
                    handler2.postDelayed(this, 4000);  //for interval...
            }
        };
        handler2.postDelayed(runnable, 0); //for initial delay..
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void goToActivityChooseFolder() {
        Toast.makeText(this, "Choose Folder", Toast.LENGTH_SHORT).show();
    }

    private void goToActivityHome() {
        Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
    }

    private void goToActivityInformation() {
        Toast.makeText(this, "Information", Toast.LENGTH_SHORT).show();
    }

    private void goToActivityPreferences() {
        Toast.makeText(this, "PreferencesActivity", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
    }

    private void goToActivityRecentlyPlayed() {
        Toast.makeText(this, "Recently Played", Toast.LENGTH_SHORT).show();
    }

    public void getAllMediaMp3Files() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            Toast.makeText(MainActivity.this, "Something Went Wrong.", Toast.LENGTH_LONG).show();
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(MainActivity.this, "No Music Found on SD Card.", Toast.LENGTH_LONG).show();
        } else if (cursor != null && cursor.moveToNext()) {

            int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int data = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int date = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED);
            int track = cursor.getColumnIndex(MediaStore.Audio.Media.TRACK);
            int artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int isMusic = cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC);
            int duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int albumArt = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            do {
                File fileA = new File(cursor.getString(data));
                String artistA = cursor.getString(artist);
                String albumA = cursor.getString(album);
                String trackA = cursor.getString(track);
                String titleA = cursor.getString(title);
                Long durationA = cursor.getLong(duration);
                int isMusicA = cursor.getInt(isMusic);
                int dateA = cursor.getInt(date);
                String albumArtString = "";
                try {
                    albumArtString = cursor.getString(albumArt);
                } catch (Exception e) {

                }
                if (isMusicA != 0) {
                    audios.add(new MAudio(albumA, titleA, artistA, durationA, trackA, dateA, fileA, albumArtString));
                }

            } while (cursor.moveToNext());
        }
    }

    public void getAllMediaMp4Files() {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            Toast.makeText(MainActivity.this, "Something Went Wrong.", Toast.LENGTH_LONG).show();
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(MainActivity.this, "No videos Found on SD Card.", Toast.LENGTH_LONG).show();
        } else {
            int title = cursor.getColumnIndex(MediaStore.Video.Media.TITLE);
            int data = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
            int date = cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED);
            int artist = cursor.getColumnIndex(MediaStore.Video.Media.ARTIST);
            int category = cursor.getColumnIndex(MediaStore.Video.Media.CATEGORY);
            int duration = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);
            int resolution = cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION);
            int album = cursor.getColumnIndex(MediaStore.Video.Media.ALBUM);

            do {
                //int idV = cursor.getInt(id);
                File fileV = new File(cursor.getString(data));
                String artistV = cursor.getString(artist);
                String albumV = cursor.getString(album);
                String categoryV = cursor.getString(category);
                String titleV = cursor.getString(title);
                Long durationV = cursor.getLong(duration);
                Bitmap bMap = ThumbnailUtils.createVideoThumbnail(fileV.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
                int resolutionV = cursor.getInt(resolution);
                int dateV = cursor.getInt(date);
                videos.add(new MVideo(cursor.getPosition(), albumV, titleV, artistV, categoryV, dateV, durationV, bMap, resolutionV, fileV));
            } while (cursor.moveToNext());
        }
    }

    public void androidRuntimePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if(!MainActivity.this.isFinishing()) {
                        AlertDialog.Builder alert_builder = new AlertDialog.Builder(this);
                        alert_builder.setMessage("External Storage Permission is Required.");
                        alert_builder.setTitle("Please Grant Permission.");
                        alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE);
                            }
                        });

                        alert_builder.setNeutralButton("Cancel", null);

                        AlertDialog dialog = alert_builder.create();

                        dialog.show();
                    }

                } else {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE);
                }
            } else {
                getAllMediaMp4Files();
                getAllMediaMp3Files();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case READ_STORAGE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
                    getAllMediaMp4Files();
                    getAllMediaMp3Files();
                } else {
                    if(!MainActivity.this.isFinishing()) {
                        AlertDialog.Builder alert_builder = new AlertDialog.Builder(getApplicationContext());
                        alert_builder.setMessage("External Storage Permission is Required.\nto access all media files music and videos");
                        alert_builder.setTitle("Please Grant Permission.");
                        alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE);
                            }
                        });
                        alert_builder.setNeutralButton("Cancel", null);
                        AlertDialog dialog = alert_builder.create();
                        dialog.show();

                    }
                }
            }
        }
    }

    @Override
    public void onAppGotoForeground() {

    }

    @Override
    public void onAppGotoBackground() {
        startActivity(new Intent(this, MyService.class));
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("switch", menuItemSwitch);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.musicVideo);
        if (menuItemSwitch) {
            menuItem.setIcon(R.drawable.ic_audiotrack_black_24dp);
            menuItem.setTitle("Music");
            sliderLayout.setVisibility(View.VISIBLE);
            autoSliderText.setVisibility(View.VISIBLE);
            bar.setTitle("Videos");
            gridView.setAdapter(videosAdapter);

        } else if (!menuItemSwitch) {
            menuItem.setIcon(R.drawable.ic_video_library_black_24dp);
            menuItem.setTitle("Videos");
            sliderLayout.setVisibility(View.GONE);
            autoSliderText.setVisibility(View.GONE);
            bar.setTitle("Music");
            gridView.setAdapter(musicAdapter);

        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        menuItemSwitch = savedInstanceState.getBoolean("switch");
    }

}