package com.example.android.musicalstructure;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.PersistableBundle;
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
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class DisplayVideoActivity extends AppCompatActivity implements View.OnClickListener, AppVisibilityDetector.AppVisibilityCallback {

    SeekBar seekBar;
    ImageButton playButton;
    ImageButton forwardButton;
    ImageButton backwardButton;
    ImageButton repeatButton;
    ImageButton fullScreenButton;
    ArrayList<MVideo> videos;
    public static MVideo workingVideo;
    ListView listView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    public static int position = -10;
    public static int lastPosition = -10;
    TextView text;
    public static MediaPlayer videoMedia;
    public SurfaceView videoSurface;
    SurfaceHolder holder;
    //RelativeLayout.LayoutParams paramsNotFullscreen;
    LinearLayout controlGroup;
    //RelativeLayout.LayoutParams paramsNotFullScreenControlGroup;
    Animation fadeIn, fadeOut;
    int currentDuration;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_video);
        ////////////////////////////////////////////////////////////////// setting up the video view
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
        fadeIn.setDuration(2000);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_animation);
        fadeOut.setDuration(2000);
        RelativeLayout bigwrraper = findViewById(R.id.bigWrraperTouchable);
        bigwrraper.setOnClickListener(this);
        controlGroup = findViewById(R.id.videoControlGroup);
        controlGroup.postDelayed(controlGroupChecker, 4000);
        videos = MainActivity.videos;
        workingVideo = videos.get(position);
        videoSurface = findViewById(R.id.videoView);
        holder = videoSurface.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                videoMedia.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        videoMedia = new MediaPlayer();
        try {
            videoMedia.setDataSource(workingVideo.getData().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoMedia.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Toast.makeText(DisplayVideoActivity.this, "working", Toast.LENGTH_SHORT).show();
                mp.start();
            }
        });
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(videoMedia.getDuration());
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            seekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoMedia.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                videoMedia.seekTo(seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        playButton = findViewById(R.id.playButton);
        playButton.setSelected(true);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton.setSelected(!playButton.isSelected());
                if (videoMedia.isPlaying()) {
                    videoMedia.pause();
                } else if (!videoMedia.isPlaying()) {
                    videoMedia.stop();
                }
            }
        });

        forwardButton = findViewById(R.id.seekTowards);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentSec = videoMedia.getCurrentPosition();
                if (currentSec != videoMedia.getDuration()) {
                    int plusSec = 1000 * 10; ///ten seconds
                    videoMedia.seekTo(currentSec + plusSec);
                    seekBar.setProgress(currentSec + plusSec);
                }
            }
        });

        backwardButton = findViewById(R.id.seekBackwards);
        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentSec = videoMedia.getCurrentPosition();
                if (currentSec != 0) {
                    int subtractedSec = 1000 * 10; ///ten seconds
                    videoMedia.seekTo(currentSec - subtractedSec);
                    seekBar.setProgress(currentSec - subtractedSec);
                }
            }
        });

        repeatButton = findViewById(R.id.repeat);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializePopUpWindow(v);
            }
        });

        fullScreenButton = findViewById(R.id.fullScreen);
        fullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    fullScreenButton.setSelected(true);
                    //getResources().getConfiguration().orientation = 1;
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    fullScreenButton.setSelected(false);
                    //getResources().getConfiguration().orientation = 2;
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });

        ///////////////////////////////////////////////////////////////// setting up the navigation videos
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            listView = findViewById(R.id.videosListView);
            NavigationVideoAdapter adapter = new NavigationVideoAdapter(this, videos);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(adapter);
            //////////////////////////////////////////////////////////////// customizing Action Bar
            text = new TextView(this);
            text.setTextColor(getResources().getColor(R.color.colorAccent));
            text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            text.setText(workingVideo.getTitle());
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(text);
            actionBar.setDisplayHomeAsUpEnabled(true);
            //////////////////////////////////////////////////////////////setting up the Navigation View
            drawerLayout = findViewById(R.id.videoActivity_drawer_layout);
            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();

            /////////////////////////////////////////////////////////////// updating
            final Handler handle = new Handler();
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    workingVideo = videos.get(position);
                    text.setText(workingVideo.getTitle());
                    if (videoMedia.isPlaying()) {
                        playButton.setSelected(true);
                    } else if (!videoMedia.isPlaying()) {
                        playButton.setSelected(false);
                    }
                    handle.postDelayed(this, 100);
                }
            };
            handle.post(run);
        }
        handler = new Handler();
        handler.postDelayed(onEverySecond, 1000);
    }

    private Runnable controlGroupChecker = new Runnable() {
        @Override
        public void run() {
                controlGroup.postDelayed(this, 4000);
                controlGroup.startAnimation(fadeOut);
        }
    };

    private void initializePopUpWindow(View view) {
        View menuView = LayoutInflater.from(this).inflate(R.layout.repeat_pop_up_window, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        PopupWindow menu = new PopupWindow(menuView, width, height, focusable);
        menu.showAtLocation(view, Gravity.CENTER_HORIZONTAL, (int) view.getX(), (int) view.getY());
        RadioGroup group = menuView.findViewById(R.id.options_radio_group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.pop_up_window_No_repeat) {
                    Toast.makeText(getBaseContext(), "No Repeat", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.pop_up_window_repeat_all) {
                    repeatAllFiles();
                } else if (checkedId == R.id.pop_up_window_repeat_one) {
                    repeatOneFile();
                }
            }
        });
    }

    private void repeatOneFile() {
        Toast.makeText(this, "Repeat One", Toast.LENGTH_SHORT).show();
        videoMedia.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    videoMedia.setDataSource(workingVideo.getData().getAbsolutePath());
                    videoMedia.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void repeatAllFiles() {
        Toast.makeText(this, "Repeat All Files", Toast.LENGTH_SHORT).show();
        videoMedia.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (position >= videos.size()) {
                    position = 0;
                } else if (position != videos.size() - 1) {
                    position += 1;
                    try {
                        videoMedia.setDataSource(workingVideo.getData().getAbsolutePath());
                        videoMedia.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private Runnable onEverySecond = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(videoMedia.getCurrentPosition());
            handler.postDelayed(this, 100);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu_toggle, menu);
        MenuItem item = menu.findItem(R.id.action_toggle);
        item.setActionView(R.layout.toggle_button_layout);
        final ImageButton button = item.getActionView().findViewById(R.id.toggleButton);
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
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.videoControlGroup || v.getId() != R.id.playButton || v.getId() != R.id.repeat || v.getId() != R.id.fullScreen || v.getId() != R.id.seekBackwards || v.getId() != R.id.seekTowards) {
            if (controlGroup.getVisibility() == View.INVISIBLE) {
                controlGroup.startAnimation(fadeIn);
                controlGroup.post(controlGroupChecker);
            } else if (controlGroup.getVisibility() == View.VISIBLE) {
                controlGroup.post(controlGroupChecker);
            }
        }
    }

    @Override
    public void onAppGotoForeground() {

    }

    @Override
    public void onAppGotoBackground() {
        Toast.makeText(this, "closed", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MyService.class));
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("currentDuration", videoMedia.getCurrentPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentDuration = savedInstanceState.getInt("currentDuration");
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoMedia.seekTo(currentDuration);
        seekBar.setProgress(currentDuration);
    }

}