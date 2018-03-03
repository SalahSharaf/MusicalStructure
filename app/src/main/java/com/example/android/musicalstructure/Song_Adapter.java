package com.example.android.musicalstructure;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ALQasem on 03/03/2018.
 */

public class Song_Adapter extends ArrayAdapter<File> implements View.OnClickListener {
    Context context;
    int resource;
    ArrayList<File> media;
    ImageButton playSongButton;
    ImageButton songOptions;
    Uri uri;

    public Song_Adapter(@NonNull Context context, int resource, ArrayList media) {
        super(context, resource);
        this.context = context;
        this.media = media;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.song_layout, null);
        }
        ////getting media information
        uri = Uri.parse(media.get(position).getAbsolutePath());
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(context, uri);
        //setting media name
        TextView songNameText = v.findViewById(R.id.song_name);
        String songName = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        songNameText.setText(songName);
        /// setting media duration
        TextView songDurationText = v.findViewById(R.id.song_duration);
        String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        songDurationText.setText(duration);
        //// setting buttons click listeners
        playSongButton = v.findViewById(R.id.song_play_button);
        playSongButton.setOnClickListener(this);
        songOptions = v.findViewById(R.id.song_options);
        songOptions.setOnClickListener(this);
        ////return customized view
        return v;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.song_play_button) {
            MainActivity.mediaPlayer = MediaPlayer.create(context, uqri);
            MainActivity.mediaPlayer.start();
        } else if (v.getId() == R.id.song_options) {
            PopupMenu popup = new PopupMenu(context, songOptions);
            popup.getMenuInflater().inflate(R.menu.song_popup_menu, popup.getMenu());
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
        }
    }
}
