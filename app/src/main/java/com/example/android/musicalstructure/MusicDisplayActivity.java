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
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.android.musicalstructure.DisplayVideoActivity.videoMedia;

public class MusicDisplayActivity extends AppCompatActivity implements AppVisibilityDetector.AppVisibilityCallback {
    ImageButton play, repeat, shuffle, seekforward, seekbackward;
    ImageView coverImage;
    TextView text;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    ListView listView;
    ArrayList<MAudio> mAudios;
    public static int position, lastPosition = -1;
    public static MediaPlayer mediaPlayer;
    SeekBar seekBar;
    int currentMusicPosition;
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

        }
        //circularImage = findViewById(R.id.music_activity_circularImage);
        coverImage = findViewById(R.id.music_activity_image);
        play = findViewById(R.id.playButton);
        repeat = findViewById(R.id.repeat);
        shuffle = findViewById(R.id.shuffle);
        seekbackward = findViewById(R.id.seekBackwards);
        seekforward = findViewById(R.id.seekTowards);
        ////////////////////////
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
        NavigationView navigationView = findViewById(R.id.musicActivityNavigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });

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
                text.setText(mAudios.get(position).getTitle());
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
        final Handler handle2 = new Handler();
        Runnable run2 = new Runnable() {
            @Override
            public void run() {
                Bitmap image = BitmapFactory.decodeFile(mAudios.get(position).getAlbumArtPath());
                if (image != null) {
                    coverImage.setImageBitmap(image);
                } else if (image == null) {
                    coverImage.setImageResource(R.drawable.a2);
                }
                handle2.postDelayed(this, 1000);
            }
        };
        handle2.post(run2);
        ///////////////////////////////////////////////////
        noRepeat();
    }

    @Override
    public void onAppGotoForeground() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu_toggle, menu);
        MenuItem item = menu.findItem(R.id.action_toggle);
        MenuItem item2 = menu.findItem(R.id.Home_toggle);
        item.setActionView(R.layout.toggle_button_layout);
        item2.setActionView(R.layout.toggle_home_layout);
        final ImageButton button = item.getActionView().findViewById(R.id.toggleButton);
        final ImageButton button2 = item2.getActionView().findViewById(R.id.toggleHome);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getBaseContext(), button);
                popupMenu.getMenuInflater().inflate(R.menu.song_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.song_menu_play) {

                        } else if (item.getItemId() == R.id.song_menu_delete) {

                        } else if (item.getItemId() == R.id.song_menu_addToPlayList) {

                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivityIntent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(mainActivityIntent);
            }
        });
        return true;
    }

    @Override
    public void onAppGotoBackground() {
        Toast.makeText(this, "background", Toast.LENGTH_SHORT).show();
    }

    public void play(View view) {
        if (mediaPlayer.isPlaying()) {
            play.setSelected(false);
            mediaPlayer.pause();
        } else if (!mediaPlayer.isPlaying()) {
            play.setSelected(true);
            mediaPlayer.start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) | super.onOptionsItemSelected(item);
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
        RadioButton btn1 = menuView.findViewById(R.id.pop_up_window_repeat_one);
        btn1.setChecked(selected[0]);
        RadioButton btn2 = menuView.findViewById(R.id.pop_up_window_repeat_all);
        btn2.setChecked(selected[1]);
        RadioButton btn3 = menuView.findViewById(R.id.pop_up_window_No_repeat);
        btn3.setChecked(selected[2]);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.pop_up_window_repeat_one) {
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
                } else if (checkedId == R.id.pop_up_window_No_repeat) {
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
                mediaPlayer = null;
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
                    position++;
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
        seekBar.setProgress(currentMusicPosition);
    }

    @Override
    public void onBackPressed() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }


}