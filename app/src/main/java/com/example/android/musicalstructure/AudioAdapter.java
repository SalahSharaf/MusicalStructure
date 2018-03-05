package com.example.android.musicalstructure;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ALQasem on 05/03/2018.
 */

public class AudioAdapter extends ArrayAdapter<MAudio> implements View.OnClickListener {
    Context context;
    ArrayList<MAudio> videos;
    ImageButton playSongButton;
    ImageButton songOptions;
    Uri uri;
    int drawables[] = new int[3];

    public AudioAdapter(@NonNull Context context, ArrayList<MAudio> videos) {
        super(context, 0, videos);
        this.context = context;
        this.videos = videos;
        drawables[0] = R.drawable.a1;
        drawables[1] = R.drawable.a2;
        drawables[2] = R.drawable.a3;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ///////////////////////////////////////////////
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
        ////////////////////////////////setting the thumbonails
        ImageView imageView = convertView.findViewById(R.id.song_cover);
        Random random=new Random();
        int index = random.nextInt(2-0);
        imageView.setImageResource(drawables[index]);
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
