package com.example.android.musicalstructure;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//////////////////////////////////////////////
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ArrayList<MVideo> videos;
    ArrayList<MAudio> audios;
    public static MediaPlayer mediaPlayer;
    ListView gridView;
    public static boolean showVideos = true;
    ActionBar bar;
///////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ////////////////////////////////////////ActionBar support
        bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Videos");
        //////////////////////////////////////// Navigation Drawer Section
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
        //////////////////////////////////////////////////grid view section
        videos = new ArrayList<>();
        audios = new ArrayList<>();
        gridView = findViewById(R.id.gridViewSongsList);
        androidRuntimePermission();
        getAllMediaMp4Files();
        VideosAdapter videosAdapter = new VideosAdapter(this, videos);
        if (videosAdapter.getCount() == 0) {
            gridView.setAdapter(videosAdapter);
            Toast.makeText(this, "nothing to display", Toast.LENGTH_SHORT).show();
        } else {
            gridView.setAdapter(videosAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void goToActivityChooseFolder() {
        Toast.makeText(this, "nothing to display", Toast.LENGTH_SHORT).show();

    }

    private void goToActivityHome() {
        Toast.makeText(this, "nothing to display", Toast.LENGTH_SHORT).show();

    }

    private void goToActivityInformation() {
        Toast.makeText(this, "nothing to display", Toast.LENGTH_SHORT).show();

    }

    private void goToActivityPrefernces() {
        Toast.makeText(this, "nothing to display", Toast.LENGTH_SHORT).show();

    }

    private void goToActivityRecentlyPlayed() {
        Toast.makeText(this, "nothing to display", Toast.LENGTH_SHORT).show();
    }

    public void getAllMediaMp3Files() {

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            Toast.makeText(MainActivity.this, "Something Went Wrong.", Toast.LENGTH_LONG);
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(MainActivity.this, "No Music Found on SD Card.", Toast.LENGTH_LONG);
        } else {
            int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int data = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int date = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED);
            int track = cursor.getColumnIndex(MediaStore.Audio.Media.TRACK);
            int artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int isMusic = cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC);
            int duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);

            do {
                //int videoIDV = cursor.getInt(id);
                File fileA = new File(cursor.getString(data));
                String artistA = cursor.getString(artist);
                String albumA = cursor.getString(album);
                String trackA = cursor.getString(track);
                String titleA = cursor.getString(title);
                Long durationA = cursor.getLong(duration);
                int isMusicA = cursor.getInt(isMusic);
                int dateA = cursor.getInt(date);
                if (isMusicA != 0) {
                    audios.add(new MAudio(albumA, titleA, artistA, durationA, trackA, dateA, fileA));
                }
            } while (cursor.moveToNext());
        }
    }

    public void getAllMediaMp4Files() {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            Toast.makeText(MainActivity.this, "Something Went Wrong.", Toast.LENGTH_LONG);
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(MainActivity.this, "No Music Found on SD Card.", Toast.LENGTH_LONG);
        } else {
            int title = cursor.getColumnIndex(MediaStore.Video.Media.TITLE);
            //Getting Song ID From Cursor.
            int id = cursor.getColumnIndex(MediaStore.Video.Media._ID);
            int data = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
            int date = cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED);
            int artist = cursor.getColumnIndex(MediaStore.Video.Media.ARTIST);
            int category = cursor.getColumnIndex(MediaStore.Video.Media.CATEGORY);
            int duration = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);
            int resolution = cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION);
            int album = cursor.getColumnIndex(MediaStore.Video.Media.ALBUM);

            do {
                //int videoIDV = cursor.getInt(id);
                File fileV = new File(cursor.getString(data));
                String artistV = cursor.getString(artist);
                String albumV = cursor.getString(album);
                String categoryV = cursor.getString(category);
                String titleV = cursor.getString(title);
                Long durationV = cursor.getLong(duration);
                Bitmap bMap = ThumbnailUtils.createVideoThumbnail(fileV.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
                int resolutionV = cursor.getInt(resolution);
                int dateV = cursor.getInt(date);
                videos.add(new MVideo(albumV, titleV, artistV, categoryV, dateV, durationV, bMap, resolutionV, fileV));
            } while (cursor.moveToNext());
        }
    }

    public void androidRuntimePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(MainActivity.this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 7);
                        }
                    });

                    alert_builder.setNeutralButton("Cancel", null);

                    AlertDialog dialog = alert_builder.create();

                    dialog.show();

                } else {

                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 7);
                }
            } else {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case 7: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
            }
        }
    }

    public void changeType(View view) {
        Button btn = (Button) view;
        if (showVideos) {
            showVideos = false;
            btn.setText("Videos");
            Drawable image = getResources().getDrawable(R.drawable.ic_video_library_black_24dp);
            btn.setCompoundDrawables(image, null, null, null);
            getAllMediaMp4Files();
            VideosAdapter videosAdapter = new VideosAdapter(this, videos);
            if (videosAdapter.getCount() == 0) {
                gridView.setAdapter(videosAdapter);
                Toast.makeText(this, "nothing to display", Toast.LENGTH_SHORT).show();
            } else {
                gridView.setAdapter(videosAdapter);
            }
            bar.setTitle("Videos");
        } else {
            showVideos = true;
            btn.setText("Audio");
            Drawable image = getResources().getDrawable(R.drawable.ic_audiotrack_black_24dp);
            btn.setCompoundDrawables(image, null, null, null);
            getAllMediaMp3Files();
            AudioAdapter videosAdapter = new AudioAdapter(this, audios);
            if (videosAdapter.getCount() == 0) {
                gridView.setAdapter(videosAdapter);
                Toast.makeText(this,"nothing to display",Toast.LENGTH_SHORT).show();
            } else {
                gridView.setAdapter(videosAdapter);
            }
            bar.setTitle("Audios");
        }
    }

}