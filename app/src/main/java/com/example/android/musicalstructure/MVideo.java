package com.example.android.musicalstructure;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;

/**
 * Created by ALQasem on 04/03/2018.
 */
public class MVideo implements Parcelable {

    private int id;
    private File data;
    private String title;
    private String album;
    private String artist;
    private String category;
    private int date;
    private Long duration;
    private Bitmap thumbonial;
    private int resolution;

    public MVideo(int id, String album, String title, String artist, String category, int date, Long duration, Bitmap thumbonial, int resolution, File data) {
        this.album = album;
        this.title = title;
        this.artist = artist;
        this.category = category;
        this.date = date;
        this.duration = duration;
        this.thumbonial = thumbonial;
        this.resolution = resolution;
        this.data = data;
        this.id = id;
    }

    protected MVideo(Parcel in) {
        id = in.readInt();
        title = in.readString();
        album = in.readString();
        artist = in.readString();
        category = in.readString();
        date = in.readInt();
        if (in.readByte() == 0) {
            duration = null;
        } else {
            duration = in.readLong();
        }
        thumbonial = in.readParcelable(Bitmap.class.getClassLoader());
        resolution = in.readInt();
    }

    public static final Creator<MVideo> CREATOR = new Creator<MVideo>() {
        @Override
        public MVideo createFromParcel(Parcel in) {
            return new MVideo(in);
        }

        @Override
        public MVideo[] newArray(int size) {
            return new MVideo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return artist;
    }

    public void setArtisit(String artisit) {
        this.artist = artisit;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(album);
        dest.writeString(artist);
        dest.writeString(category);
        dest.writeInt(date);
        if (duration == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(duration);
        }
        dest.writeParcelable(thumbonial, flags);
        dest.writeInt(resolution);
    }
}
