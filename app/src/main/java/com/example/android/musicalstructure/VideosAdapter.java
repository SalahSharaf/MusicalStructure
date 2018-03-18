package com.example.android.musicalstructure;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
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
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class VideosAdapter extends ArrayAdapter<MVideo> implements View.OnClickListener {

    Context context;
    ArrayList<MVideo> videos;
    ImageButton playSongButton;
    ImageButton songOptions;
    ImageView songCover;

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
        convertView = inflater.inflate(R.layout.video_layout, null);
        //setting media name
        TextView songNameText = convertView.findViewById(R.id.song_name);
        songNameText.setText(videos.get(position).getTitle());
        ////////////////setting up the thumbonail
        ImageView imageView = convertView.findViewById(R.id.song_cover);
        imageView.setImageBitmap(videos.get(position).getThumbonial());
        //// setting buttons click listeners
        playSongButton = convertView.findViewById(R.id.song_play_button);
        playSongButton.setOnClickListener(this);
        if (DisplayVideoActivity.position != position) {
            playSongButton.setSelected(false);
        } else if (DisplayVideoActivity.position == position) {
            playSongButton.setSelected(true);
        }
        songOptions = convertView.findViewById(R.id.song_options);
        songOptions.setOnClickListener(this);
        songCover = convertView.findViewById(R.id.song_cover);
        songCover.setOnClickListener(this);
        //return customized view
        return convertView;

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.song_play_button) {

            final ImageButton playSongButton = (ImageButton) v;
            View parentRow = (View) v.getParent();
            ListView listView = (ListView) parentRow.getParent().getParent().getParent();
            int position = listView.getPositionForView(parentRow);
            if(!playSongButton.isSelected()) {
                View c = getViewByPosition(DisplayVideoActivity.lastPosition, listView);
                ImageButton e = c.findViewById(R.id.layout2_song_play_button);
                e.setSelected(false);
            }
            playSongButton.setSelected(!playSongButton.isSelected());


            /*
            if (DisplayVideoActivity.songPlaying && DisplayVideoActivity.position == position) {
                DisplayVideoActivity.songPlaying = false;
            } else if (!DisplayVideoActivity.songPlaying) {
                DisplayVideoActivity.songPlaying = true;
                Toast.makeText(context, videos.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            } else if (DisplayVideoActivity.songPlaying && DisplayVideoActivity.position != position) {
                DisplayVideoActivity.songPlaying = true;
                DisplayVideoActivity.lastPosition = DisplayVideoActivity.position;
                DisplayVideoActivity.position = position;
            }
            */
        }

        if (v.getId() == R.id.song_options) {
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

        if (v.getId() == R.id.song_cover) {
            View parentRow = (View) v.getParent();
            ListView listView = (ListView) parentRow.getParent().getParent().getParent();
            int position = listView.getPositionForView(parentRow);
            Intent intent = new Intent(context, DisplayVideoActivity.class);
            DisplayVideoActivity.lastPosition = DisplayVideoActivity.position;
            DisplayVideoActivity.position = position;
            DisplayVideoActivity.workingVideo=videos.get(position);
            context.startActivity(intent);
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