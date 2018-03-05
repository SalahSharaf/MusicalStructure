package com.example.android.musicalstructure;

import java.io.File;

/**
 * Created by ALQasem on 04/03/2018.
 */

public class MAudio {
    private File data;
    private String title;
    private String album;
    private String artist;
    private Long duration;
    private String track;
    int date;

    public MAudio(String album, String title, String artist, Long duration, String track, int date, File data) {
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.track = track;
        this.date = date;
        this.data = data;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getData() {
        return data;
    }

    public void setData(File data) {
        this.data = data;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

}
