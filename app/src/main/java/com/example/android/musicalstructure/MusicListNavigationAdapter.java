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

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ALQasem on 13/03/2018.
 */

public class MusicListNavigationAdapter extends ArrayAdapter<MAudio> implements View.OnClickListener, Runnable {
    Context context;
    ArrayList<MAudio> audios;
    ImageButton playSongButton;
    ImageButton songOptions;
    CardView cardView;

    public MusicListNavigationAdapter(@NonNull Context context, ArrayList<MAudio> audios) {
        super(context, 0, audios);
        this.audios = audios;
        Thread thread = new Thread(this);
        thread.start();
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
        cardView.setTag(R.id.position, position);
        playSongButton = convertView.findViewById(R.id.layout2_song_play_button);
        playSongButton.setOnClickListener(this);
        playSongButton.setTag(R.id.position, position);
        if (MusicDisplayActivity.position != position && !MusicDisplayActivity.mediaPlayer.isPlaying()) {
            playSongButton.setSelected(false);
        } else if (MusicDisplayActivity.position == position && MusicDisplayActivity.mediaPlayer.isPlaying()) {
            playSongButton.setSelected(true);
        } else if (MusicDisplayActivity.position == position) {
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
            ImageButton play = (ImageButton) v;
            if (MusicDisplayActivity.position != (int) v.getTag(R.id.position)) {
                int position = Integer.getInteger((v.getTag(R.id.position).toString()));
                MusicDisplayActivity.position = position;
                play.setSelected(true);
                CardView cardView = (CardView) v.getParent().getParent();
                cardView.setBackgroundResource(R.color.colorPrimaryLight);
                context.startActivity(new Intent(context, MusicDisplayActivity.class));
            } else if (MusicDisplayActivity.position == (int) v.getTag(R.id.position) && MusicDisplayActivity.mediaPlayer.isPlaying()) {
                MusicDisplayActivity.mediaPlayer.pause();
                play.setSelected(false);
            } else if (MusicDisplayActivity.position == (int) v.getTag(R.id.position) && !MusicDisplayActivity.mediaPlayer.isPlaying()) {
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
            if (MusicDisplayActivity.position != (int) v.getTag(R.id.position)) {
                CardView cardView = (CardView) v;
                cardView.setBackgroundResource(R.color.colorPrimaryLight);
                int position = (int) v.getTag(R.id.position);
                MusicDisplayActivity.position = position;
                MusicDisplayActivity.mediaPlayer = new MediaPlayer();
                MusicDisplayActivity.mediaPlayer.release();
                try {
                    MusicDisplayActivity.mediaPlayer.setDataSource(audios.get(position).getData().getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MusicDisplayActivity.mediaPlayer.start();
            }
        }
    }

    @Override
    public void run() {
        //android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        if (Integer.getInteger(playSongButton.getTag(R.id.position).toString()) != MusicDisplayActivity.position) {
            playSongButton.setSelected(false);
        } else if (playSongButton.getTag(R.id.position) == Integer.getInteger(playSongButton.getTag(R.id.position).toString())) {
            playSongButton.setSelected(true);
        }
        if (Integer.getInteger(cardView.getTag(R.id.position).toString()) != MusicDisplayActivity.position) {
            cardView.setBackgroundResource(R.color.colorPrimary);
        } else if (cardView.getTag(R.id.position) == Integer.getInteger(playSongButton.getTag(R.id.position).toString())) {
            cardView.setBackgroundResource(R.color.colorPrimaryLight);
        }

    }
}
