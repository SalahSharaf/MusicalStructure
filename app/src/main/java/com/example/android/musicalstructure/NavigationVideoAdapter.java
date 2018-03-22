package com.example.android.musicalstructure;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ALQasem on 07/03/2018.
 */

public class NavigationVideoAdapter extends ArrayAdapter<MVideo> implements View.OnClickListener {
    Context context;
    ArrayList<MVideo> videos;
    ImageButton playSongButton;
    ImageButton songOptions;
    CardView cardView;

    public NavigationVideoAdapter(@NonNull Context context, ArrayList<MVideo> videos) {
        super(context, 0, videos);
        this.context = context;
        this.videos = videos;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.music_video_layout, null);
        //setting media name
        TextView songNameText = convertView.findViewById(R.id.layout2_song_name);
        songNameText.setText(videos.get(position).getTitle());
        // setting buttons click listeners
        cardView = convertView.findViewById(R.id.layout2_card_view);
        cardView.setOnClickListener(this);
        playSongButton = convertView.findViewById(R.id.layout2_song_play_button);
        playSongButton.setOnClickListener(this);
        if (DisplayVideoActivity.position != position) {
            playSongButton.setSelected(false);
        } else if (DisplayVideoActivity.position == position && DisplayVideoActivity.videoMedia.isPlaying()) {
            playSongButton.setSelected(true);
        } else if (DisplayVideoActivity.position == position) {
            cardView.setBackgroundResource(R.color.colorPrimaryLight);
        }
        songOptions = convertView.findViewById(R.id.layout2_options_button);
        songOptions.setOnClickListener(this);
        //return customized view
        return convertView;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.layout2_song_play_button) {
            ImageButton playSongButton = (ImageButton) v;
            View parentRow = (View) v.getParent();
            ListView listView = (ListView) parentRow.getParent().getParent().getParent();
            int position = listView.getPositionForView(parentRow);
            if (DisplayVideoActivity.lastPosition != -10) {
                View c = getViewByPosition(DisplayVideoActivity.lastPosition, listView);
                ImageButton e = c.findViewById(R.id.layout2_song_play_button);
                e.setSelected(false);
            }
            if (DisplayVideoActivity.videoMedia.isPlaying() && DisplayVideoActivity.position == position) {
                DisplayVideoActivity.videoMedia.pause();
                playSongButton.setSelected(false);
            } else if (!DisplayVideoActivity.videoMedia.isPlaying() && DisplayVideoActivity.position == position) {
                DisplayVideoActivity.videoMedia.start();
                playSongButton.setSelected(true);
            } else if (!DisplayVideoActivity.videoMedia.isPlaying() && DisplayVideoActivity.position != position) {
                DisplayVideoActivity.lastPosition = DisplayVideoActivity.position;
                DisplayVideoActivity.position = position;
                DisplayVideoActivity.videoMedia.release();
                DisplayVideoActivity.videoMedia = new MediaPlayer();
                DisplayVideoActivity.holder = DisplayVideoActivity.videoSurface.getHolder();
                DisplayVideoActivity.holder.addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
                        DisplayVideoActivity.videoMedia.setDisplay(holder);
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {

                    }
                });
                try {
                    DisplayVideoActivity.videoMedia.setDataSource(videos.get(position).getData().getAbsolutePath());
                    DisplayVideoActivity.videoMedia.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DisplayVideoActivity.videoMedia.start();
                cardView.setBackgroundResource(R.color.colorPrimaryLight);
                playSongButton.setSelected(true);
                Toast.makeText(context, videos.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            } else if (DisplayVideoActivity.videoMedia.isPlaying() && DisplayVideoActivity.position != position) {
                DisplayVideoActivity.lastPosition = DisplayVideoActivity.position;
                DisplayVideoActivity.position = position;
                DisplayVideoActivity.videoMedia.release();
                DisplayVideoActivity.videoMedia = new MediaPlayer();
                DisplayVideoActivity.holder = DisplayVideoActivity.videoSurface.getHolder();
                DisplayVideoActivity.holder.addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
                        DisplayVideoActivity.videoMedia.setDisplay(holder);
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {

                    }
                });
                try {
                    DisplayVideoActivity.videoMedia.setDataSource(videos.get(position).getData().getAbsolutePath());
                    DisplayVideoActivity.videoMedia.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DisplayVideoActivity.videoMedia.start();
                cardView.setBackgroundResource(R.color.colorPrimaryLight);
                playSongButton.setSelected(false);
                Toast.makeText(context, videos.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        }

        if (v.getId() == R.id.layout2_options_button) {
            ImageButton songOptions = (ImageButton) v;
            PopupMenu popup = new PopupMenu(context, songOptions);
            popup.getMenuInflater().inflate(R.menu.song_popup_menu, popup.getMenu());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                popup.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if (item.getItemId() == R.id.song_menu_play) {

                    } else if (item.getItemId() == R.id.song_menu_delete) {

                    } else if (item.getItemId() == R.id.song_menu_addToPlayList) {

                    }
                    return true;
                }
            });
            popup.show();
        }

        if (v.getId() == R.id.layout2_card_view) {
            View parentRow = (View) v.getParent();
            ListView listView = (ListView) parentRow.getParent();
            int position = listView.getPositionForView(parentRow);
            if (DisplayVideoActivity.position != position && DisplayVideoActivity.lastPosition != -10) {
                DisplayVideoActivity.lastPosition = DisplayVideoActivity.position;
                DisplayVideoActivity.position = position;
                DisplayVideoActivity.videoMedia.release();
                DisplayVideoActivity.videoMedia = new MediaPlayer();
                DisplayVideoActivity.holder = DisplayVideoActivity.videoSurface.getHolder();
                DisplayVideoActivity.holder.addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
                        DisplayVideoActivity.videoMedia.setDisplay(holder);
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                        DisplayVideoActivity.videoMedia.setDisplay(holder);
                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {

                    }
                });
                try {
                    DisplayVideoActivity.videoMedia.setDataSource(videos.get(position).getData().getAbsolutePath());
                    DisplayVideoActivity.videoMedia.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                View c = getViewByPosition(DisplayVideoActivity.lastPosition, listView);
                ImageButton e = c.findViewById(R.id.layout2_song_play_button);
                e.setSelected(false);
                DisplayVideoActivity.videoMedia.start();
                cardView.setBackgroundResource(R.color.colorPrimaryLight);
            }
        }
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
}
