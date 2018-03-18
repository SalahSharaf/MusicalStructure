package com.example.android.musicalstructure;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.AudioPlaybackConfiguration;
import android.media.MediaPlayer;
import android.media.VolumeShaper;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.AnimatorRes;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.musicalstructure.DisplayVideoActivity.videoMedia;

public class MusicDisplayActivity extends AppCompatActivity implements AppVisibilityDetector.AppVisibilityCallback {
    //ImageView circularImage, coverImage;

    ImageButton play, repeat, shuffle, seekforward, seekbackward;
    TextView text;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    ListView listView;
    ArrayList<MAudio> mAudios;
    public static int position;
    public static MediaPlayer mediaPlayer;
    SeekBar seekBar;
    int currentMusicPosition;
    //RotateAnimation rotateAnimation;
    MusicListNavigationAdapter adapter;
    boolean selected[] = new boolean[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_display);
        ////////////////////////initializations
        mAudios = MainActivity.audios;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        AudioManager.OnAudioFocusChangeListener listener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                    mediaPlayer.start();
                } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                    mediaPlayer.stop();
                } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                    int MAX_VOLUME = 50;
                    AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
                    int soundVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                    float volume = (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math.log(MAX_VOLUME)));
                    mediaPlayer.setVolume(volume, volume);
                } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                    mediaPlayer.pause();
                }
            }
        };
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // Request audio focus for play back
        int result = am.requestAudioFocus(listener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);
        /////
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

        } else if (result == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
            Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, this.getClass()));
        }
        // circularImage = findViewById(R.id.music_activity_circularImage);
        // coverImage = findViewById(R.id.music_activity_image);
        play = findViewById(R.id.playButton);
        repeat = findViewById(R.id.repeat);
        shuffle = findViewById(R.id.shuffle);
        seekbackward = findViewById(R.id.seekBackwards);
        seekforward = findViewById(R.id.seekTowards);
        ////////////////////////
        // circularRotatingAnimation(6000);
        text = new TextView(this);
        text.setTextColor(getResources().getColor(R.color.colorAccent));
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        text.setText(mAudios.get(position).getTitle());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(text);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //////////////////////////////////////////////////////////////setting up the Navigation View
        drawerLayout = findViewById(R.id.musicActivity_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        //////////////////////////////////////////////////////////////
        listView = findViewById(R.id.music_navigation_list);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        adapter = new MusicListNavigationAdapter(this, mAudios);
        listView.setAdapter(adapter);
        /////////////////////////////////////////////////////////////
        try {
            mediaPlayer.setDataSource(mAudios.get(position).getData().getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Toast.makeText(this, "Error Occurred ", Toast.LENGTH_SHORT).show();
        }
        seekBar = findViewById(R.id.musicSeekBar);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //////////////////////////////////////////////////////////////update
        final Handler handle = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    play.setSelected(true);
                } else if (!mediaPlayer.isPlaying()) {
                    play.setSelected(false);
                }
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handle.postDelayed(this, 1000);
            }
        };
        handle.post(run);
        ///////////////////////////////////////////////////////////////
        Bitmap image = BitmapFactory.decodeFile(mAudios.get(position).getAlbumArtPath());
        if (image != null) {
            //    circularImage.setImageBitmap(image);
            //    coverImage.setImageBitmap(image);
        } else if (image == null) {
            //   circularImage.setImageResource(R.drawable.a3);
            //   coverImage.setImageResource(R.drawable.a2);
        }
        ///////////////////////////////////////////////////////////////
        noRepeat();
    }

    //  private void circularRotatingAnimation(int duration) {
    //     rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    //     rotateAnimation.setDuration(duration);
    //     rotateAnimation.setRepeatCount(Animation.INFINITE);
    //     circularImage.setAnimation(rotateAnimation);
    //  }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) | super.onOptionsItemSelected(item);
    }

    @Override
    public void onAppGotoForeground() {

    }

    @Override
    public void onAppGotoBackground() {
        Toast.makeText(this, "background", Toast.LENGTH_SHORT).show();
    }

    public void listRepeatMode(View view) {
        initializePopUpWindow(view);
    }

    public void listAsynce(View view) {
        ArrayList<MAudio> audios = getAllMediaMp3Files();
        adapter = new MusicListNavigationAdapter(this, audios);
        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        Toast.makeText(this, "List synced", Toast.LENGTH_SHORT).show();
    }

    public void listShuffle(View view) {

    }

    public void play(View view) {
        if (mediaPlayer.isPlaying()) {
            play.setSelected(false);
            mediaPlayer.pause();
            //  circularImage.clearAnimation();
        } else if (!mediaPlayer.isPlaying()) {
            play.setSelected(true);
            mediaPlayer.start();
            //   circularImage.setAnimation(rotateAnimation);
            //    circularImage.animate();
        }
    }

    public void seekBack(View view) {
        int currentSec = mediaPlayer.getCurrentPosition();
        if (currentSec != 0) {
            int subtractedSec = 1000 * 10; ///ten seconds
            mediaPlayer.seekTo(currentSec - subtractedSec);
            seekBar.setProgress(currentSec - subtractedSec);
        }
    }

    public void seekForward(View view) {
        int currentSec = mediaPlayer.getCurrentPosition();
        if (currentSec != mediaPlayer.getDuration()) {
            int addedSec = 1000 * 10; ///ten seconds
            mediaPlayer.seekTo(currentSec + addedSec);
            seekBar.setProgress(currentSec + addedSec);
        }
    }

    public void shuffle(View view) {

    }

    public void repeat(View view) {
        initializePopUpWindow(view);
    }

    private void initializePopUpWindow(View view) {
        View menuView = LayoutInflater.from(this).inflate(R.layout.repeat_pop_up_window, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        PopupWindow menu = new PopupWindow(menuView, width, height, focusable);
        menu.showAtLocation(view, Gravity.CENTER_HORIZONTAL, (int) view.getX(), (int) view.getY());
        RadioGroup group = menuView.findViewById(R.id.options_radio_group);
        RadioButton btn1 = menuView.findViewById(R.id.pop_up_window_No_repeat);
        btn1.setChecked(selected[0]);
        RadioButton btn2 = menuView.findViewById(R.id.pop_up_window_No_repeat);
        btn2.setChecked(selected[1]);
        RadioButton btn3 = menuView.findViewById(R.id.pop_up_window_No_repeat);
        btn3.setChecked(selected[2]);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.pop_up_window_No_repeat) {
                    selected[0] = true;
                    selected[1] = false;
                    selected[2] = false;
                    Toast.makeText(getBaseContext(), "No Repeat", Toast.LENGTH_SHORT).show();
                    noRepeat();
                } else if (checkedId == R.id.pop_up_window_repeat_all) {
                    selected[0] = false;
                    selected[1] = true;
                    selected[2] = false;
                    Toast.makeText(MusicDisplayActivity.this, "Repeat All file", Toast.LENGTH_SHORT).show();
                    repeatAllFiles();
                } else if (checkedId == R.id.pop_up_window_repeat_one) {
                    selected[0] = false;
                    selected[1] = false;
                    selected[2] = true;
                    Toast.makeText(MusicDisplayActivity.this, "Repeat one file", Toast.LENGTH_SHORT).show();
                    repeatOneFile();
                }
            }
        });
    }

    private void noRepeat() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });
    }

    private void repeatOneFile() {
        mediaPlayer.setLooping(true);
    }

    private void repeatAllFiles() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (position >= mAudios.size()) {
                    position = 0;
                } else if (position != (mAudios.size() - 1)) {
                    position ++;
                    try {
                        mp.release();
                        mp.setDataSource(mAudios.get(position).getData().getAbsolutePath());
                        mp.prepare();
                    } catch (Exception e) {
                        Toast.makeText(MusicDisplayActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                    }
                    mp.start();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("progress", mediaPlayer.getCurrentPosition());
        outState.putBooleanArray("selected", selected);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        currentMusicPosition = savedInstanceState.getInt("progress");
        selected = savedInstanceState.getBooleanArray("selected");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.seekTo(currentMusicPosition);
    }

    public ArrayList<MAudio> getAllMediaMp3Files() {
        ArrayList<MAudio> audios = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            Toast.makeText(MusicDisplayActivity.this, "Error Occurred.", Toast.LENGTH_LONG).show();
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(MusicDisplayActivity.this, "No Music Found on SD Card.", Toast.LENGTH_LONG).show();
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
        return audios;
    }
}



