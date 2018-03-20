package com.example.android.musicalstructure;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ALQasem on 13/03/2018.
 */

public class MusicListNavigationAdapter extends ArrayAdapter<MAudio> implements View.OnClickListener {

    Context context;
    ArrayList<MAudio> audios;
    ImageButton playSongButton;
    ImageButton songOptions;
    CardView cardView;

    public MusicListNavigationAdapter(@NonNull Context context, ArrayList<MAudio> audios) {
        super(context, 0, audios);
        this.audios = audios;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.music_video_layout, null);
        //setting media name
        TextView songNameText = convertView.findViewById(R.id.layout2_song_name);
        songNameText.setText(audios.get(position).getTitle());
        // setting buttons click listeners
        cardView = convertView.findViewById(R.id.layout2_card_view);
        cardView.setOnClickListener(this);
        cardView.setTag(position);
        playSongButton = convertView.findViewById(R.id.layout2_song_play_button);
        playSongButton.setOnClickListener(this);
        playSongButton.setTag(position);
        if (MusicDisplayActivity.position != position && !MusicDisplayActivity.mediaPlayer.isPlaying()) {
            playSongButton.setSelected(false);
        } else if (MusicDisplayActivity.position == position && MusicDisplayActivity.mediaPlayer.isPlaying()) {
            playSongButton.setSelected(true);
        } else if (MusicDisplayActivity.position == position) {
            cardView.setBackgroundResource(R.color.colorPrimaryLight);
        }
        songOptions = convertView.findViewById(R.id.layout2_options_button);
        songOptions.setOnClickListener(this);
        cardView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MusicDisplayActivity.position == (int) playSongButton.getTag()) {
                    cardView.setBackgroundResource(R.color.colorPrimaryLight);
                } else {
                    cardView.setBackgroundResource(R.color.colorPrimary);
                }
                cardView.postDelayed(this, 100);
            }
        },100);
        playSongButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MusicDisplayActivity.position == (int) playSongButton.getTag() && MusicDisplayActivity.mediaPlayer.isPlaying()) {
                    playSongButton.setSelected(true);
                } else {
                    playSongButton.setSelected(false);
                }
                playSongButton.postDelayed(this, 100);
            }
        },100);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout2_song_play_button) {
            ImageButton play = (ImageButton) v;
            int position = (int) v.getTag();
            if (MusicDisplayActivity.position != position) {
                play.setSelected(true);
                CardView cardView = (CardView) v.getParent().getParent();
                cardView.setBackgroundResource(R.color.colorPrimaryLight);
                MusicDisplayActivity.position = position;
                MusicDisplayActivity.mediaPlayer.release();
                MusicDisplayActivity.mediaPlayer = new MediaPlayer();
                try {
                    MusicDisplayActivity.mediaPlayer.setDataSource(audios.get(position).getData().getAbsolutePath());
                    MusicDisplayActivity.mediaPlayer.prepare();
                } catch (IOException e) {
                    Toast.makeText(context, "Couldn't Play Music", Toast.LENGTH_SHORT).show();
                }
                MusicDisplayActivity.mediaPlayer.start();
            } else if (MusicDisplayActivity.position == position && MusicDisplayActivity.mediaPlayer.isPlaying()) {
                MusicDisplayActivity.mediaPlayer.pause();
                play.setSelected(false);
            } else if (MusicDisplayActivity.position == position && !MusicDisplayActivity.mediaPlayer.isPlaying()) {
                MusicDisplayActivity.mediaPlayer.start();
                play.setSelected(true);
            }
        } else if (v.getId() == R.id.layout2_options_button) {
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

        } else if (v.getId() == R.id.song_cover) {
            int position = (int) v.getTag();
            if (MusicDisplayActivity.position != position) {
                CardView cardView = (CardView) v;
                cardView.setBackgroundResource(R.color.colorPrimaryLight);
                MusicDisplayActivity.position = position;
                MusicDisplayActivity.mediaPlayer.release();
                MusicDisplayActivity.mediaPlayer = new MediaPlayer();
                try {
                    MusicDisplayActivity.mediaPlayer.setDataSource(audios.get(position).getData().getAbsolutePath());
                    MusicDisplayActivity.mediaPlayer.prepare();
                } catch (IOException e) {
                    Toast.makeText(context, "Couldn't Play Music", Toast.LENGTH_SHORT).show();
                }
                MusicDisplayActivity.mediaPlayer.start();
            }
        }
    }
}