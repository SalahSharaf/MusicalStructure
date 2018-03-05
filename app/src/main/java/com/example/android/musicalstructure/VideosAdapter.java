package com.example.android.musicalstructure;

import android.content.Context;
import android.content.res.ColorStateList;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ALQasem on 03/03/2018.
 */

public class VideosAdapter extends ArrayAdapter<MVideo> implements View.OnClickListener {

    Context context;
    ArrayList<MVideo> videos;
    ImageButton playSongButton;
    ImageButton songOptions;
    Uri uri;

    public VideosAdapter(@NonNull Context context, ArrayList<MVideo> videos) {
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
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.song_layout, null);
        ////getting media data
        uri = Uri.parse(videos.get(position).getData().getAbsolutePath());
        //setting media name
        TextView songNameText = convertView.findViewById(R.id.song_name);
        songNameText.setText(videos.get(position).getTitle());
        /// setting media duration
        TextView songDurationText = convertView.findViewById(R.id.song_duration);
        songDurationText.setText(videos.get(position).getDuration().toString());
        ////////////////setting up the thumbonial
        ImageView imageView = convertView.findViewById(R.id.song_cover);
        imageView.setImageBitmap(videos.get(position).getThumbonial());
        //// setting buttons click listeners
        playSongButton = convertView.findViewById(R.id.song_play_button);
        playSongButton.setOnClickListener(this);
        songOptions = convertView.findViewById(R.id.song_options);
        songOptions.setOnClickListener(this);
        ////return customized view
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.song_play_button) {
            MediaPlayer.create(context, uri).start();
            Button btn=(Button) v;
            btn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_pause_black_24dp));
            ColorStateList colorStateList=ColorStateList.valueOf(R.color.colorAccent);
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