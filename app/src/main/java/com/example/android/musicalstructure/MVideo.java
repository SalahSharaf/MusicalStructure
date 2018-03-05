package com.example.android.musicalstructure;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by ALQasem on 04/03/2018.
 */
public class MVideo {
    private File data;
    private String title;
    private String album;
    private String artisit;
    private String category;
    private int date;
    private Long duration;
    private Bitmap thumbonial;
    private int resolution;

    public MVideo(String album, String title, String artisit, String category, int date, Long duration, Bitmap thumbonial, int resolution, File data) {
        this.album = album;
        this.title = title;
        this.artisit = artisit;
        this.category = category;
        this.date = date;
        this.duration = duration;
        this.thumbonial = thumbonial;
        this.resolution = resolution;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResolution() {
        return resolution;
    }

    public Bitmap getThumbonial() {
        return thumbonial;
    }

    public void setThumbonial(Bitmap thumbonial) {
        this.thumbonial = thumbonial;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
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

    public String getArtisit() {
        return artisit;
    }

    public void setArtisit(String artisit) {
        this.artisit = artisit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

}
