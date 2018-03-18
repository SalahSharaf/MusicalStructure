package com.example.android.musicalstructure;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ALQasem on 05/03/2018.
 */

public class MusicAdapter extends ArrayAdapter<MAudio> implements View.OnClickListener {
    Context context;
    ArrayList<MAudio> audios;
    ImageButton playSongButton;
    ImageButton songOptions;

    public MusicAdapter(@NonNull Context context, ArrayList<MAudio> videos) {
        super(context, 0, videos);
        this.context = context;
        this.audios = videos;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ///////////////////////////////////////////////
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.video_layout, null);
        //setting media name
        TextView songNameText = convertView.findViewById(R.id.song_name);
        songNameText.setText(audios.get(position).getTitle());
        ////////////////////////////////setting the thumbonails
        ImageView imageView = convertView.findViewById(R.id.song_cover);
        if (!audios.get(position).getAlbumArtPath().equals("")) {
            Bitmap image = BitmapFactory.decodeFile(audios.get(position).getAlbumArtPath());
            imageView.setImageBitmap(image);
        } else {
            imageView.setImageResource(R.drawable.a4);
        }
        imageView.setOnClickListener(this);
        imageView.setTag(R.id.mine3, audios.get(position).getData().getAbsolutePath());
        imageView.setTag(R.id.mine2, position);
        //// setting buttons click listeners
        playSongButton = convertView.findViewById(R.id.song_play_button);
        playSongButton.setOnClickListener(this);
        songOptions = convertView.findViewById(R.id.song_options);
        songOptions.setOnClickListener(this);
        playSongButton.setTag(R.id.mine3, audios.get(position).getData());
        playSongButton.setTag(R.id.mine2, position);
        ////return customized view
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.song_play_button) {
            try {
                MusicDisplayActivity.mediaPlayer=new MediaPlayer();
                MusicDisplayActivity.mediaPlayer.setDataSource(v.getTag(R.id.mine3).toString());
                MusicDisplayActivity.position = Integer.parseInt(v.getTag(R.id.mine2).toString());
                MusicDisplayActivity.mediaPlayer.start();
            } catch (Exception e) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }
            MusicDisplayActivity.mediaPlayer.start();
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
        } else if (v.getId() == R.id.song_cover) {
            MusicDisplayActivity.position = Integer.parseInt(v.getTag(R.id.mine2).toString());
            context.startActivity(new Intent(context, MusicDisplayActivity.class));
        }
    }
}
